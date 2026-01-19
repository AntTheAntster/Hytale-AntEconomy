package uk.anttheantster.anteconomy.balance;

import uk.anttheantster.anteconomy.AntEconomy;
import uk.anttheantster.anteconomy.utils.SQLGetter;

import java.util.*;
import java.util.concurrent.*;

public class BalanceController {
    private AntEconomy plugin;
    private SQLGetter data;
    public BalanceController(AntEconomy plugin, SQLGetter data){
        this.plugin = plugin;
        this.data = data;

        start();
    }

    private final ConcurrentHashMap<UUID, Integer> balances = new ConcurrentHashMap<>();
    private final Set<UUID> dirty = ConcurrentHashMap.newKeySet();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "AntEconomy DB-Updater");
        t.setDaemon(true);
        return t;
    });

    public int getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, 0);
    }

    public void setBalance(UUID uuid, int amount) {
        balances.put(uuid, amount);
        dirty.add(uuid);
    }

    public void resetBalance(UUID uuid) {
        setBalance(uuid, plugin.getConfig().getDefaultBalance()); //Later the 1000 will be changed to a config reference of whatever the user wants as their default-balance
    }

    // The actual lifecycle of the SQL Daemon
    public void start() {
        //1. Load balances on background thread at start
        scheduler.execute(() -> {
            try {

                Map<UUID, Integer> dbBalances = data.loadAllBalances();

                balances.clear();
                balances.putAll(dbBalances);

                dirty.clear();
                plugin.logger.atInfo().log("Loaded " + dbBalances.size() + " balances from DB to Memory!");
            } catch (Exception e) {
                e.printStackTrace();
                //Decide to disable plugin or run with no caching later on or make it configurable
            }
        });

        //2. Update the "dirty" uuid's every 'x' mins (dirty = changed) -- Reason: No point updating the SQL with entries of UUID's data that hasn't changed
        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateBalances();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.MINUTES);
    }

    public void shutdown() {
        //Push during shutdown/reboot
        updateBalances();
        scheduler.shutdownNow();
    }

    private void updateBalances() {
        //Copy and clear so that the threads keep working and don't lose sync during database pushes.
        List<UUID> toPush = new ArrayList<>(dirty);
        dirty.removeAll(toPush);

        for (int i = 0; i < toPush.size(); i++) {
            UUID uuid = toPush.get(i);
            Integer bal = balances.get(uuid);
            if (bal != null) data.setBalance(bal, uuid);
        }
    }

    public Executor getExecutor() {
        return scheduler;
    }

    public void loadPlayer(UUID uuid, int balance) {
        balances.put(uuid, balance);
        dirty.remove(uuid); // clean on load
    }

    public boolean hasPlayer(UUID uuid) {
        return balances.containsKey(uuid);
    }

}

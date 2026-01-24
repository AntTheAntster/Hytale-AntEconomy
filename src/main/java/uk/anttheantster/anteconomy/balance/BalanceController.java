package uk.anttheantster.anteconomy.balance;

import uk.anttheantster.anteconomy.ui.BalanceHudService;
import uk.anttheantster.anteconomy.utils.EconomyConfig;
import uk.anttheantster.anteconomy.utils.EconomyData;
import uk.anttheantster.anteconomy.utils.EconomyFileStore;

import java.util.*;
import java.util.concurrent.*;

public final class BalanceController {

    private final BalanceHudService hudService;

    private final EconomyFileStore store;
    private final long defaultBalance;
    private EconomyData data;
    private final EconomyConfig config;

    private final ConcurrentHashMap<UUID, Long> balances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UUID> nameIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, String> uuidToName = new ConcurrentHashMap<>();
    private final Set<UUID> dirty = ConcurrentHashMap.newKeySet();

    private final ScheduledExecutorService io = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "AntEconomy-FileStore");
        t.setDaemon(true);
        return t;
    });

    public BalanceController(EconomyFileStore store, long defaultBalance, EconomyConfig config, BalanceHudService hudService) {
        this.store = store;
        this.defaultBalance = defaultBalance;

        this.data = new EconomyData();
        this.config = config;

        this.hudService = hudService;

        start();
    }

    public void start() {
        io.execute(this::loadAll);

        io.scheduleAtFixedRate(() -> {
            try { flushAll(); } catch (Exception e) { e.printStackTrace(); }
        }, 5, 5, TimeUnit.MINUTES);
    }

    private void loadAll() {
        EconomyData data = store.load();

        balances.clear();
        nameIndex.clear();
        dirty.clear();

        data.players.forEach((uuidStr, entry) -> {
            UUID uuid = UUID.fromString(uuidStr);
            balances.put(uuid, entry.balance);
            if (entry.name != null) nameIndex.put(entry.name.toLowerCase(Locale.ROOT), uuid);
            uuidToName.put(uuid, entry.name);
        });

        data.names.forEach((lower, uuidStr) -> nameIndex.put(lower, UUID.fromString(uuidStr)));
    }

    public long getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, defaultBalance);
    }

    public void setBalance(UUID uuid, long amount) {
        balances.put(uuid, amount);
        dirty.add(uuid);
        hudService.onBalanceChanged(uuid);
    }

    public void onPlayerJoin(UUID uuid, String name) {
        uuidToName.put(uuid, name);

        String key = name.toLowerCase(Locale.ROOT);
        nameIndex.put(key, uuid);

        balances.putIfAbsent(uuid, defaultBalance);
        dirty.add(uuid);
    }

    public UUID resolveName(String name) {
        return nameIndex.get(name.toLowerCase(Locale.ROOT));
    }

    public void shutdown() {
        io.execute(this::flushAll);
        io.shutdown();
    }

    private void flushAll() {

        for (var e : balances.entrySet()) {
            UUID uuid = e.getKey();
            long bal = e.getValue();

            String name = null;
            data.players.put(uuid.toString(), new EconomyData.PlayerEntry(name, bal));
        }

        for (var e : nameIndex.entrySet()) {
            data.names.put(e.getKey(), e.getValue().toString());
        }

        store.saveAtomic(data);
        dirty.clear();
    }

    public Executor getExecutor() {
        return io;
    }

    public String getName(UUID uuid) {
        return uuidToName.getOrDefault(uuid, uuid.toString());
    }

    public String getCurrencyPrefix() {
        return config.getCurrencyPrefix();
    }
}

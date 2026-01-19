package uk.anttheantster.anteconomy.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import uk.anttheantster.anteconomy.AntEconomy;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.utils.SQLGetter;

import java.util.UUID;

public class PlayerJoin {

    private final AntEconomy plugin;
    private final BalanceController balanceController;
    private final SQLGetter data;
    private final int defaultBalance;


    public PlayerJoin(AntEconomy plugin, BalanceController balanceController, SQLGetter data, int defaultBalance) {
        this.plugin = plugin;
        this.balanceController = balanceController;
        this.data = data;
        this.defaultBalance = defaultBalance;
    }


    public void onJoin(PlayerConnectEvent event) {
        PlayerRef ref = event.getPlayerRef();

        UUID uuid = ref.getUuid();
        String name = ref.getUsername();

        if (plugin.mysql.isConnected()){
            if (!data.exists(uuid)){
                data.createPlayer(uuid, name, defaultBalance);
                data.setBalance(defaultBalance, uuid);
                data.setPlayerName(uuid, name);
            }
        }

        balanceController.getExecutor().execute(() -> {
            int balance = data.getBalance(uuid);

            if (!balanceController.hasPlayer(uuid)){
                balanceController.loadPlayer(uuid, balance);
            }
        });

    }
}

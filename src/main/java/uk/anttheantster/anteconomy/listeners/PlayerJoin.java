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
    private final SQLGetter sql;
    private final int defaultBalance;


    public PlayerJoin(AntEconomy plugin, BalanceController balanceController, SQLGetter sql, int defaultBalance) {
        this.plugin = plugin;
        this.balanceController = balanceController;
        this.sql = sql;
        this.defaultBalance = defaultBalance;
    }


    public void onJoin(PlayerConnectEvent event) {
        PlayerRef ref = event.getPlayerRef();

        UUID uuid = ref.getUuid();
        String name = ref.getUsername();

        if (plugin.mysql.isConnected()){
            if (!sql.exists(uuid)){
                sql.createPlayer(uuid, name, defaultBalance);
                sql.setBalance(defaultBalance, uuid);
                sql.setPlayerName(uuid, name);
            }
        }


    }
}

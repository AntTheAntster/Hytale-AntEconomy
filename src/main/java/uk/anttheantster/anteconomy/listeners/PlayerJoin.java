package uk.anttheantster.anteconomy.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import uk.anttheantster.anteconomy.AntEconomy;
import uk.anttheantster.anteconomy.balance.BalanceController;

import java.util.UUID;

public class PlayerJoin {

    private final BalanceController balanceController;


    public PlayerJoin(BalanceController balanceController) {
        this.balanceController = balanceController;
    }


    public void onJoin(PlayerConnectEvent event) {
        PlayerRef ref = event.getPlayerRef();

        UUID uuid = ref.getUuid();
        String name = ref.getUsername();

        balanceController.getExecutor().execute(() -> {
            balanceController.onPlayerJoin(uuid, name);
        });

    }
}

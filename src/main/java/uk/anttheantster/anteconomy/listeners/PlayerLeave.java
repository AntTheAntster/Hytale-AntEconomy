package uk.anttheantster.anteconomy.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import uk.anttheantster.anteconomy.ui.BalanceHudService;


public class PlayerLeave {
    private BalanceHudService balanceHudService;

    public PlayerLeave(BalanceHudService balanceHudService) {
        this.balanceHudService = balanceHudService;
    }
    public void onLeave(PlayerDisconnectEvent e) {
        balanceHudService.onPlayerRemoved(e.getPlayerRef().getUuid());
    }


}

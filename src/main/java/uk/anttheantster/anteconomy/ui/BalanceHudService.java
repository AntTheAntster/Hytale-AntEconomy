package uk.anttheantster.anteconomy.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import uk.anttheantster.anteconomy.balance.BalanceController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BalanceHudService {

    private final Map<UUID, BalanceHud> active = new ConcurrentHashMap<>();

    public void enable(Player player, PlayerRef playerRef, BalanceController controller) {
        if (active.containsKey(playerRef.getUuid())) return;

        BalanceHud hud = new BalanceHud(playerRef, controller);
        player.getHudManager().setCustomHud(playerRef, hud);

        active.put(playerRef.getUuid(), hud);

        hud.refreshFromController();
    }

    public void disable(Player player, PlayerRef playerRef) {
        active.remove(playerRef.getUuid());
        player.getHudManager().setCustomHud(playerRef, new ClearCustomHud(playerRef));
    }

    public void onBalanceChanged(UUID playerUuid) {
        BalanceHud hud = active.get(playerUuid);
        if (hud != null) {
            hud.setBalanceText();
        }
    }

    public void onPlayerRemoved(UUID playerUuid) {
        active.remove(playerUuid);
    }

    public boolean isEnabled(UUID playerUuid) {
        return active.containsKey(playerUuid);
    }
}

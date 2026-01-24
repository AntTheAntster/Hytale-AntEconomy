package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.ui.BalanceHud;
import uk.anttheantster.anteconomy.ui.BalanceHudService;

public class BalanceHudToggle extends AbstractPlayerCommand {
    private final BalanceController balanceController;
    private final BalanceHudService hudService;

    private RequiredArg<String> toggle;

    public BalanceHudToggle(BalanceController balanceController, BalanceHudService hudService) {
        super("hud", "Hud controls");
        this.balanceController = balanceController;
        this.hudService = hudService;

        this.toggle = this.withRequiredArg("On/Off", "Toggle Balance Hud on/off", ArgTypes.STRING);
    }

    @Override
    protected void execute(@NonNull CommandContext ctx, @NonNull Store<EntityStore> store,
                           @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {

        String state = ctx.get(toggle);
        if (!state.equalsIgnoreCase("on") && !state.equalsIgnoreCase("off")) {
            playerRef.sendMessage(Message.raw("Usage: /balance hud <on/off>"));
            return;
        }

        world.execute(() -> {
            if (!ref.isValid()) return;

            Player player = store.getComponent(ref, Player.getComponentType());
            if (player == null) return;

            if (state.equalsIgnoreCase("on")) {
                if (!hudService.isEnabled(playerRef.getUuid())) {
                    hudService.enable(player, playerRef, balanceController);
                    playerRef.sendMessage(Message.raw("Balance Hud Enabled."));
                }
            } else {
                if (hudService.isEnabled(playerRef.getUuid())) {
                    hudService.disable(player, playerRef);
                    playerRef.sendMessage(Message.raw("Balance Hud Disabled."));
                }
            }
        });
    }
}


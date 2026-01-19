package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class BalanceOtherVariant extends CommandBase {
    private BalanceController balanceController;

    private RequiredArg<UUID> target;

    public BalanceOtherVariant(BalanceController balanceController) {
        super("View another player's balance");
        requirePermission("antseconomy.balance.others");
        this.balanceController = balanceController;

        this.target = this.withRequiredArg("player", "Player name", ArgTypes.PLAYER_UUID);
    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {
        if (!(context.isPlayer())) {
            context.sendMessage(Message.raw("Only a player can do this!"));
            return;
        }

        Ref<EntityStore> senderRef = context.senderAsPlayerRef();
        if (senderRef == null || !senderRef.isValid()) {
            throw new IllegalStateException("Sender player reference is invalid!");
        }

        EntityStore entityStore = senderRef.getStore().getExternalData();
        World world = entityStore.getWorld();

        String targetName = balanceController.getName(context.get(target));

        world.execute(() -> {
            if (!senderRef.isValid()) return;

            Store<EntityStore> store = senderRef.getStore();

            PlayerRef playerRef = store.getComponent(senderRef, PlayerRef.getComponentType());
            if (playerRef == null) return;

            UUID tUUID = context.get(target);

            playerRef.sendMessage(Message.raw(targetName + "'s Balance: " + balanceController.getBalance(tUUID)));
        });
    }
}

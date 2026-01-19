package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public class BalanceCommand extends CommandBase {
    private BalanceController balanceController;

    private OptionalArg<String> target;

    public BalanceCommand(BalanceController balanceController) {
        super("balance", "Get player balance");
        addAliases("bal");
        requirePermission("antseconomy.balance");

        this.balanceController = balanceController;

        this.addUsageVariant(new BalanceOtherVariant(balanceController));

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

        world.execute(() -> {
            if (!senderRef.isValid()) return;

            Store<EntityStore> store = senderRef.getStore();

            PlayerRef playerRef = store.getComponent(senderRef, PlayerRef.getComponentType());
            if (playerRef == null) return;

            UUID pUuid = playerRef.getUuid();

            playerRef.sendMessage(Message.raw("Balance: " + balanceController.getBalance(pUuid)));
        });
    }
}
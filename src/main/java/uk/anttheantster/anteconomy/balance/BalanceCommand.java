package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public class BalanceCommand extends AbstractPlayerCommand {
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
    protected void execute(@NonNull CommandContext context, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {

        UUID pUuid = playerRef.getUuid();
        playerRef.sendMessage(Message.raw("Balance: " + balanceController.getBalance(pUuid)));
    }
}
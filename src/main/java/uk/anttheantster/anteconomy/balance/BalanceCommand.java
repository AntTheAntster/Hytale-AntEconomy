package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.ui.BalanceHudService;

import java.util.UUID;

public class BalanceCommand extends AbstractPlayerCommand {
    private BalanceController balanceController;
    private BalanceHudService hudService;

    public BalanceCommand(BalanceController balanceController, BalanceHudService hudService) {
        super("balance", "Get player balance");
        addAliases("bal");
        requirePermission("antseconomy.balance");

        this.balanceController = balanceController;
        this.hudService = hudService;

        this.addUsageVariant(new BalanceOtherVariant(balanceController));
        this.addSubCommand(new BalanceHudToggle(balanceController, hudService));

    }

    @Override
    protected void execute(@NonNull CommandContext context, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {

        UUID pUuid = playerRef.getUuid();
        playerRef.sendMessage(Message.raw("Balance: " + balanceController.getCurrencyPrefix() + balanceController.getBalance(pUuid)));
    }
}
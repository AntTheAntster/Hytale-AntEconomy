package uk.anttheantster.anteconomy.balance;

import com.hypixel.hytale.component.Ref;
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

import static uk.anttheantster.anteconomy.utils.FindOnlinePlayerHelper.findOnlinePlayerRef;

public class PayCommand extends CommandBase {
    private BalanceController balanceController;

    private RequiredArg<UUID> target;
    private RequiredArg<Integer> amount;


    public PayCommand(BalanceController balanceController) {
        super("pay", "Pay a specified player 'x' amount");
        this.requirePermission("anteconomy.pay");

        this.balanceController = balanceController;

        this.target = this.withRequiredArg("target", "Target name or UUID", ArgTypes.PLAYER_UUID);
        this.amount = this.withRequiredArg("amount", "Amount to pay player", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {

        Ref<EntityStore> senderRef = context.senderAsPlayerRef();
        EntityStore entityStore = senderRef.getStore().getExternalData();
        World world = entityStore.getWorld();

        world.execute(() -> {
            PlayerRef sender = senderRef.getStore()
                    .getComponent(senderRef, PlayerRef.getComponentType());

            UUID targetUuid = context.get(target);
            int amt = context.get(amount);

            PlayerRef target = findOnlinePlayerRef(targetUuid);

            balanceController.getExecutor().execute(() -> {
                balanceController.setBalance(sender.getUuid(),
                        balanceController.getBalance(sender.getUuid()) - amt);
                balanceController.setBalance(targetUuid,
                        balanceController.getBalance(targetUuid) + amt);
            });

            sender.sendMessage(Message.raw("You sent " + amt));

            if (target != null) {
                target.sendMessage(Message.raw(
                        "You received " + amt + " from " + sender.getUsername()
                ));
            }

        });
    }



}

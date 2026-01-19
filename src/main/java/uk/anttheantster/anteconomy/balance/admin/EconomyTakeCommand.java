package uk.anttheantster.anteconomy.balance.admin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.utils.SQLGetter;

import java.util.UUID;

public class EconomyTakeCommand extends CommandBase {
    private final BalanceController balances;

    private final SQLGetter data;

    private final RequiredArg<UUID> target;
    private final RequiredArg<Integer> amount;

    public EconomyTakeCommand(BalanceController balances, SQLGetter data) {
        super("take", "Take 'x' amount from a specified player");
        this.requirePermission("antseconomy.admin.take");

        this.balances = balances;
        this.data = data;

        this.target = this.withRequiredArg("target", "Target name or UUID", ArgTypes.PLAYER_UUID);
        this.amount = this.withRequiredArg("Amount", "Amount to give player", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {

        UUID tUUID = context.get(target);
        int amt = context.get(amount);

        if (amt <= 0) {
            context.sendMessage(Message.raw("Amount must be more than 0."));
        }

        int newBal = balances.getBalance(tUUID) - amt;
        balances.setBalance(tUUID, newBal);

        String tName = data.getPlayerName(tUUID);

        context.sendMessage(Message.raw("Taken " + amt + " to " + tName));
    }
}

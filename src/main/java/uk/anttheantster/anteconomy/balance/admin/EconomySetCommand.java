package uk.anttheantster.anteconomy.balance.admin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.balance.BalanceController;

import java.util.UUID;

public class EconomySetCommand extends CommandBase {
    private final BalanceController balances;

    private final RequiredArg<UUID> target;
    private final RequiredArg<Integer> amount;

    public EconomySetCommand(BalanceController balances) {
        super("set", "Set a specified players balance to 'x' amount");
        this.requirePermission("antseconomy.admin.set");

        this.balances = balances;

        this.target = this.withRequiredArg("target", "Target name or UUID", ArgTypes.PLAYER_UUID);
        this.amount = this.withRequiredArg("Amount", "Amount to set the player to", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {

        UUID tUUID = context.get(target);
        int amt = context.get(amount);

        int newBal = Math.toIntExact(amt);
        balances.setBalance(tUUID, newBal);

        String tName = balances.getName(tUUID);

        context.sendMessage(Message.raw("Set " + tName + "'s Balance to " + balances.getCurrencyPrefix() + amt));
    }
}

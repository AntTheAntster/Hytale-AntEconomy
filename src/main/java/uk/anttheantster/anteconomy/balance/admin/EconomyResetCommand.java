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

public class EconomyResetCommand extends CommandBase {
    private final BalanceController balances;
    private SQLGetter data;
    private final RequiredArg<UUID> target;

    public EconomyResetCommand(BalanceController balances, SQLGetter data) {
        super("reset", "Resets a players balance to default");
        requirePermission("antseconomy.admin.reset");

        this.balances = balances;
        this.data = data;

        this.target = this.withRequiredArg("player", "Player name", ArgTypes.PLAYER_UUID);

    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {
        UUID tUUID = context.get(target);

        balances.resetBalance(tUUID);

        String tName = data.getPlayerName(tUUID);

        context.sendMessage(Message.raw("Reset the balance of: " + tName));
    }
}

package uk.anttheantster.anteconomy.balance.admin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.utils.EconomyConfig;

import java.util.UUID;

public class EconomyResetCommand extends CommandBase {
    private final BalanceController balances;
    private final RequiredArg<UUID> target;

    private final EconomyConfig config;

    public EconomyResetCommand(BalanceController balances, EconomyConfig config) {
        super("reset", "Resets a players balance to default");
        requirePermission("antseconomy.admin.reset");

        this.balances = balances;
        this.config = config;

        this.target = this.withRequiredArg("player", "Player name", ArgTypes.PLAYER_UUID);

    }

    @Override
    protected void executeSync(@NonNull CommandContext context) {
        UUID tUUID = context.get(target);

        balances.setBalance(tUUID, config.getDefaultBalance());

        String tName = balances.getName(tUUID);

        context.sendMessage(Message.raw("Reset the balance of: " + tName));
    }
}

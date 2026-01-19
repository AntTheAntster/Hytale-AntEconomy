package uk.anttheantster.anteconomy.balance.admin;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.utils.EconomyConfig;

public class AdminCommand extends AbstractCommandCollection {


    public AdminCommand(BalanceController balances, EconomyConfig config) {
        super("economy", "Admin Economy Commands");
        this.addAliases("eco");
        this.requirePermission("antseconomy.admin");

        this.addSubCommand(new EconomyResetCommand(balances, config));
        this.addSubCommand(new EconomyGiveCommand(balances));
        this.addSubCommand(new EconomyTakeCommand(balances));
        this.addSubCommand(new EconomySetCommand(balances));
    }
}

package uk.anttheantster.anteconomy.balance.admin;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.utils.SQLGetter;

public class AdminCommand extends AbstractCommandCollection {


    public AdminCommand(BalanceController balances, SQLGetter data) {
        super("economy", "Admin Economy Commands");
        this.addAliases("eco");
        this.requirePermission("antseconomy.admin");

        this.addSubCommand(new EconomyResetCommand(balances, data));
        this.addSubCommand(new EconomyGiveCommand(balances, data));
        this.addSubCommand(new EconomyTakeCommand(balances, data));
    }
}

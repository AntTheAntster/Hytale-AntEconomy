package uk.anttheantster.anteconomy;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.balance.BalanceCommand;
import uk.anttheantster.anteconomy.balance.BalanceController;
import uk.anttheantster.anteconomy.balance.PayCommand;
import uk.anttheantster.anteconomy.balance.admin.AdminCommand;
import uk.anttheantster.anteconomy.listeners.PlayerJoin;
import uk.anttheantster.anteconomy.utils.EconomyConfig;
import uk.anttheantster.anteconomy.utils.MySQL;
import uk.anttheantster.anteconomy.utils.SQLGetter;

import java.sql.SQLException;

public class AntEconomy extends JavaPlugin {
    public final HytaleLogger logger = HytaleLogger.forEnclosingClass();

    private final Config<EconomyConfig> config;

    public MySQL mysql;
    public SQLGetter data;

    private BalanceController balanceController;


    public AntEconomy(@NonNull JavaPluginInit init) {
        super(init);
        logger.atInfo().log("Starting %s", this.getName());

        this.config = this.withConfig(EconomyConfig.CODEC);
    }

    @Override
    protected void setup() {
        EconomyConfig cfg = config.get();

        //Create config on first run
        config.save();
        setupSQL(cfg);

        balanceController = new BalanceController(this, data);

        registerCommandsAndListeners();
    }

    @Override
    protected void shutdown() {
        balanceController.shutdown();
        if (mysql != null) mysql.disconnect();
    }

    public EconomyConfig getConfig() {
        return config.get();
    }

    private void setupSQL(EconomyConfig cfg) {
        this.mysql = new MySQL(cfg);
        this.data = new SQLGetter(this);

        try {
            mysql.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (mysql.isConnected()){
            logger.atInfo().log("SQL Database Connected!");
            data.createTable();
        } else {
            logger.atSevere().log("SQL Database Not Connected!");
        }
    }

    private void registerCommandsAndListeners() {
        this.getCommandRegistry().registerCommand(new BalanceCommand(balanceController, data));
        this.getCommandRegistry().registerCommand(new AdminCommand(balanceController, data));
        this.getCommandRegistry().registerCommand(new PayCommand(balanceController, data));

        PlayerJoin pJoin = new PlayerJoin(this, balanceController, data, getConfig().getDefaultBalance());
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, pJoin::onJoin);
    }
}

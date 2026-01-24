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
import uk.anttheantster.anteconomy.utils.EconomyData;
import uk.anttheantster.anteconomy.utils.EconomyFileStore;

public class AntEconomy extends JavaPlugin {
    public final HytaleLogger logger = HytaleLogger.forEnclosingClass();

    private final Config<EconomyConfig> config;

    private final EconomyData data;
    private final EconomyFileStore ecoFS;

    private BalanceController balanceController;


    public AntEconomy(@NonNull JavaPluginInit init) {
        super(init);
        logger.atInfo().log("Starting %s", this.getName());

        this.config = this.withConfig(EconomyConfig.CODEC);

        this.data = new EconomyData();
        this.ecoFS = new EconomyFileStore(this.getDataDirectory());
    }

    @Override
    protected void setup() {
        EconomyConfig cfg = config.get();

        //Creates a config if it doesn't exist
        config.save();


        balanceController = new BalanceController(ecoFS, cfg.getDefaultBalance());

        registerCommandsAndListeners();
    }

    @Override
    protected void shutdown() {
        balanceController.shutdown();

    }

    public EconomyConfig getConfig() {
        return config.get();
    }



    private void registerCommandsAndListeners() {
        this.getCommandRegistry().registerCommand(new BalanceCommand(balanceController));
        this.getCommandRegistry().registerCommand(new AdminCommand(balanceController, getConfig()));
        this.getCommandRegistry().registerCommand(new PayCommand(balanceController));

        PlayerJoin pJoin = new PlayerJoin(balanceController);
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, pJoin::onJoin);
    }
}

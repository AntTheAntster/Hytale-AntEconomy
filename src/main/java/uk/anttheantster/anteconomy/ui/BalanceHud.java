package uk.anttheantster.anteconomy.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jspecify.annotations.NonNull;
import uk.anttheantster.anteconomy.balance.BalanceController;
import java.util.concurrent.*;

public class BalanceHud extends CustomUIHud {
    private final BalanceController balanceController;
    private final PlayerRef pRef;

    public BalanceHud(@NonNull PlayerRef playerRef, BalanceController balanceController) {
        super(playerRef);
        this.balanceController = balanceController;
        this.pRef = playerRef;
    }

    @Override
    protected void build(@NonNull UICommandBuilder uiBuilder) {
        uiBuilder.append("Hud/BalanceHud.ui");
        uiBuilder.set("#Balance.Text", "Balance: " + balanceController.getCurrencyPrefix() + balanceController.getBalance(pRef.getUuid()));
    }

    public void setBalanceText() {
        UICommandBuilder b = new UICommandBuilder();
        b.set("#Balance.Text", "Balance: " + balanceController.getCurrencyPrefix() + balanceController.getBalance(pRef.getUuid()));
        update(false, b);
    }

    public void refreshFromController() {
        setBalanceText();
    }

    public static void toggleHud(Player player, PlayerRef playerRef, BalanceController controller, String state) {


    }
}
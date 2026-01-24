package uk.anttheantster.anteconomy.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public final class ClearCustomHud extends CustomUIHud {
    public ClearCustomHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder uiBuilder) {
        // Intentionally empty.
        // show() will call update(true, builder) → sends CustomHud(clear=true, commands=empty array)
    }
}

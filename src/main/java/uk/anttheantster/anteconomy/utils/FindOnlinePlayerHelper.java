package uk.anttheantster.anteconomy.utils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.UUID;

public class FindOnlinePlayerHelper {

    @Nullable
    public static PlayerRef findOnlinePlayerRef(UUID uuid) {
        for (World world : Universe.get().getWorlds().values()) {
            Ref<EntityStore> ref = world.getEntityStore().getRefFromUUID(uuid);
            if (ref != null && ref.isValid()) {
                return ref.getStore().getComponent(ref, PlayerRef.getComponentType());
            }
        }
        return null;
    }

}

package io.github.jamalam360.jamfabric.registry;

import io.github.jamalam360.jamfabric.compat.CompatibilityPlugin;
import io.github.jamalam360.jamfabric.compat.sandwichable.SandwichableCompat;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class CompatRegistry {
    public static final Map<String, CompatibilityPlugin> COMPATIBILITY_PLUGIN_MAP = new HashMap<>();

    static {
        COMPATIBILITY_PLUGIN_MAP.put("sandwichable", new SandwichableCompat());
    }

    public static void init() {
        for (String modId : COMPATIBILITY_PLUGIN_MAP.keySet()) {
            if (FabricLoader.getInstance().isModLoaded(modId)) {
                COMPATIBILITY_PLUGIN_MAP.get(modId).init();
                COMPATIBILITY_PLUGIN_MAP.get(modId).initMixins();
            }
        }
    }
}

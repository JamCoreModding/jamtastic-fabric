package io.github.jamalam360.jamfabric.util;

import io.github.jamalam360.jamfabric.config.JamFabricConfig;
import me.shedaniel.autoconfig.AutoConfig;

/**
 * @author Jamalam360
 */
public class Utils {
    public static JamFabricConfig getConfig() {
        return AutoConfig.getConfigHolder(JamFabricConfig.class).getConfig();
    }
}

package io.github.jamalam360.jamfabric.config;

import io.github.jamalam360.jamfabric.JamModInit;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

/**
 * @author Jamalam360
 */

@Config(name = JamModInit.MOD_ID)
public class JamFabricConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public JamOptions jamOptions = new JamOptions();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public CompatOptions compatOptions = new CompatOptions();

    public static class JamOptions {
        @ConfigEntry.Gui.Tooltip
        public int maxJamIngredients = 4;
    }

    public static class CompatOptions {
        public boolean enableSandwichableCompat = true;
    }
}

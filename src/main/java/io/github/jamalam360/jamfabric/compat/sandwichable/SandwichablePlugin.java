package io.github.jamalam360.jamfabric.compat.sandwichable;

import io.github.foundationgames.sandwichable.util.SpreadRegistry;
import io.github.jamalam360.jamfabric.compat.CompatibilityPlugin;
import io.github.jamalam360.jamfabric.util.Utils;

/**
 * @author Jamalam360
 */
public class SandwichablePlugin implements CompatibilityPlugin {
    @Override
    public void init() {
        if (Utils.getConfig().compatOptions.enableSandwichableCompat) {
            SpreadRegistry.INSTANCE.register("jamtastic:jam", new JamSpreadType());
        }
    }

    @Override
    public String getModId() {
        return "sandwichable";
    }
}

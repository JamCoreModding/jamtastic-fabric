package io.github.jamalam360.jamfabric.config;

import io.github.jamalam360.jamfabric.util.color.AverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.algorithm.HsbAverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.algorithm.SimpleAverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.algorithm.SimpleSquaredAverageColorProvider;

/**
 * @author Jamalam360
 */
public enum AverageColorProviderType {
    SIMPLE_AVERAGE(new SimpleAverageColorProvider()),
    SIMPLE_SQUARED_AVERAGE(new SimpleSquaredAverageColorProvider()),
    HSB_AVERAGE(new HsbAverageColorProvider());

    AverageColorProviderType(AverageColorProvider provider) {
        this.provider = provider;
    }

    private final AverageColorProvider provider;

    public AverageColorProvider getProvider() {
        return provider;
    }
}

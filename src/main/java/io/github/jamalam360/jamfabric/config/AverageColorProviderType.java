package io.github.jamalam360.jamfabric.config;

import io.github.jamalam360.jamfabric.util.color.AverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.algorithm.SimpleAverageColorProvider;

/**
 * @author Jamalam360
 */
public enum AverageColorProviderType {
    SIMPLE_AVERAGE(new SimpleAverageColorProvider());

    AverageColorProviderType(AverageColorProvider provider) {
        this.provider = provider;
    }

    private final AverageColorProvider provider;

    public AverageColorProvider getProvider() {
        return provider;
    }
}

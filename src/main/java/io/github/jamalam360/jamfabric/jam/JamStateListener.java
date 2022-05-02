package io.github.jamalam360.jamfabric.jam;

/**
 * @author Jamalam360
 */
public interface JamStateListener {
    default void onCleared() {}
    default void onUpdated() {}
}

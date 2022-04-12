package io.github.jamalam360.jamfabric.util.registry;

import io.github.jamalam360.jamfabric.block.JamPotBlock;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.data.JamIngredient;
import io.github.jamalam360.jamfabric.data.JamIngredientRegistry;
import io.github.jamalam360.jamfabric.data.JamIngredientsResourceReloadListener;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.ActionResult;

/**
 * @author Jamalam360
 */
public class DataRegistry {
    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new JamIngredientsResourceReloadListener());
    }
}

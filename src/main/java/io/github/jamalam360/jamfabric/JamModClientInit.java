package io.github.jamalam360.jamfabric;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntityRenderer;
import io.github.jamalam360.jamfabric.item.JamJarItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

/**
 * @author Jamalam360
 */
public class JamModClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //region Block Entity Renderers
        BlockEntityRendererRegistry.INSTANCE.register(JamModInit.JAM_POT_BLOCK_ENTITY, JamPotBlockEntityRenderer::new);
        //endregion

        //region Block Render Layers
        BlockRenderLayerMap.INSTANCE.putBlock(JamModInit.JAM_POT_BLOCK, RenderLayer.getTranslucent());
        //endregion

        //region Color Providers
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return JamJarItem.getColour(stack).getRGB();
            } else {
                return 0xffffff;
            }
        }), JamModInit.JAM_JAR);
        //endregion

        //region Model Predicate Providers
        FabricModelPredicateProviderRegistry.register(JamModInit.JAM_JAR, new Identifier("jam_jar_full"), ((stack, world, entity, seed) -> NbtHelper.readItems(stack.getOrCreateNbt(), "Ingredients").length != 0 ? 1.0f : 0.0f));
        //endregion
    }
}

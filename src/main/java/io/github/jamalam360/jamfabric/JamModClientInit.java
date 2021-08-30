package io.github.jamalam360.jamfabric;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntityRenderer;
import io.github.jamalam360.jamfabric.registry.BlockRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;

/**
 * @author Jamalam360
 */
@SuppressWarnings("unchecked")
public class JamModClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register((BlockEntityType<JamPotBlockEntity>) BlockRegistry.JAM_POT.getBlockEntityType(), JamPotBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.JAM_POT.getBlock(), RenderLayer.getCutout());
    }
}

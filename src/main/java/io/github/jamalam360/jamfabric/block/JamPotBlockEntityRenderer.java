package io.github.jamalam360.jamfabric.block;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntityRenderer implements BlockEntityRenderer<JamPotBlockEntity> {
    @SuppressWarnings("unused")
    public JamPotBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(JamPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
        FluidState state = Fluids.WATER.getDefaultState();
        float size = 12f / 16f;

        matrices.scale(size, size, size);

        renderManager.renderBlockAsEntity(Blocks.WATER.getDefaultState(), matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }
}

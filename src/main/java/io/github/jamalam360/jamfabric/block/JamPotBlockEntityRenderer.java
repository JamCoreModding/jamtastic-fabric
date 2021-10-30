package io.github.jamalam360.jamfabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntityRenderer implements BlockEntityRenderer<JamPotBlockEntity> {
    @SuppressWarnings("unused")
    public JamPotBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(JamPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int updatedLight = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        float size = this.blockBenchToBlock(13);

        float x = this.blockBenchToBlock(2);
        float y = this.blockBenchToBlock(2);
        float z = this.blockBenchToBlock(2);

        if (entity.getItems().length > 0) {
            matrices.push();

            BlockState state = Blocks.RED_WOOL.getDefaultState();

            matrices.scale(size, (float) entity.getItems().length / JamPotBlockEntity.CAPACITY, size);
            matrices.translate(x, y, z);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(state, matrices, vertexConsumers, updatedLight, overlay);

            matrices.pop();
        } else if (entity.hasWater) {
            matrices.push();

            matrices.scale(size, (float) entity.getItems().length / JamPotBlockEntity.CAPACITY, size);
            matrices.translate(x, y, z);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Fluids.WATER.getStill().getDefaultState().getBlockState(), matrices, vertexConsumers, updatedLight, overlay);

            matrices.pop();
        }
    }

    private float blockBenchToBlock(int blockBenchCoordinates) {
        return (float) blockBenchCoordinates / 16f;
    }
}

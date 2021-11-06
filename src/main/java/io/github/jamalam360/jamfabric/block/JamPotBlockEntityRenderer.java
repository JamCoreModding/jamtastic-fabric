/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.util.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntityRenderer implements BlockEntityRenderer<JamPotBlockEntity> {
    private static final Color WATER = new Color(49, 95, 219);
    public static final BlockState JAM = Blocks.WHITE_WOOL.getDefaultState();
    private static final BakedModel JAM_BAKED_MODEL = MinecraftClient.getInstance().getBlockRenderManager().getModel(JAM);

    @SuppressWarnings("unused")
    public JamPotBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(JamPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //region Shut Up IntelliJ
        assert entity.getWorld() != null;
        assert GameRenderer.getRenderTypeEntityCutoutShader() != null;
        assert GameRenderer.getRenderTypeEntityCutoutShader().colorModulator != null;
        //endregion

        int updatedLight = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        Color colour = entity.cachedColor;

        if (entity.getItems().length == 0 && entity.hasWater()) { // No items, but water, should just render the water
            colour = WATER;
        } else if (entity.getItems().length == 0) { // No items, no water, shouldn't render anything
            return;
        }

        //region Actually Rendering
        matrices.push();
        matrices.scale(0.82f, 0.82f, 0.82f); //0.8125f
        matrices.translate(0.0625f, 0.0625f, 0.0625f);

        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout()), JAM, JAM_BAKED_MODEL, colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, updatedLight, overlay);

        matrices.pop();
        //endregion
    }
}

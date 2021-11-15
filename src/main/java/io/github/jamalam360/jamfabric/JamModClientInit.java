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

package io.github.jamalam360.jamfabric;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntityRenderer;
import io.github.jamalam360.jamfabric.util.registry.BlockRegistry;
import io.github.jamalam360.jamfabric.util.registry.ItemRegistry;
import io.github.jamalam360.jamfabric.util.Jam;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
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
        BlockEntityRendererRegistry.register(BlockRegistry.JAM_POT_ENTITY, JamPotBlockEntityRenderer::new);
        //endregion

        //region Block Render Layers
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.JAM_POT, RenderLayer.getTranslucent());
        //endregion

        //region Color Providers
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return Jam.fromNbt(stack.getSubNbt("Jam")).getColor().getRGB();
            } else {
                return 0xffffff;
            }
        }), ItemRegistry.JAM_JAR);
        //endregion

        //region Model Predicate Providers
        FabricModelPredicateProviderRegistry.register(ItemRegistry.JAM_JAR, new Identifier("jam_jar_full"), ((stack, world, entity, seed) -> Jam.fromNbt(stack.getSubNbt("Jam")).ingredientsSize() != 0 ? 1.0f : 0.0f));
        //endregion
    }
}

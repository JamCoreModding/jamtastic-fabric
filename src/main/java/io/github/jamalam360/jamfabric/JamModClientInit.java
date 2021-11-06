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

import io.github.foundationgames.mealapi.api.v0.PlayerFullnessUtil;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntityRenderer;
import io.github.jamalam360.jamfabric.util.Color;
import io.github.jamalam360.jamfabric.util.JamColor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam360
 */
public class JamModClientInit implements ClientModInitializer {
    public static KeyBinding keybinding;

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
                return JamColor.getAverageItemColor(JamNbtHelper.readItems(stack.getOrCreateNbt(), "Ingredients")).getRGB();
            } else {
                return 0xffffff;
            }
        }), JamModInit.JAM_JAR);
        //endregion

        //region Model Predicate Providers
        FabricModelPredicateProviderRegistry.register(JamModInit.JAM_JAR, new Identifier("jam_jar_full"), ((stack, world, entity, seed) -> JamNbtHelper.readItems(stack.getOrCreateNbt(), "Ingredients").length != 0 ? 1.0f : 0.0f));
        //endregion

        keybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("jamfabric.test", GLFW.GLFW_KEY_J, "jamfabric.test"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybinding.wasPressed()) {
                System.out.println(PlayerFullnessUtil.instance().getClientFullness());
            }
        });
    }
}

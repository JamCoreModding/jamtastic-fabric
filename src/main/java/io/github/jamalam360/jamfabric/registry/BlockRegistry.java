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

package io.github.jamalam360.jamfabric.registry;

import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.block.JamPotBlock;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.util.Utils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

/**
 * @author Jamalam360
 */
public class BlockRegistry {
    public static final Block JAM_RENDER = new Block(FabricBlockSettings.of(Material.AIR));
    public static final Block JAM_POT = new JamPotBlock();
    public static final BlockEntityType<JamPotBlockEntity> JAM_POT_ENTITY = FabricBlockEntityTypeBuilder.create(JamPotBlockEntity::new, JAM_POT).build();

    public static void init() {
        registerBlock("jam_render", JAM_RENDER, null);
        registerBlock("jam_pot", JAM_POT, JamModInit.ITEM_GROUP);
        registerBlockEntity("jam_pot", JAM_POT_ENTITY);
    }

    private static void registerBlock(String id, Block block, ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, Utils.id(id), block);

        if (itemGroup != null) {
            Registry.register(Registry.ITEM, Utils.id(id), new BlockItem(block, new FabricItemSettings().group(itemGroup)));
        }
    }

    private static void registerBlockEntity(String id, BlockEntityType<?> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.id(id), blockEntityType);
    }
}

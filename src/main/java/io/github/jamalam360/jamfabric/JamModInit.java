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

import io.github.jamalam360.jamfabric.block.JamPotBlock;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.item.JamJarItem;
import io.github.jamalam360.jamfabric.registry.CompatRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JamModInit implements ModInitializer {
    public static final String MOD_ID = "jamfabric";
    public static final String MOD_NAME = "Jam";
    public static Logger LOGGER = LogManager.getLogger(MOD_NAME);

    //region Blocks
    public static final Block JAM_POT_BLOCK = new JamPotBlock();
    public static final BlockEntityType<JamPotBlockEntity> JAM_POT_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(JamPotBlockEntity::new, JAM_POT_BLOCK).build();
    //endregion

    //region Items
    public static final Item JAM_JAR = new JamJarItem(new FabricItemSettings().maxCount(1).group(ItemGroup.FOOD).food(new FoodComponent.Builder().alwaysEdible().build()));
    //endregion

    //region Registry Methods
    public static void registerBlock(String id, Block block, ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, id), block);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), new BlockItem(block, new FabricItemSettings().group(itemGroup)));
    }

    public static void registerBlockEntity(String id, BlockEntityType<?> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, id), blockEntityType);
    }

    public static void registerItem(String id, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), item);
    }
    //endregion

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing '" + MOD_NAME + "' under the ID '" + MOD_ID + "'");

        registerBlock("jam_pot", JAM_POT_BLOCK, ItemGroup.DECORATIONS);
        registerBlockEntity("jam_pot", JAM_POT_BLOCK_ENTITY);

        registerItem("jam_jar", JAM_JAR);

        CompatRegistry.init();
    }
}
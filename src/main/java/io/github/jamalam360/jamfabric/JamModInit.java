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

import io.github.jamalam360.jamfabric.config.JamFabricConfig;
import io.github.jamalam360.jamfabric.registry.*;
import io.github.jamalam360.jamfabric.util.Utils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JamModInit implements ModInitializer {
    public static final String MOD_ID = "jamfabric";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            Utils.id("general_group"),
            () -> new ItemStack(ItemRegistry.JAM_JAR)
    );
    private static final Logger LOGGER = LogManager.getLogger("Jamtastic/Initializer");

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Jamtastic.");

        AutoConfig.register(JamFabricConfig.class, GsonConfigSerializer::new);

        ItemRegistry.init();
        BlockRegistry.init();
        CompatRegistry.init();
        DataRegistry.init();
        NetworkingRegistry.init(false);

        LOGGER.log(Level.INFO, "Jamtastic initialized.");
    }
}

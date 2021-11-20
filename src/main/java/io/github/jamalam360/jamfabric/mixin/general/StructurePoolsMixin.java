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

package io.github.jamalam360.jamfabric.mixin.general;

import com.mojang.datafixers.util.Pair;
import io.github.jamalam360.jamfabric.mixin.accessor.StructurePoolAccessor;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Credit: FoundationGames
 *
 * @author Jamalam360
 */

@Mixin(StructurePools.class)
public class StructurePoolsMixin {
    //FIXME: THIS IS SO BROKEN JESUS CHRIST
    @Inject(method = "register", at = @At("HEAD"))
    private static void jamfabric$registerVillagerHouses(StructurePool pool, CallbackInfoReturnable<StructurePool> cir) {
        jamfabric$tryAddElementToPool(new Identifier("village/desert/houses"), pool, "jamfabric:desert_jam_house");
        jamfabric$tryAddElementToPool(new Identifier("village/savanna/houses"), pool, "jamfabric:savanna_jam_house");
        jamfabric$tryAddElementToPool(new Identifier("village/snowy/houses"), pool, "jamfabric:snowy_jam_house");
    }

    private static void jamfabric$tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId) {
        if (targetPool.equals(pool.getId())) {
            StructurePoolElement element = StructurePoolElement.ofProcessedLegacySingle(elementId, StructureProcessorLists.EMPTY).apply(StructurePool.Projection.RIGID);
            for (int i = 0; i < 2; i++) {
                ((StructurePoolAccessor) pool).getElements().add(element);
            }
            ((StructurePoolAccessor) pool).getElementCounts().add(Pair.of(element, 2));
        }
    }
}

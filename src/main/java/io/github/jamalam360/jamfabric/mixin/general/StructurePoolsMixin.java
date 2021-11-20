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

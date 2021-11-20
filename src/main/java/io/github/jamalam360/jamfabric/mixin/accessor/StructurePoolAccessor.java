package io.github.jamalam360.jamfabric.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Jamalam360
 */

@Mixin(StructurePool.class)
public interface StructurePoolAccessor {
    @Accessor
    List<StructurePoolElement> getElements();

    @Accessor
    List<Pair<StructurePoolElement, Integer>> getElementCounts();
}

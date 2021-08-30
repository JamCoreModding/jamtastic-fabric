package io.github.jamalam360.jamfabric.registry;

import io.github.jamalam360.jamfabric.block.JamPotBlock;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.libjam.registry.entry.BlockEntry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;

/**
 * @author Jamalam360
 */

@SuppressWarnings("unused")
public class BlockRegistry {
    public static final BlockEntry JAM_POT = new BlockEntry("jam_pot", new JamPotBlock(), JamPotBlockEntity::new, new FabricItemSettings().group(ItemGroup.DECORATIONS));
}

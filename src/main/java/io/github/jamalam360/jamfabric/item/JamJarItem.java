package io.github.jamalam360.jamfabric.item;

import io.github.jamalam360.jamfabric.JamColorUtil;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.NbtHelper;
import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.util.Color;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam360
 */
public class JamJarItem extends Item {
    public JamJarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        if (world.getBlockState(pos).isOf(JamModInit.JAM_POT_BLOCK)) {
            JamPotBlockEntity blockEntity = (JamPotBlockEntity) world.getBlockEntity(pos);
            NbtHelper.writeItems(stack.getOrCreateNbt(), "Ingredients", blockEntity.getItems());
            blockEntity.clearItems();
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }

    public static Color getColour(ItemStack stack) {
        List<Color> averageColours = new ArrayList<>();

        for (Item item : NbtHelper.readItems(stack.getOrCreateNbt(), "Ingredients")) {
            averageColours.add(JamColorUtil.getAverageColour(item));
        }

        return JamColorUtil.averageColours(averageColours.toArray(new Color[0]));
    }
}

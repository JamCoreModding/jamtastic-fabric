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
import io.github.foundationgames.mealapi.api.v0.PlayerFullnessUtil;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.JamNbtHelper;
import io.github.jamalam360.jamfabric.util.HungerManagerDuck;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Jamalam360
 */

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin implements HungerManagerDuck {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    private PlayerEntity jamfabric$parent;

    @Inject(
            method = "eat",
            at = @At("HEAD"),
            cancellable = true
    )
    public void jamfabric$eatMixin(Item item, ItemStack stack, CallbackInfo ci) {
        if (stack.isOf(JamModInit.JAM_JAR)) {
            Pair<Integer, Float> pair = JamNbtHelper.getJamJarHungerAndSaturation(stack.getOrCreateNbt());

            if (!this.jamfabric$parent.world.isClient && pair.getFirst() > 0) {
                PlayerFullnessUtil.instance().addFullness((ServerPlayerEntity) this.jamfabric$parent, pair.getFirst());
            }

            ci.cancel();
        }
    }

    @Override
    public void jamfabric$setPlayer(PlayerEntity player) {
        this.jamfabric$parent = player;
    }
}

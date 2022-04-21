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

package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.util.Jam;
import io.github.jamalam360.jamfabric.util.Utils;
import io.github.jamalam360.jamfabric.util.color.Color;
import io.github.jamalam360.jamfabric.util.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntity extends BlockEntity {
    public static final int CAPACITY = Utils.getConfig().jamOptions.maxJamIngredients;

    public Jam jam = new Jam(this::update);
    public Color cachedColor = new Color(255, 255, 255);
    public Color lastColorBeforeChange = new Color(255, 255, 255);
    private boolean hasWater = false;
    private boolean hasSugar = false;

    public Color lerpingTo;
    public Color lastLerpProgress;
    public boolean appliedClearListener = false;
    public boolean justCleared = false;

    public JamPotBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.JAM_POT_ENTITY, pos, state);
    }

    public boolean canInsertWater() {
        return !hasWater;
    }

    public boolean canInsertSugar() {
        return !hasSugar && hasWater;
    }

    public boolean canInsertIngredients() {
        return hasSugar && hasWater && jam.ingredientsSize() < CAPACITY;
    }

    public void setFilledWater(boolean filled) {
        this.hasWater = filled;

        if (filled) {
            this.update();
        }

        this.markDirty();
    }

    public void setFilledSugar(boolean filled) {
        this.hasSugar = filled;
        this.markDirty();
    }

    public boolean hasWater() {
        return this.hasWater;
    }

    public void empty() {
        this.jam.clear();
        this.setFilledWater(false);
        this.setFilledSugar(false);
        this.markDirty();
    }

    public void update() {
        if (this.jam == null) return;

        if (this.jam.ingredientsSize() > 0) {
            if (this.world.isClient) {
                if (this.cachedColor.equals(this.jam.getColor())) return;
                this.lastColorBeforeChange = cachedColor;
                this.cachedColor = this.jam.getColor();
            }
        } else {
            this.lastColorBeforeChange = Utils.WATER_COLOR;
            this.cachedColor = Utils.WATER_COLOR;
        }

        if (!this.world.isClient) {
            this.world.getPlayers().forEach(player -> ((ServerPlayerEntity) player).networkHandler.sendPacket(this.toUpdatePacket()));
        }

        this.markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.jam = Jam.fromNbt((NbtCompound) nbt.get("Jam"));
        this.hasWater = nbt.getBoolean("ContainsWater");
        this.hasSugar = nbt.getBoolean("ContainsSugar");
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("Jam", this.jam.toNbt());
        nbt.putBoolean("ContainsWater", this.hasWater);
        nbt.putBoolean("ContainsSugar", this.hasSugar);
        super.writeNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.of(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        nbt.put("Jam", this.jam.toNbt());
        nbt.putBoolean("ContainsWater", this.hasWater);
        nbt.putBoolean("ContainsSugar", this.hasSugar);
        super.writeNbt(nbt);
        this.markDirty();
        return nbt;
    }
}

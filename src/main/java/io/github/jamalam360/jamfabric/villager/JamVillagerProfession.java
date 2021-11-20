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

package io.github.jamalam360.jamfabric.villager;

import com.google.common.collect.ImmutableMap;
import io.github.jamalam360.jamfabric.util.Jam;
import io.github.jamalam360.jamfabric.util.Utils;
import io.github.jamalam360.jamfabric.util.registry.BlockRegistry;
import io.github.jamalam360.jamfabric.util.registry.ItemRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Random;

/**
 * @author Jamalam360
 */
public class JamVillagerProfession {
    public static final PointOfInterestType JAM_POI = PointOfInterestHelper.register(
            Utils.id("jam_poi"),
            1,
            16,
            BlockRegistry.JAM_POT
    );

    public static final VillagerProfession JAM_VILLAGER_PROFESSION = VillagerProfessionBuilder
            .create()
            .id(Utils.id("jam_villager_profession"))
            .workstation(JAM_POI)
            .workSound(SoundEvents.BLOCK_BREWING_STAND_BREW)
            .build();

    public static void init() {
        Registry.register(Registry.VILLAGER_PROFESSION, Utils.id("jam_villager_profession"), JAM_VILLAGER_PROFESSION);

        ItemStack jamJar1 = new ItemStack(ItemRegistry.JAM_JAR);
        Jam jam1 = new Jam(Items.GLOW_BERRIES, Items.SWEET_BERRIES);
        jamJar1.setSubNbt("Jam", jam1.toNbt());

        ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        NbtList nbtList = new NbtList();
        nbtList.add(0, NbtString.of("get scammed lmao"));
        book.setSubNbt("pages", nbtList);
        book.setSubNbt("author", NbtString.of("The Jam Master"));
        book.setSubNbt("title", NbtString.of("The Wonders of Modern Jam"));

        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(
                JAM_VILLAGER_PROFESSION,
                new Int2ObjectOpenHashMap<>(
                        ImmutableMap.of(
                                1, new TradeOffers.Factory[]{
                                        new BuyForOneEmeraldFactory(Items.GLASS_BOTTLE, 3, 15, 2),
                                        new BuyForOneEmeraldFactory(Items.SUGAR, 15, 3, 2),
                                        new SellItemFactory(jamJar1, 3, 1, 3, 3)
                                },
                                2, new TradeOffers.Factory[]{
                                        new BuyForOneEmeraldFactory(Items.WATER_BUCKET, 2, 5, 2),
                                        new BuyForOneEmeraldFactory(ItemRegistry.JAM_JAR, 1, 5, 2),
                                        new SellItemFactory(book, 12, 1, 3, 8),
                                        new SellItemFactory(BlockRegistry.JAM_POT.asItem(), 6, 1, 2)
                                },
                                3, new TradeOffers.Factory[]{
                                        new BuyForOneEmeraldFactory(Items.WATER_BUCKET, 3, 4, 2),
                                        new BuyForOneEmeraldFactory(ItemRegistry.JAM_JAR, 3, 4, 2),
                                        new SellItemFactory(BlockRegistry.JAM_POT.asItem(), 3, 1, 2)
                                }
                        )
                )
        );
    }


    static class BuyForOneEmeraldFactory implements TradeOffers.Factory {
        private final Item buy;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BuyForOneEmeraldFactory(ItemConvertible item, int price, int maxUses, int experience) {
            this.buy = item.asItem();
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(this.buy, this.price);
            return new TradeOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.experience, this.multiplier);
        }
    }

    private static class SellItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final int count;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellItemFactory(Item item, int price, int count, int experience) {
            this(new ItemStack(item), price, count, 12, experience);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience) {
            this(stack, price, count, maxUses, experience, 0.05F);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience, float multiplier) {
            this.sell = stack;
            this.price = price;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier);
        }
    }
}

package io.github.jamalam360.jamfabric.util;

import com.mojang.datafixers.util.Pair;
import io.github.jamalam360.jamfabric.util.helper.ColorHelper;
import io.github.jamalam360.jamfabric.util.helper.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.*;

/**
 * @author Jamalam360
 */
public class Jam {
    public static final String INGREDIENTS_BASE_KEY = "Ingredients";

    private final List<Item> ingredients = new ArrayList<>();
    private final List<Pair<StatusEffectInstance, Float>> effects = new ArrayList<>();

    private int hunger;
    private float saturation;

    public Jam(Item... ingredients) {
        this.ingredients.addAll(Arrays.asList(ingredients));
        this.recalculate();
    }

    public void add(Item item) {
        ingredients.add(item);
        this.recalculate();
    }

    public void addAll(Collection<Item> items) {
        ingredients.addAll(items);
        this.recalculate();
    }

    public Item removeLast() {
        Item item = ingredients.remove(ingredients.size() - 1);
        this.recalculate();
        return item;
    }

    public void clear() {
        ingredients.clear();
        this.recalculate();
    }

    public int ingredientsSize() {
        return ingredients.size();
    }

    public List<Item> ingredients() {
        return ingredients;
    }

    public Color getColor() {
        return ColorHelper.getAverageItemColor(ingredients);
    }

    public List<Text> getTooltipContent() {
        Map<Item, Integer> tooltipMap = new HashMap<>();
        List<Text> finalTooltip = new ArrayList<>();

        for (Item item : this.ingredients) {
            if (tooltipMap.containsKey(item)) {
                tooltipMap.put(item, tooltipMap.get(item) + 1);
            } else {
                tooltipMap.put(item, 1);
            }
        }

        if (tooltipMap.isEmpty()) {
            finalTooltip.add(new TranslatableText("tooltip.jamfabric.empty_tooltip").styled(s -> s.withColor(Formatting.AQUA)));
        } else {
            finalTooltip.add(new TranslatableText("tooltip.jamfabric.hunger_tooltip", this.hunger).styled(s -> s.withColor(Formatting.AQUA)));
            finalTooltip.add(new LiteralText(""));
            tooltipMap.forEach((key, value) -> finalTooltip.add(new TranslatableText(key.getTranslationKey()).append(" x" + value).styled((style -> style.withColor(Formatting.GOLD)))));
        }

        return finalTooltip;
    }

    public int hunger() {
        return hunger;
    }

    public List<Pair<StatusEffectInstance, Float>> effectsRaw() {
        return effects;
    }

    public List<StatusEffectInstance> effects() {
        List<StatusEffectInstance> finalEffects = new ArrayList<>();
        this.effects.forEach((pair) -> finalEffects.add(pair.getFirst()));

        return finalEffects;
    }

    public float saturation() {
        return saturation;
    }

    private void recalculate() {
        this.hunger = 0;
        this.saturation = 0;
        this.effects.clear();

        for (Item item : ingredients) {
            this.hunger += item.getFoodComponent().getHunger();
            this.saturation += item.getFoodComponent().getSaturationModifier();
            this.effects.addAll(item.getFoodComponent().getStatusEffects());
        }
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        NbtHelper.writeItems(compound, INGREDIENTS_BASE_KEY, ingredients.toArray(new Item[0]));
        return compound;
    }

    public static Jam fromNbt(NbtCompound compound) {
        if (compound != null) {
            return new Jam(NbtHelper.readItems(compound, INGREDIENTS_BASE_KEY));
        } else {
            return new Jam(new ArrayList<Item>().toArray(new Item[0]));
        }
    }
}

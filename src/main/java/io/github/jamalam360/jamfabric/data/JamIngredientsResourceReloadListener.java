package io.github.jamalam360.jamfabric.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.jamalam360.jamfabric.util.Utils;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

/**
 * @author Jamalam360
 */
public class JamIngredientsResourceReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public JamIngredientsResourceReloadListener() {
        super(new Gson(), "jam_ingredients");
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((id, element) -> {
            for (var entry : element.getAsJsonObject().get("ingredients").getAsJsonArray()) {
                JamIngredientRegistry.register(JamIngredient.ofJson(entry.getAsJsonObject()));
            }
        });
    }

    @Override
    public Identifier getFabricId() {
        return Utils.id("ingredients");
    }
}

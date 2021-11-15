package io.github.jamalam360.jamfabric.util.helper;

import io.github.jamalam360.jamfabric.JamModInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.IOException;

/**
 * @author Jamalam360
 */
public class NativeImageHelper {
    /**
     * Returns a NativeImage of an Items texture
     */
    public static NativeImage getNativeImage(Item item) {
        try {
            Identifier id = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(item).getParticleSprite().getId();
            Resource texture = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(id.getNamespace(), "textures/" + id.getPath() + ".png"));
            return NativeImage.read(texture.getInputStream());
        } catch (IOException e) {
            JamModInit.LOGGER.log(Level.ERROR, "Caught an error while retrieving native image");
            e.printStackTrace();
        }

        return new NativeImage(16, 16, false);
    }
}

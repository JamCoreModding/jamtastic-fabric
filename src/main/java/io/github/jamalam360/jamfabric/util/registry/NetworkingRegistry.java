package io.github.jamalam360.jamfabric.util.registry;

import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.util.JamNameGenerator;
import io.github.jamalam360.jamfabric.util.Utils;
import io.github.jamalam360.jamfabric.util.helper.TranslationHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * @author Jamalam360
 */
public class NetworkingRegistry {
    public static final Identifier GET_TRANSLATION_KEYS_REQ = Utils.id("get_translation_count_req");
    private static final Identifier GET_TRANSLATION_KEYS_RES = Utils.id("get_translation_count_res");

    public static void init(boolean isClient) {
        if (isClient) {
            ClientPlayNetworking.registerGlobalReceiver(GET_TRANSLATION_KEYS_REQ, (client, handler, reqBuf, responseSender) -> {
                PacketByteBuf resBuf = PacketByteBufs.create();
                String baseKey = reqBuf.readString();
                resBuf.writeString(baseKey);
                resBuf.writeString(String.join(",", TranslationHelper.getTranslations(baseKey)));
                responseSender.sendPacket(GET_TRANSLATION_KEYS_RES, resBuf);
            });
        } else {
            ServerPlayNetworking.registerGlobalReceiver(GET_TRANSLATION_KEYS_RES, (server, player, handler, reqBuf, responseSender) -> {
                String baseKey = reqBuf.readString();
                String translations = reqBuf.readString();
                String[] translationsArray = translations.split(",");

                switch (baseKey) {
                    case JamNameGenerator.BENEFICIAL_EFFECT_ADJECTIVES_KEY -> JamNameGenerator.BENEFICIAL_EFFECT_ADJECTIVES = translationsArray;
                    case JamNameGenerator.NON_BENEFICIAL_EFFECT_ADJECTIVES_KEY -> JamNameGenerator.NON_BENEFICIAL_EFFECT_ADJECTIVES = translationsArray;
                    case JamNameGenerator.NOUNS_KEY -> JamNameGenerator.NOUNS = translationsArray;
                    default -> {
                        JamModInit.LOGGER.error("Unknown translation key: " + baseKey);
                        throw new IllegalArgumentException();
                    }
                }
            });

            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                PacketByteBuf reqBuf1 = PacketByteBufs.create();
                reqBuf1.writeString(JamNameGenerator.BENEFICIAL_EFFECT_ADJECTIVES_KEY);
                PacketByteBuf reqBuf2 = PacketByteBufs.create();
                reqBuf2.writeString(JamNameGenerator.NON_BENEFICIAL_EFFECT_ADJECTIVES_KEY);
                PacketByteBuf reqBuf3 = PacketByteBufs.create();
                reqBuf3.writeString(JamNameGenerator.NOUNS_KEY);

                sender.sendPacket(GET_TRANSLATION_KEYS_REQ, reqBuf1);
                sender.sendPacket(GET_TRANSLATION_KEYS_REQ, reqBuf2);
                sender.sendPacket(GET_TRANSLATION_KEYS_REQ, reqBuf3);
            });
        }

    }
}

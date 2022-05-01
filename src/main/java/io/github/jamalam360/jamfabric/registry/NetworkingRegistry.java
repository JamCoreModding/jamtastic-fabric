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

package io.github.jamalam360.jamfabric.registry;

import io.github.jamalam360.jamfabric.jam.JamNameGenerator;
import io.github.jamalam360.jamfabric.util.Utils;
import io.github.jamalam360.jamfabric.util.helper.TranslationHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jamalam360
 */
public class NetworkingRegistry {
    public static final Identifier GET_TRANSLATION_KEYS_REQ = Utils.id("get_translation_count_req");
    private static final Identifier GET_TRANSLATION_KEYS_RES = Utils.id("get_translation_count_res");

    private static final Logger LOGGER = LogManager.getLogger("Jamtastic/Networking");

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
                        LOGGER.error("Unknown translation key: " + baseKey + ".");
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

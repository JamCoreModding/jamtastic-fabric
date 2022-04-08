package io.github.jamalam360.jamfabric.util.helper;

import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;

public class TranslationHelper {
    public static String[] getTranslations(String baseKey) {
        ArrayList<String> list = new ArrayList<>();
        int index = 0;

        while (I18n.hasTranslation(baseKey + "." + index)) {
            list.add(I18n.translate(baseKey + "." +
                    "" + index));
            index++;
        }

        return list.toArray(new String[0]);
    }
}

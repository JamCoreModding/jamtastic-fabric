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

package io.github.jamalam360.jamfabric.config;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.DirtTexturedBackground;
import dev.lambdaurora.spruceui.option.*;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import io.github.jamalam360.jamfabric.JamModInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.config.api.values.TrackedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam
 * Adapted from FoundationGame's ConfigInABarrel to use the Quilt Config API
 * Temporary and hacky, only supports the types Jamtastic needs.
 */
@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class ConfigScreen extends SpruceScreen {
    private final Screen parent;
    private SpruceOptionListWidget optionsWidget;

    protected ConfigScreen(Screen parent) {
        super(new TranslatableText("cfgbarrel.jamfabric.screen"));
        this.parent = parent;
    }

    private void addFields(@Nullable List<String> parents) {
        String pKey = "cfgbarrel.jamfabric.option";

        if (parents != null) {
            pKey += "." + parents.stream().reduce((a, b) -> a + "." + b).orElse("error");
        }

        for (TrackedValue<?> value : JamModInit.CONFIG.values()) {
            Class<?> clazz = value.getRealValue().getClass();
            String key = pKey + "." + value.key().getLastComponent();

            if (clazz.equals(int.class)) {
                this.optionsWidget.addSingleOptionEntry(new SpruceIntegerInputOption(key,
                        () -> (Integer) value.value(),
                        (i) -> ((TrackedValue<Integer>) value).setValue(i, true), null));
            } else if (clazz.equals(boolean.class)) {
                this.optionsWidget.addSingleOptionEntry(new SpruceToggleBooleanOption(key,
                        () -> (Boolean) value.value(),
                        (b) -> ((TrackedValue<Boolean>) value).setValue(b, true), null));
            } else if (clazz.isEnum()) {
                this.optionsWidget.addOptionEntry(new LabelOption(key), new SpruceCyclingOption(key,
                        (i) -> {
                            int s = clazz.getEnumConstants().length;
                            ((TrackedValue<AverageColorProviderType>) value).setValue(
                                    (AverageColorProviderType) clazz.getEnumConstants()[Math.floorMod(((Enum<?>) value.value()).ordinal() + 1, s)], true
                            );
                        },
                        opt -> new TranslatableText("cfgbarrel.jamfabric.enum." + ((Enum<?>) value.value()).name()), null));
            } else {
                this.optionsWidget.addSingleOptionEntry(new SpruceSeparatorOption(key, true, null));
                List<String> nParents = new ArrayList<>();
                if (parents != null) {
                    nParents.addAll(parents);
                }
                nParents.add(value.key().getLastComponent());
                this.addFields(nParents);
                this.optionsWidget.addSingleOptionEntry(new SpruceSeparatorOption("cfgbarrel.empty", false, null));
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderUtil.renderBackgroundTexture(0, 0, this.width, this.height, 0, 64, 64, 64, 255);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.getTitle(), this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        this.optionsWidget = new SpruceOptionListWidget(Position.of(0, 22), this.width, this.height - (35 + 22));
        this.optionsWidget.setBackground(DirtTexturedBackground.DARKENED);
        this.addFields(null);
        this.addDrawableChild(optionsWidget);
        int bottomCenter = this.width / 2 - 65;
        this.addDrawableChild(new SpruceButtonWidget(Position.of(bottomCenter - 69, this.height - 27), 130, 20, ScreenTexts.CANCEL, button -> this.onClose()));
        this.addDrawableChild(new SpruceButtonWidget(Position.of(bottomCenter + 69, this.height - 27), 130, 20, ScreenTexts.DONE, button -> {
            JamModInit.CONFIG.save();
            onClose();
        }));
    }

    @Environment(EnvType.CLIENT)
    private static class LabelOption extends SpruceOption {
        public LabelOption(String key) {
            super(key);
        }

        @Override
        public SpruceWidget createWidget(Position position, int width) {
            return new SpruceLabelWidget(position, new TranslatableText(key), width, w -> {
            }, false);
        }
    }
}

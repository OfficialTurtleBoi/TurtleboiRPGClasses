package net.turtleboi.turtlerpgclasses.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import org.jetbrains.annotations.NotNull;

public class ClassButton extends Button {
    private static final ResourceLocation CLASS_WIDGET = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/class_widget.png");
    private static final ResourceLocation CLASS_WIDGET_SELECTED = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/class_widget_selected.png");
    private static final ResourceLocation CLASS_WIDGET_BACKGROUND = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/class_widget_background.png");

    private final Component className, classDescription, classFeatures;

    public ClassButton(int x, int y, int width, int height, Component className, Component classDescription, Component classFeatures, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, className, onPress, createNarration);
        this.className = className;
        this.classDescription = classDescription;
        this.classFeatures = classFeatures;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        ResourceLocation resourceLocation = isHoveredOrFocused() ? CLASS_WIDGET_SELECTED : CLASS_WIDGET;
        guiGraphics.blit(resourceLocation, this.getX(), this.getY(), 0, 0, width, height, 128, 144);

        guiGraphics.drawCenteredString(font, className, this.getX() + width / 2, this.getY() + 7, 0xFFFFFF);

        //RenderSystem.setShaderTexture(0, CLASS_WIDGET_BACKGROUND);
        guiGraphics.blit(CLASS_WIDGET_BACKGROUND, this.getX() + 4, this.getY() + 20, 0, 0, 120, 119, 120, 119);

        renderTextWithinBounds(guiGraphics, classDescription, this.getX() + 8, this.getY() + 24, 120 - 14);
        renderTextWithinBounds(guiGraphics, classFeatures, this.getX() + 8, this.getY() + 24 + 9 * getLineCount(classDescription) + 12, 120 - 14);
    }

    private void renderTextWithinBounds(GuiGraphics guiGraphics, Component text, int x, int y, int width) {
        Font font = Minecraft.getInstance().font;
        for (FormattedCharSequence line : font.split(text, width)) {
            guiGraphics.drawString(font, line, x, y, 0xFFFFFF);
            y += 9;
        }
    }

    private int getLineCount(Component text) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.font.split(text, 120 - 12).size();
    }
}

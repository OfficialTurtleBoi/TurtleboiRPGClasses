package net.turtleboi.turtlerpgclasses.client.ui.resources;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;

public class ResourceBar {
    private static final ResourceLocation resourceBars = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/resourcebars.png");
    public static final int barWidth = 80;
    public static final int barHeight = 15;
    public static final int barXOffset = 3;

    private int x;
    private int y;
    private final float filledWidth;
    private final Component text;
    private final int textColor;
    private final int barTypeYOffset;
    private final boolean isMain;

    public ResourceBar(int x, int y, float filledWidth, Component text, int textColor, int barTypeYOffset, boolean isMain) {
        this.x = x;
        this.y = y;
        this.filledWidth = filledWidth;
        this.text = text;
        this.textColor = textColor;
        this.barTypeYOffset = barTypeYOffset;
        this.isMain = isMain;
    }

    public void render(GuiGraphics guiGraphics, Font font) {
        guiGraphics.blit(resourceBars, x, y, 0, barTypeYOffset, barWidth, barHeight, 80, 90);
        guiGraphics.blit(resourceBars, x + barXOffset, y, barXOffset, barTypeYOffset + barHeight, (int) filledWidth, barHeight, 80, 90);
        int textWidth = font.width(text) / 2;
        int textX = x + (barWidth / 2) - textWidth;
        int textY = y + (barHeight / 2);
        guiGraphics.drawString(font, text, textX - 1, textY, 0x330000, false);
        guiGraphics.drawString(font, text, textX + 1, textY, 0x330000, false);
        guiGraphics.drawString(font, text, textX, textY - 1, 0x330000, false);
        guiGraphics.drawString(font, text, textX, textY + 1, 0x330000, false);
        guiGraphics.drawString(font, text, textX, textY, textColor, false);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static class Builder {
        private int x;
        private int y;
        private float filledWidth;
        private Component text;
        private int textColor;
        private int barTypeYOffset;
        private boolean isMain;

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setFilledWidth(float filledWidth) {
            this.filledWidth = filledWidth;
            return this;
        }

        public Builder setText(Component text) {
            this.text = text;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setBarTypeYOffset(int barTypeYOffset) {
            this.barTypeYOffset = barTypeYOffset;
            return this;
        }

        public Builder setIsMain(boolean isMain) {
            this.isMain = isMain;
            return this;
        }

        public ResourceBar build() {
            return new ResourceBar(x, y, filledWidth, text, textColor, barTypeYOffset, isMain);
        }
    }
}

package net.turtleboi.turtlerpgclasses.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class MovableUIComponent extends AbstractWidget {
    public enum Anchor {
        TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT, BOTTOM_RIGHT, TOP_CENTER, BOTTOM_CENTER, CENTER;

        public static Anchor fromString(String anchor) {
            try {
                return Anchor.valueOf(anchor.toUpperCase());
            } catch (IllegalArgumentException e) {
                return TOP_LEFT;
            }
        }
    }

    private final String identifier;
    private boolean isDragging;
    private int lastMouseX;
    private int lastMouseY;
    private Anchor anchor;

    public MovableUIComponent(int x, int y, int width, int height, Component title, String anchor) {
        super(x, y, width, height, title);
        this.identifier = title.getString();
        this.anchor = Anchor.fromString(anchor);
        updatePosition(x, y);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isDragging) {
            this.setX(mouseX - this.lastMouseX);
            this.setY(mouseY - this.lastMouseY);
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY) && button == 0) {
            this.isDragging = true;
            this.lastMouseX = (int) mouseX;
            this.lastMouseY = (int) mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getAnchor() {
        return anchor.name();
    }

    public void setAnchor(String anchor) {
        this.anchor = Anchor.fromString(anchor);
    }

    public void updatePosition(int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        switch (anchor) {
            case TOP_LEFT:
                this.setX(x);
                this.setY(y);
                break;
            case TOP_RIGHT:
                this.setX(screenWidth - x - this.width);
                this.setY(y);
                break;
            case BOTTOM_LEFT:
                this.setX(x);
                this.setY(screenHeight - y - this.height);
                break;
            case BOTTOM_RIGHT:
                this.setX(screenWidth - x - this.width);
                this.setY(screenHeight - y - this.height);
                break;
            case TOP_CENTER:
                this.setX((screenWidth / 2) + x - (this.width / 2));
                this.setY(y);
                break;
            case BOTTOM_CENTER:
                this.setX((screenWidth / 2) + x - (this.width / 2));
                this.setY(screenHeight - y - this.height);
                break;
            case CENTER:
                this.setX((screenWidth / 2) + x - (this.width / 2));
                this.setY((screenHeight / 2) + y - (this.height / 2));
                break;
        }
    }
}

package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActiveTalentButton extends TalentButton {
    private static final ResourceLocation TALENT_TREE_TEXTURES = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/talent_widgets.png");
    protected Talent talentClass;
    private int x = getX();
    private int y = getY();

    public ActiveTalentButton(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass.getTalentName(), x, y, 26, 26, maxPoints,  requiredPoints, Component.empty(), alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    public ResourceLocation getIconTexture() {
        return talentClass.getIconTexture();
    }

    public void drawTalentIcon(GuiGraphics guiGraphics, int x, int y) {
        ResourceLocation customIconTexture = getIconTexture();
        if (customIconTexture != null) {
            Optional<Resource> resourceOptional = Minecraft.getInstance().getResourceManager().getResource(customIconTexture);
            if (resourceOptional.isPresent()) {
                RenderSystem.setShaderTexture(0, customIconTexture);
                int iconX = x + (this.width - 16) / 2;
                int iconY = y + (this.height - 16) / 2;

                if (isActive() || isUnlocked()) {
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1.0f);
                }
                guiGraphics.blit(customIconTexture, iconX, iconY, 0, 0, 16, 16, 16, 16);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        int textureX = 0;
        int textureY = 104;

        if (this.getState() == TalentState.ACTIVE) {
            textureY = isHovered ? 26 : 0;
        } else if (this.getState() == TalentState.UNLOCKED) {
            textureY = isHovered ? 78 : 52;
        } else if (this.getState() == TalentState.LOCKED) {
            textureY = 104;
        } else if (this.getState() == TalentState.UNIQUE_LOCKED) {
            textureY = 104;
        }

        RenderSystem.setShaderTexture(0, TALENT_TREE_TEXTURES);
        guiGraphics.blit(TALENT_TREE_TEXTURES, this.getX(), this.getY(), textureX, textureY, this.width, this.height, 104, 130);
        drawTalentIcon(guiGraphics, this.getX(), this.getY());
        if (this.getState() == TalentState.ACTIVE && this.getCurrentPoints() >= 1 && this.maxPoints > 1) {
            String pointsText = getCurrentPoints() + "/" + maxPoints;
            Font font = Minecraft.getInstance().font;
            int textWidth = font.width(pointsText);
            int textX = this.getX() + (this.width - textWidth) / 2;
            int textY = (this.getY() + this.height) - 6;
            int outlineColor = 0xFF154015;
            int textColor = 0xFF55FF55;
            guiGraphics.drawString(font, pointsText, textX - 1, textY, outlineColor);
            guiGraphics.drawString(font, pointsText, textX + 1, textY, outlineColor);
            guiGraphics.drawString(font, pointsText, textX, textY - 1, outlineColor);
            guiGraphics.drawString(font, pointsText, textX, textY + 1, outlineColor);
            guiGraphics.drawString(font, pointsText, textX, textY, textColor);
        }
        setTooltipText(generateDynamicTooltip());
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        return new ArrayList<>();
    }
}


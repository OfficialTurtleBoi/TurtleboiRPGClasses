package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public class MageTalentTree extends TalentTree {
    public MageTalentTree(int posX, int posY, int width, int height, TalentScreen talentScreen) {
        super(posX, posY, width, height, true, talentScreen);
    }
    @Override
    protected void setupTalentButtons() {

    }

    @Override
    protected void drawConnectionsTexture(GuiGraphics guiGraphics) {

    }
}

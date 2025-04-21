package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.juggernaut.juggernautSubclassNode;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.paladin.paladinSubclassNode;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.classes.subclasses.Paladin;

import java.util.function.Supplier;

public class PaladinTalentTree extends TalentTree {
    public PaladinTalentTree(int posX, int posY, int width, int height, TalentScreen talentScreen) {
        super(posX, posY, width, height, true, talentScreen);
    }
    @Override
    protected void drawConnectionsTexture(GuiGraphics guiGraphics) {

    }

    @Override
    protected void setupTalentButtons() {
        int centerX = posX + (width / 2) - 13;
        int startY = posY + 13;
        String subclassName = ClientClassData.getPlayerSubclass();
        paladinSubclassNode paladinSubclassNode = new paladinSubclassNode(
                this,
                new Paladin(),
                centerX,
                startY,
                1,
                0,
                ("Paladin".equals(subclassName)),
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(paladinSubclassNode, TalentButton.TalentState.ACTIVE);
    }
}

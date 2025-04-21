package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.barbarian.barbarianSubclassNode;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.juggernaut.juggernautSubclassNode;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.classes.subclasses.Juggernaut;

import java.util.function.Supplier;

public class JuggernautTalentTree extends TalentTree {
    public JuggernautTalentTree(int posX, int posY, int width, int height, TalentScreen talentScreen) {
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
        juggernautSubclassNode juggernautSubclassNode = new juggernautSubclassNode(
                this,
                new Juggernaut(),
                centerX,
                startY,
                1,
                0,
                ("Juggernaut".equals(subclassName)),
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(juggernautSubclassNode, TalentButton.TalentState.ACTIVE);
    }
}

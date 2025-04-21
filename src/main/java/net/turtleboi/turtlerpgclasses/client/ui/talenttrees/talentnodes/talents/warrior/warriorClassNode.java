package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.CapstoneTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class warriorClassNode extends CapstoneTalentButton {
    public warriorClassNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();

        tooltip.add(Component.translatable("class.warrior.name")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.capstone")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        tooltip.add(Component.translatable("class.warrior.description"));
        tooltip.add(Component.translatable("talents.reset_points")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        return tooltip;
    }

    @Override
    public void onRightClick(double mouseX, double mouseY) {
        super.onRightClick(mouseX, mouseY);
        if (this.getState() != TalentState.ACTIVE) {
            this.setState(TalentState.UNLOCKED);
            this.setState(TalentState.ACTIVE);
            this.setCurrentPoints(1);
        }
        if (this.getState() == TalentState.ACTIVE) {
            this.setState(TalentState.UNLOCKED);
            this.setState(TalentState.ACTIVE);
            this.setCurrentPoints(1);
        }
    }
}

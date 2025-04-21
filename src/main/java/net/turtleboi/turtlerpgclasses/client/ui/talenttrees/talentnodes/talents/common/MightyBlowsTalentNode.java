package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.common;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class MightyBlowsTalentNode extends StatTalentButton {
    public MightyBlowsTalentNode(TalentTree talentTree, Talent talent, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talent, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double statMultiplier = 0.5;//Change this value for each talent
        double currentRankValue = (currentPoints * statMultiplier);
        double nextRankValue = (currentRankValue + statMultiplier);
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.mighty_blows")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.stat")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(Component.literal(" +0.5/1.0/1.5/2.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.melee_damage")));//Change this value for each talent
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component pointsComponent = Component.literal(" +" + (isShiftPressed ? nextRankValue : currentRankValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(pointsComponent)
                    .append(Component.translatable("talents.melee_damage")));//Change this value for each talent

            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component pointsComponent = Component.literal(" +" + currentRankValue + " ");
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(pointsComponent)
                    .append(Component.translatable("talents.melee_damage")));//Change this value for each talent
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

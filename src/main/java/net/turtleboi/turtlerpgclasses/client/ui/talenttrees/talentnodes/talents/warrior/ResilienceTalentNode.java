package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class ResilienceTalentNode extends StatTalentButton {
    public ResilienceTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] healthValues = {4.0, 8.0, 12.0, 16.0, 20.0};
        double[] armorValues = {2.0, 3.0, 4.0, 5.0, 6.0};
        double[] armorToughnessValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, healthValues.length - 1));
        double healthValue = healthValues[currentRankIndex];
        double nextRankHealthValue = currentRankIndex < healthValues.length - 1 ? healthValues[currentRankIndex + 1] : healthValue;
        double armorValue = armorValues[currentRankIndex];
        double nextRankArmorValue = currentRankIndex < healthValues.length - 1 ? healthValues[currentRankIndex + 1] : armorValue;
        double armorToughnessValue = armorToughnessValues[currentRankIndex];
        double nextRankArmorToughnessValue = currentRankIndex < healthValues.length - 1 ? healthValues[currentRankIndex + 1] : armorToughnessValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.resilience")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.stat")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(Component.literal(" +4.0/8.0/12.0/16.0/20.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.health_points"))
                    .append(Component.literal(" +2.0/3.0/4.0/5.0/6.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.armor_points"))
                    .append(Component.literal(" +1.0/+2.0/+3.0/+4.0/+5.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.armor_toughness")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component healthComponent = Component.literal(" +" + (isShiftPressed ? nextRankHealthValue : healthValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component armorComponent = Component.literal(" +" + (isShiftPressed ? nextRankArmorValue : armorValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component armorToughnessComponent = Component.literal(" +" + (isShiftPressed ? nextRankArmorToughnessValue : armorToughnessValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(healthComponent)
                    .append(Component.translatable("talents.health_points"))
                    .append(armorComponent)
                    .append(Component.translatable("talents.armor_points"))
                    .append(armorToughnessComponent)
                    .append(Component.translatable("talents.armor_toughness")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component healthComponent = Component.literal(" +" + (healthValue) + " ");
            Component armorComponent = Component.literal(" +" + (armorValue) + "% ");
            Component armorToughnessComponent = Component.literal(" +" + (armorToughnessValue) + " ");
            tooltip.add(Component.translatable("talents.player_gains")
                    .append(healthComponent)
                    .append(Component.translatable("talents.health_points"))
                    .append(armorComponent)
                    .append(Component.translatable("talents.armor_points"))
                    .append(armorToughnessComponent)
                    .append(Component.translatable("talents.armor_toughness")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

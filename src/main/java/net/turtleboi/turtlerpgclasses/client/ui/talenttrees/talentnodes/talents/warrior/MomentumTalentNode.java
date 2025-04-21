package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.PassiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class MomentumTalentNode extends PassiveTalentButton {
    public MomentumTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] speedValues = {5.0, 10.0, 18.0};
        String[] strengthStrings = {"Strength", "Strength II", "Strength III"};
        double[] durationValues = {6.0, 8.0, 10.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, speedValues.length - 1));
        int currentRankStringIndex = (int) Math.max(0, Math.min(currentPoints - 1, strengthStrings.length - 1));
        double speedValue = speedValues[currentRankIndex];
        double nextRankSpeedValue = currentRankIndex < speedValues.length - 1 ? speedValues[currentRankIndex + 1] : speedValue;
        String strengthString = strengthStrings[currentRankStringIndex];
        String nextRankStrengthString = currentRankStringIndex < strengthStrings.length - 1 ? strengthStrings[currentRankStringIndex + 1] : strengthString;
        double durationValue = durationValues[currentRankIndex];
        double nextRankDurationValue = currentRankIndex < durationValues.length - 1 ? durationValues[currentRankIndex + 1] : durationValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.momentum")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.momentum.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.charge")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.momentum.description2"))
                    .append(Component.literal(" +5.0/7.0%/10.0%/15.0%/20.0% ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.movement_speed"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(Component.literal("Strength")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Strength II")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Strength III")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("for"))
                    .append(Component.literal(" 6.0/8.0/10.0 "))
                    .append(Component.translatable("seconds")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component speedComponent = Component.literal(" +" + (isShiftPressed ? nextRankSpeedValue : speedValue) + "% ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component strengthComponent = Component.literal((isShiftPressed ? nextRankStrengthString : strengthString))
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                            Style.EMPTY.withColor(TextColor.parseColor("#C70039")));
            Component durationComponent = Component.literal(" +" + (isShiftPressed ? nextRankDurationValue : durationValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.momentum.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.charge")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.momentum.description2"))
                    .append(speedComponent)
                    .append(Component.translatable("talents.movement_speed"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(strengthComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("for"))
                    .append(durationComponent)
                    .append(Component.translatable("seconds")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component speedComponent = Component.literal((" +" + speedValue) + "% ");
            Component strengthComponent = Component.literal((strengthString))
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#C70039")));
            Component durationComponent = Component.literal((" +" + durationValue) + " ");
            tooltip.add(Component.translatable("talents.momentum.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.charge")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.momentum.description2"))
                    .append(speedComponent)
                    .append(Component.translatable("talents.movement_speed"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(strengthComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("for"))
                    .append(durationComponent)
                    .append(Component.translatable("seconds")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

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

public class SecondWindTalentNode extends PassiveTalentButton {
    public SecondWindTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] speedBoostValues = {10.0, 20.0, 30.0, 40.0, 50.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, speedBoostValues.length - 1));
        double speedBoostValue = speedBoostValues[currentRankIndex];
        double nextRankSpeedBoostValue = currentRankIndex < speedBoostValues.length - 1 ? speedBoostValues[currentRankIndex + 1] : speedBoostValue;
        double[] healthRecoveryValues = {10.0, 12.0, 14.0, 18.0, 24.0};
        double healthRecoveryValue = healthRecoveryValues[currentRankIndex];
        double nextRankhealthRecoveryValue = currentRankIndex < healthRecoveryValues.length - 1 ? healthRecoveryValues[currentRankIndex + 1] : healthRecoveryValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.second_wind")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.second_wind.description1")//Change this value for each talent
                    .append(Component.translatable("talents.second_wind")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#9CA5CE"))))
                    .append(Component.translatable("talents.second_wind.description2"))
                    .append(Component.literal("10.0%/20.0%/30.0%/40.0%/50.0% ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.second_wind.description3"))
                    .append(Component.literal("10.0/12.0/14.0/18.0/24.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.health_points"))
                    .append(Component.translatable("talents.second_wind.description4")));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.second_wind.description5")
                    .append(Component.translatable("effect.turtlerpgclasses.winded")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#C0A44D"))))
                    .append(Component.translatable("talents.second_wind.description6")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component speedComponent = Component.literal((isShiftPressed ? nextRankSpeedBoostValue : speedBoostValue) + "%")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component healComponent = Component.literal((isShiftPressed ? nextRankhealthRecoveryValue : healthRecoveryValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.second_wind.description1")//Change this value for each talent
                    .append(Component.translatable("talents.second_wind")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#9CA5CE"))))
                    .append(Component.translatable("talents.second_wind.description2"))
                    .append(speedComponent)
                    .append(Component.translatable("talents.second_wind.description3"))//Change this value for each talent
                    .append(healComponent)
                    .append(Component.translatable("talents.health_points"))
                    .append(Component.translatable("talents.second_wind.description4")));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.second_wind.description5")
                    .append(Component.translatable("effect.turtlerpgclasses.winded")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#9070BB"))))
                    .append(Component.translatable("talents.second_wind.description6")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component speedComponent = Component.literal((speedBoostValue) + "%");
            Component healComponent = Component.literal((healthRecoveryValue) + " ");
            tooltip.add(Component.translatable("talents.second_wind.description1")//Change this value for each talent
                    .append(Component.translatable("talents.second_wind")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#9CA5CE"))))
                    .append(Component.translatable("talents.second_wind.description2"))
                    .append(speedComponent)
                    .append(Component.translatable("talents.second_wind.description3"))//Change this value for each talent
                    .append(healComponent)
                    .append(Component.translatable("talents.health_points"))
                    .append(Component.translatable("talents.second_wind.description4")));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.second_wind.description5")
                    .append(Component.translatable("effect.turtlerpgclasses.winded")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#9070BB"))))
                    .append(Component.translatable("talents.second_wind.description6")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

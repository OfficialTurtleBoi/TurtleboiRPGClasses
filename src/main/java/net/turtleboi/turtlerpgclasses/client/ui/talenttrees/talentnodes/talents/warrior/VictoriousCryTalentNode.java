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

public class VictoriousCryTalentNode extends PassiveTalentButton {
    public VictoriousCryTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] damageBuffValues = {12.0, 17.0, 24.0};
        double[] staminaValues = {10.0, 16.25, 25.0};
        double[] healValues = {5.0, 7.0, 12.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, damageBuffValues.length - 1));
        double damageBuffValue = damageBuffValues[currentRankIndex];
        double nextRankDamageBuffValue = currentRankIndex < damageBuffValues.length - 1 ? damageBuffValues[currentRankIndex + 1] : damageBuffValue;
        double staminaValue = staminaValues[currentRankIndex];
        double nextRankStaminaValue = currentRankIndex < staminaValues.length - 1 ? staminaValues[currentRankIndex + 1] : staminaValue;
        double healValue = healValues[currentRankIndex];
        double nextRankHealValue = currentRankIndex < healValues.length - 1 ? healValues[currentRankIndex + 1] : healValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.victorious_cry")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.victorious_cry.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description2"))
                    .append(Component.literal(" 12.0%/17.0%/24.0% ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("for"))
                    .append(Component.literal(" 5 "))
                    .append(Component.translatable("seconds")));
            tooltip.add(Component.literal(" "));
            tooltip.add((Component.translatable("talents.victorious_cry.description3"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description4"))
                    .append(Component.literal(" 10.0%/16.25%/25.0% ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.victorious_cry.description5"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.stamina")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.translatable("talents.victorious_cry.description6"))
                    .append(Component.literal(" 5.0/7.0/12.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.health_points")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component damageBuffComponent = Component.literal(" " + (isShiftPressed ? nextRankDamageBuffValue : damageBuffValue) + "% ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component staminaComponent = Component.literal(" " + (isShiftPressed ? nextRankStaminaValue : staminaValue) + "% ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            Component healComponent = Component.literal(" " + (isShiftPressed ? nextRankHealValue : healValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.victorious_cry.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description2"))
                    .append(damageBuffComponent)
                    .append(Component.translatable("for"))
                    .append(Component.literal(" 5 "))
                    .append(Component.translatable("seconds")));
            tooltip.add(Component.literal(" "));
            tooltip.add((Component.translatable("talents.victorious_cry.description3"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description4"))
                    .append(staminaComponent)
                    .append(Component.translatable("talents.victorious_cry.description5"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.stamina")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.translatable("talents.victorious_cry.description6"))
                    .append(healComponent)
                    .append(Component.translatable("talents.health_points")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component damageBuffComponent = Component.literal(" " + (damageBuffValue) + "% ");
            Component staminaComponent = Component.literal(" " + (staminaValue) + "% ");
            Component healComponent = Component.literal(" " + (healValue) + " ");
            tooltip.add(Component.translatable("talents.victorious_cry.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description2"))
                    .append(damageBuffComponent)
                    .append(Component.translatable("for"))
                    .append(Component.literal(" 5 "))
                    .append(Component.translatable("seconds")));
            tooltip.add(Component.literal(" "));
            tooltip.add((Component.translatable("talents.victorious_cry.description3"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.taunt")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                    .append(Component.translatable("talents.victorious_cry.description4"))
                    .append(staminaComponent)
                    .append(Component.translatable("talents.victorious_cry.description5"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.stamina")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.translatable("talents.victorious_cry.description6"))
                    .append(healComponent)
                    .append(Component.translatable("talents.health_points")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

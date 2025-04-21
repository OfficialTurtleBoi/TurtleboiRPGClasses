package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.ActiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class StampedeTalentNode extends ActiveTalentButton {
    public StampedeTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] staminaValues = {6.0, 6.0, 5.0};
        double[] cooldownValues = {12.0, 11.0, 10.0};
        double[] damageValues = {5.0, 7.0, 10.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, staminaValues.length - 1));
        double staminaValue = staminaValues[currentRankIndex];
        double nextRankStaminaValue = currentRankIndex < staminaValues.length - 1 ? staminaValues[currentRankIndex + 1] : staminaValue;
        double cooldownValue = cooldownValues[currentRankIndex];
        double nextRankCooldownValue = currentRankIndex < cooldownValues.length - 1 ? cooldownValues[currentRankIndex + 1] : cooldownValue;
        double damageValue = damageValues[currentRankIndex];
        double nextRankDamageValue = currentRankIndex < damageValues.length - 1 ? damageValues[currentRankIndex + 1] : damageValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.stampede")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.active")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))))
                .append(Component.literal(" "))
                .append(Component.literal("(")
                        .append(Component.translatable("replaces"))
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.charge")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                .append(Component.literal(")")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("6.0/6.0/5.0")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(Component.literal("12.0s/11.0s/10.0s")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    ));
            tooltip.add(Component.translatable("talents.stampede.description1")
                    .append(Component.literal(" 5.0/7.0/10.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.melee_damage")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component staminaComponent = Component.literal(" " + (isShiftPressed ? nextRankStaminaValue : staminaValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                            Style.EMPTY.withColor(TextColor.parseColor("#AA0000")));
            Component cooldownComponent = Component.literal(" " + (isShiftPressed ? nextRankCooldownValue : cooldownValue) + "s ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                            Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));
            Component damageComponent = Component.literal(" " + (isShiftPressed ? nextRankDamageValue : damageValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(staminaComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(cooldownComponent)
                    ));
            tooltip.add(Component.translatable("talents.stampede.description1")
                    .append(damageComponent)
                    .append(Component.translatable("talents.melee_damage")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component staminaComponent = Component.literal(" " + (staminaValue) + " ")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")));
            Component cooldownComponent = Component.literal(" " + (cooldownValue) + "s ")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));
            Component damageComponent = Component.literal(" " + (damageValue) + " ");
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(staminaComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(cooldownComponent)
                    ));
            tooltip.add(Component.translatable("talents.stampede.description1")
                    .append(damageComponent)
                    .append(Component.translatable("talents.melee_damage")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

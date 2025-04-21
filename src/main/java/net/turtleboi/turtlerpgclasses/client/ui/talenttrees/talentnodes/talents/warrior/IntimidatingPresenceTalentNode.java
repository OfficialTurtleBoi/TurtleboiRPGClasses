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

public class IntimidatingPresenceTalentNode extends ActiveTalentButton {
    public IntimidatingPresenceTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] cooldownValues = {20.0, 16.0, 12.0};
        double[] absorptionValues = {1.0, 2.0, 4.0};
        String[] weaknessStrings = {"Weakness", "Weakness", "Weakness II"};
        String[] slownessStrings = {"Slowness", "Slowness", "Slowness II"};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, cooldownValues.length - 1));
        int currentRankStringIndex = (int) Math.max(0, Math.min(currentPoints - 1, weaknessStrings.length - 1));
        double cooldownValue = cooldownValues[currentRankIndex];
        double nextRankCooldownValue = currentRankIndex < cooldownValues.length - 1 ? cooldownValues[currentRankIndex + 1] : cooldownValue;
        String weaknessString = weaknessStrings[currentRankStringIndex];
        String nextRankWeaknessString = currentRankStringIndex < weaknessStrings.length - 1 ? weaknessStrings[currentRankStringIndex + 1] : weaknessString;
        String slownessString = slownessStrings[currentRankStringIndex];
        String nextRankSlownessString = currentRankStringIndex < slownessStrings.length - 1 ? slownessStrings[currentRankStringIndex + 1] : slownessString;
        double absorptionValue = absorptionValues[currentRankIndex];
        double nextRankAbsorptionValue = currentRankIndex < absorptionValues.length - 1 ? absorptionValues[currentRankIndex + 1] : absorptionValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.intimidating_presence")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.active")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))))
                .append(Component.literal(" "))
                .append(Component.literal("(")
                        .append(Component.translatable("replaces"))
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.taunt")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                .append(Component.literal(")")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("7")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(Component.literal("20.0s/16.0s/12.0s")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    ));
            tooltip.add(Component.translatable("talents.intimidating_presence.description1")
                    .append(Component.literal(" "))
                    .append(Component.literal("Weakness")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Weakness")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Weakness II")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(Component.literal("Slowness")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Slowness")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal("/"))
                    .append(Component.literal("Slowness II")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.intimidating_presence.description2"))
                    .append(Component.literal(" +1.0/2.0/4.0 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.points_of"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.absorption")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.intimidating_presence.description3")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component cooldownComponent = Component.literal(" " + (isShiftPressed ? nextRankCooldownValue : cooldownValue) + "s ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : 
                            Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));
            Component weaknessComponent = Component.literal((isShiftPressed ? nextRankWeaknessString : weaknessString))
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                            Style.EMPTY.withColor(TextColor.parseColor("#BFAEB5")));
            Component slownessComponent = Component.literal((isShiftPressed ? nextRankSlownessString : slownessString))
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                            Style.EMPTY.withColor(TextColor.parseColor("#AEADDF")));
            Component absorptionComponent = Component.literal(" +" + (isShiftPressed ? nextRankAbsorptionValue : absorptionValue) + " ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("7")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(cooldownComponent)
                    ));
            tooltip.add(Component.translatable("talents.intimidating_presence.description1")
                    .append(Component.literal(" "))
                    .append(weaknessComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(slownessComponent)
                    .append(Component.translatable("talents.intimidating_presence.description2"))
                    .append(absorptionComponent)
                    .append(Component.translatable("talents.points_of"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.absorption")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.intimidating_presence.description3")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component cooldownComponent = Component.literal(" " + (cooldownValue) + "s ")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));
            Component weaknessComponent = Component.literal((weaknessString))
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#BFAEB5")));
            Component slownessComponent = Component.literal((slownessString))
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AEADDF")));
            Component absorptionComponent = Component.literal(" +" + (absorptionValue) + " ");
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("7")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(cooldownComponent)
                    ));
            tooltip.add(Component.translatable("talents.intimidating_presence.description1")
                    .append(Component.literal(" "))
                    .append(weaknessComponent)
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(Component.literal(" "))
                    .append(slownessComponent)
                    .append(Component.translatable("talents.intimidating_presence.description2"))
                    .append(absorptionComponent)
                    .append(Component.translatable("talents.points_of"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.absorption")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.intimidating_presence.description3")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.common;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.PassiveTalentButton;
import net.turtleboi.turtlerpgclasses.effect.effects.FocusedStrikesEffect;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.FocusedStrikesTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BrawlersTenacityTalent;

import java.util.ArrayList;
import java.util.List;

public class FocusedStrikesTalentNode extends PassiveTalentButton {

    public FocusedStrikesTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof FocusedStrikesTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent damageIncreaseComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getDamageIncrease
            );

            MutableComponent hitThresholdComponent = buildValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getHitThreshold
            );

            MutableComponent maxDamageComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getDamageMaximum
            );

        tooltip.add(Component.literal(talentName)
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));

            tooltip.add(Component.translatable("talents.focused_strikes.description1")//Change this value for each talent
                    .append(damageIncreaseComponent)
                    .append(Component.translatable("talents.focused_strikes.description2"))//Change this value for each talent
                    .append(hitThresholdComponent)
                    .append(Component.translatable("talents.focused_strikes.description3"))
                    .append(maxDamageComponent));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.focused_strikes.description4"));

            if (currentPoints == 0) {
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else if (currentPoints < maxPoints) {
                if (!isShiftPressed) {
                    tooltip.add(Component.translatable("talents.press_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                } else {
                    tooltip.add(Component.translatable("talents.release_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                }
            } else if (currentPoints == maxPoints) {
                tooltip.add(Component.translatable("talents.max_rank")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
            return tooltip;
        }
        return null;
    }
}

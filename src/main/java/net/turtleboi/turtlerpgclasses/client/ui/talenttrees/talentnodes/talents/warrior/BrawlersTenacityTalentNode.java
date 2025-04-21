package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.PassiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.LethalityTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BrawlersTenacityTalent;

import java.util.ArrayList;
import java.util.List;

public class BrawlersTenacityTalentNode extends PassiveTalentButton {
    public BrawlersTenacityTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof BrawlersTenacityTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent armorComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getArmorIncrease
            );

            MutableComponent maxArmorComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getMaxArmor
            );

            tooltip.add(Component.literal(talentName)
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));

            tooltip.add(Component.translatable("talents.brawlers_tenacity1")
                    .append(armorComponent)
                    .append(Component.translatable("talents.brawlers_tenacity2"))
                    .append(maxArmorComponent)
                    .append(Component.translatable("talents.brawlers_tenacity3")));

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

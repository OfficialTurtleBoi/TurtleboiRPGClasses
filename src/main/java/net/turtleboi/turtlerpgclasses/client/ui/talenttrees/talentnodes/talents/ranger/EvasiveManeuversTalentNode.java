package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.ranger;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.PassiveTalentButton;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.BeastmastersAxiomSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.EvasiveManeuversTalent;

import java.util.ArrayList;
import java.util.List;

public class EvasiveManeuversTalentNode extends PassiveTalentButton {
    public EvasiveManeuversTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof EvasiveManeuversTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent dodgeChanceComponent = buildPlusValuePercentComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getDodgeChance
            );

            tooltip.add(Component.literal(talentName)
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.passive")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                    ));

            tooltip.add(Component.translatable("talents.evasive_maneuvers.description1")
                    .append(dodgeChanceComponent)
                    .append(Component.translatable("talents.dodge_chance")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#ad3df6"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.evasive_maneuvers.description2"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.dodge_chance")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#ad3df6"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.evasive_maneuvers.description3"))
            );

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

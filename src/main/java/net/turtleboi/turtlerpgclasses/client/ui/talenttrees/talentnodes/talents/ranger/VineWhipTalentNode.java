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
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.VineWhipTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BattleHardenedTalent;

import java.util.ArrayList;
import java.util.List;

public class VineWhipTalentNode extends PassiveTalentButton {
    public VineWhipTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof VineWhipTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent rootComponent = buildValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getRootDuration
            );

            MutableComponent swiftnessComponent = buildRankedStringComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getSwiftnessComponent,
                    "#33EBFF"
            );

            tooltip.add(Component.literal(talentName)
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.passive")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))))
            );

            tooltip.add(Component.translatable("talents.vine_whip.description1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("effect.turtlecore.rooted")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4B3300"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.vine_whip.description2"))
                    .append(rootComponent)
                    .append(Component.translatable("talents.vine_whip.description3"))
                    .append(swiftnessComponent)
                    .append(Component.translatable("talents.vine_whip.description4"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("effect.turtlecore.rooted")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4B3300"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.vine_whip.description5")));

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

package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.ranger;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.ActiveTalentButton;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.BeastmastersAxiomSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.RoguesAxiomSubclass;

import java.util.ArrayList;
import java.util.List;

public class RoguesAxiomSubclassNode extends ActiveTalentButton {
    public RoguesAxiomSubclassNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof RoguesAxiomSubclass talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            //MutableComponent criticalChanceComponent = buildPlusValuePercentComponent(
            //        currentPoints,
            //        maxPoints,
            //        isShiftPressed,
            //        talent::getCriticalChance
            //);

            tooltip.add(Component.literal(talentName)
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.subclass")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                    ));

            tooltip.add(Component.translatable("subclass.rogue.description")
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

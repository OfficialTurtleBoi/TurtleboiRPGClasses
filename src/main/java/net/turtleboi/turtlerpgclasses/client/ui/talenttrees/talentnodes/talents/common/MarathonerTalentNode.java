package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.common;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.MarathonerTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.VigorTalent;

import java.util.ArrayList;
import java.util.List;

public class MarathonerTalentNode extends StatTalentButton {
    public MarathonerTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof MarathonerTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent staminaComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getStamina
            );

            MutableComponent energyComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getEnergy
            );

            tooltip.add(Component.literal(talentName)
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.stat")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                    ));

            String playerClass = ClientClassData.getPlayerClass();
            if ("Warrior".equals(playerClass)) {
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(staminaComponent)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.points"))
                );
            } else if ("Ranger".equals(playerClass)) {
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(energyComponent)
                        .append(Component.translatable("talents.energy")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF61"))))
                        .append(Component.translatable("talents.points"))
                );
            }
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

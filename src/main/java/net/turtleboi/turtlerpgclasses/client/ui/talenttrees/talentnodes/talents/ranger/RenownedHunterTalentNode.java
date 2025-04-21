package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.ranger;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.RenownedHunterTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.WeakPointsTalent;

import java.util.ArrayList;
import java.util.List;

public class RenownedHunterTalentNode extends StatTalentButton {
    public RenownedHunterTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
        this.maxPoints = maxPoints;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof RenownedHunterTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            String talentName = talent.getName();
            int currentPoints = getCurrentPoints();
            boolean isShiftPressed = Screen.hasShiftDown();

            MutableComponent rangedDamageComponent = buildPlusValueComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getRangeDamage
            );

            MutableComponent attackSpeedComponent = buildPlusValuePercentComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getAttackSpeed
            );

            MutableComponent criticalHitChanceComponent = buildPlusValuePercentComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getCriticalHitChance
            );

            MutableComponent criticalHitDamageComponent = buildPlusValuePercentComponent(
                    currentPoints,
                    maxPoints,
                    isShiftPressed,
                    talent::getCriticalHitDamage
            );

            tooltip.add(Component.literal(talentName)
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.stat")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                    ));

            tooltip.add(Component.translatable("talents.player_gains")
                    .append(rangedDamageComponent)
                    .append(Component.translatable("talents.ranged_damage"))
                    .append(attackSpeedComponent)
                    .append(Component.translatable("talents.attack_speed"))
                    .append(criticalHitChanceComponent)
                    .append(Component.translatable("talents.critical_hit_chance"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("and"))
                    .append(criticalHitDamageComponent)
                    .append(Component.translatable("talents.critical_hit_damage"))
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

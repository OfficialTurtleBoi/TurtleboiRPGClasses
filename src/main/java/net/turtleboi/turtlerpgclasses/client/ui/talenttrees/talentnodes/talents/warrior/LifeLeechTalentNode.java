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

public class LifeLeechTalentNode extends PassiveTalentButton {
    public LifeLeechTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        double[] healPercentValues = {2.5, 5.0, 10.0, 15.0, 20.0};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, healPercentValues.length - 1));
        double healPercentValue = healPercentValues[currentRankIndex];
        double nextRankHealPercentValue = currentRankIndex < healPercentValues.length - 1 ? healPercentValues[currentRankIndex + 1] : healPercentValue;
        boolean isShiftPressed = Screen.hasShiftDown();

        tooltip.add(Component.translatable("talents.life_leech")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.passive")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.life_leech1")
                    .append(Component.literal(" 2.5%/5.0%/10.0%/15.00%/20.0% ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.life_leech2"))
                    .append(Component.translatable("talents.spell_effect.bleed")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                    .append(Component.translatable("talents.life_leech3")));
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints < maxPoints) {
            Component leechComponent = Component.literal(" " + (isShiftPressed ? nextRankHealPercentValue : healPercentValue) + "% ")
                    .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
            tooltip.add(Component.translatable("talents.life_leech1")
                    .append(leechComponent)
                    .append(Component.translatable("talents.life_leech2"))
                    .append(Component.translatable("talents.spell_effect.bleed")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                    .append(Component.translatable("talents.life_leech3")));
            if (!isShiftPressed) {
                tooltip.add(Component.translatable("talents.press_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else {
                tooltip.add(Component.translatable("talents.release_shift")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (currentPoints == maxPoints){
            Component leechComponent = Component.literal((" " + healPercentValue) + "% ");
            tooltip.add(Component.translatable("talents.life_leech1")
                    .append(leechComponent)
                    .append(Component.translatable("talents.life_leech2"))
                    .append(Component.translatable("talents.spell_effect.bleed")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                    .append(Component.translatable("talents.life_leech3")));
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }
}

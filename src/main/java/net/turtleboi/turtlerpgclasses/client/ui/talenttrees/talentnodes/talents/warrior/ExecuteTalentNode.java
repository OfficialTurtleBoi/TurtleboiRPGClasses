package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.ActiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.ArrayList;
import java.util.List;

public class ExecuteTalentNode extends ActiveTalentButton {
    public ExecuteTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();

        tooltip.add(Component.translatable("talents.execute")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.active")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("4.0")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(Component.literal("10.0s")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    ));
            tooltip.add(Component.translatable("talents.execute.description_1")
                    .append(Component.literal(" 12 ")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                    .append(Component.translatable("talents.execute.description_2"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.spell_effect.bleeding")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                    .append(Component.literal(", "))
                    .append(Component.translatable("talents.execute.description_3")));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.execute.description_4")
                    .append(Component.translatable("talents.execute")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))));
        }else if (currentPoints == maxPoints){
            tooltip.add(Component.translatable("talents.cost_stamina")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                    .append(Component.literal("4.0")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.cooldown")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                            .append(Component.literal("10.0s")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA"))))
                    ));
            tooltip.add(Component.translatable("talents.execute.description_1")
                    .append(Component.literal(" 12 "))
                    .append(Component.translatable("talents.execute.description_2"))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.spell_effect.bleeding")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                    .append(Component.literal(", "))
                    .append(Component.translatable("talents.execute.description_3")));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("talents.execute.description_4")
                    .append(Component.translatable("talents.execute")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))));
        }
        return tooltip;
    }
}

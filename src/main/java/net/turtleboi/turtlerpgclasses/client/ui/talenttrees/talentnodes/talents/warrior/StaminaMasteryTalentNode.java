package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.StatTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheBarbarianSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheJuggernautSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfThePaladinSubclass;

import java.util.ArrayList;
import java.util.List;

public class StaminaMasteryTalentNode extends StatTalentButton {
    public StaminaMasteryTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        Player player = Minecraft.getInstance().player;
        double currentPoints = getCurrentPoints();
        double[] staminaValues = {2.0, 4.0, 6.0, 8.0, 10.0};
        double[] staminaRechargeRateValues = {5.0, 7.0, 10.0, 15.0, 25.0};
        double[] staminaValues2 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] staminaRechargeRateValues2 = {2.5, 5.0, 8.5, 12.0, 16.25};
        double[] manaValues = {10.0, 20.0, 30.0, 40.0, 60.0};
        double[] manaRechargeRateValues = {2.5, 5.0, 8.5, 12.0, 16.25};
        int currentRankIndex = (int) Math.max(0, Math.min(currentPoints - 1, staminaValues.length - 1));
        double staminaValue = staminaValues[currentRankIndex];
        double staminaRechargeRateValue = staminaRechargeRateValues[currentRankIndex];
        double nextRankStaminaValue = currentRankIndex < staminaValues.length - 1 ? staminaValues[currentRankIndex + 1] : staminaValue;
        double nextRankStaminaRechargeRateValue = currentRankIndex < staminaRechargeRateValues.length - 1 ? staminaRechargeRateValues[currentRankIndex + 1] : staminaRechargeRateValue;
        double staminaValue2 = staminaValues2[currentRankIndex];
        double staminaRechargeRateValue2 = staminaRechargeRateValues2[currentRankIndex];
        double manaValue = manaValues[currentRankIndex];
        double manaRechargeRateValue = manaRechargeRateValues[currentRankIndex];
        double nextRankStaminaValue2 = currentRankIndex < staminaValues2.length - 1 ? staminaValues2[currentRankIndex + 1] : staminaValue2;
        double nextRankStaminaRechargeRateValue2 = currentRankIndex < staminaRechargeRateValues2.length - 1 ? staminaRechargeRateValues2[currentRankIndex + 1] : staminaRechargeRateValue2;
        double nextRankManaValue = currentRankIndex < manaValues.length - 1 ? manaValues[currentRankIndex + 1] : manaValue;
        double nextRankManaRechargeRateValue = currentRankIndex < manaRechargeRateValues.length - 1 ? manaRechargeRateValues[currentRankIndex + 1] : manaRechargeRateValue;
        boolean isShiftPressed = Screen.hasShiftDown();
        boolean barbarianActive = new PathOfTheBarbarianSubclass().isActive(player);
        boolean juggernautActive = new PathOfTheJuggernautSubclass().isActive(player);
        boolean paladinActive = new PathOfThePaladinSubclass().isActive(player);

        tooltip.add(Component.translatable("talents.stamina_mastery")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.stat")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));

        if (!barbarianActive && !juggernautActive && !paladinActive) {
            if (currentPoints == 0) {
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" +2.0/4.0/6.0/8.0/10.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.literal("+5.0%/7.0%/10.0%/15.0%/25.0% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate")));
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("talents.subclass.paladin")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                        .append(Component.translatable("talents.subclass_exclusive")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))));
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" +1.0/2.0/3.0/4.0/5.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.literal("+2.5%/5.0%/8.5%/12.0%/16.25% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                        .append(Component.literal(" +10.0/20.0/30.0/40.0/60.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.literal("+2.5%/5.0%/8.5%/12.0%/16.25% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (paladinActive) {
            if (currentPoints == 0) {
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" +1.0/2.0/3.0/4.0/5.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.literal("+2.5%/5.0%/8.5%/12.0%/16.25% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                        .append(Component.literal(" +10.0/20.0/30.0/40.0/60.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.literal("+2.5%/5.0%/8.5%/12.0%/16.25% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else if (currentPoints < maxPoints) {
                Component stamina2Component = Component.literal(" +" + (isShiftPressed ? nextRankStaminaValue2 : staminaValue2) + " ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                Component staminaRecharge2Component = Component.literal("+" + (isShiftPressed ? nextRankStaminaRechargeRateValue2 : staminaRechargeRateValue2) + "% ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                Component manaComponent = Component.literal(" +" + (isShiftPressed ? nextRankManaValue : manaValue) + " ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                Component manaRechargeComponent = Component.literal("+" + (isShiftPressed ? nextRankManaRechargeRateValue : manaRechargeRateValue) + "% ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(stamina2Component)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(staminaRecharge2Component)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                        .append(manaComponent)
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(manaRechargeComponent)
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                if (!isShiftPressed) {
                    tooltip.add(Component.translatable("talents.press_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                } else {
                    tooltip.add(Component.translatable("talents.release_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                }
            } else if (currentPoints == maxPoints) {
                Component stamina2Component = Component.literal(" +" + staminaValue2 + " ");
                Component staminaRecharge2Component = Component.literal("+" + staminaRechargeRateValue2 + "% ");
                Component manaComponent = Component.literal(" +" + manaValue + " ");
                Component manaRechargeComponent = Component.literal("+" + manaRechargeRateValue + "% ");
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(stamina2Component)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(staminaRecharge2Component)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                        .append(manaComponent)
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(manaRechargeComponent)
                        .append(Component.translatable("talents.mana")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                tooltip.add(Component.translatable("talents.max_rank")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        } else if (barbarianActive || juggernautActive){
            if (currentPoints == 0) {
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" +2.0/4.0/6.0/8.0/10.0 ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.literal("+5.0%/7.0%/10.0%/15.0%/25.0% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            } else if (currentPoints < maxPoints) {
                Component staminaComponent = Component.literal(" +" + (isShiftPressed ? nextRankStaminaValue : staminaValue) + " ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                Component staminaRechargeComponent = Component.literal("+" + (isShiftPressed ? nextRankStaminaRechargeRateValue : staminaRechargeRateValue) + "% ")
                        .withStyle(isShiftPressed ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) : Style.EMPTY);
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(staminaComponent)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(staminaRechargeComponent)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate"))
                );
                if (!isShiftPressed) {
                    tooltip.add(Component.translatable("talents.press_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                } else {
                    tooltip.add(Component.translatable("talents.release_shift")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
                }
            } else if (currentPoints == maxPoints) {
                Component staminaComponent = Component.literal(" +" + staminaValue + " ");
                Component staminaRechargeComponent = Component.literal("+" + staminaRechargeRateValue + "% ");
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(staminaComponent)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(staminaRechargeComponent)
                        .append(Component.translatable("talents.stamina")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000"))))
                        .append(Component.translatable("talents.recharge_rate")));
                tooltip.add(Component.translatable("talents.max_rank")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
            }
        }
            return tooltip;
        }
    }

package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.CapstoneTalentButton;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuardiansOathTalentNode extends CapstoneTalentButton {
    public GuardiansOathTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/talent_icons/wordofhonor_icon.png");
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        double currentPoints = getCurrentPoints();
        Player player = Minecraft.getInstance().player;
        GuardiansOathTalent talent = new GuardiansOathTalent();

        boolean isAltPressed = Screen.hasAltDown();
        double guardianResistance = talent.getResistance() * -1;
        double guardianArmorToughness = talent.getArmorToughness();
        double guardianHealingEffect = talent.getHealingEffectiveness() * 100;
        double guardianKnockbackResist = talent.getKnockbackResistance() * 100;
        double guardianHealth = talent.getMaxHealth();

        double effectArmor = talent.getArmor();
        double effectHeal = talent.getHealPoints();
        double effectAbsorb = talent.getAbsorbPoints();
        double effectMaxAbsorb = talent.getMaxAbsorbPoints();

        double bastionHealingEffect = talent.getBastionHealingEffectiveness() * 100;
        double bastionResistance = talent.getBastionDamageResistance() * -1;
        double bastionArmor = talent.getArmor();
        double bastionMaxAbsorb = talent.getBastionAbsorption();
        double bastionHeal = talent.getBastionHealPerSecond();
        double bastionDuration = talent.getBastionHealPerSecond();

        assert player != null;
        double cooldownReduction = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get());
        double baseCooldown = talent.getCooldownSeconds();
        double adjustedCooldown = baseCooldown * (cooldownReduction / 100.0);

        double baseCost = 0.0;
        Map<String, Integer> costs = talent.getResourceCosts(player);
        if (costs.containsKey("stamina")) {
            baseCost = costs.get("stamina").doubleValue();
        }

        tooltip.add(Component.translatable("talents.guardians_oath")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.capstone")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        if (currentPoints == 0) {
            if(!isAltPressed) {
                tooltip.add(Component.translatable("talents.guardians_oath.description1")
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.guardians_oath")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#E1A63B"))))
                        .append(Component.translatable("talents.guardians_oath.description2"))
                        .append(Component.literal(effectArmor + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.armor_points"))
                        .append(Component.translatable("talents.guardians_oath.description3"))
                        .append(Component.literal(effectAbsorb + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.guardians_oath.description4"))
                        .append(Component.literal(effectMaxAbsorb + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.max_points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.guardians_oath.description5"))
                        .append(Component.literal(effectHeal + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.health_points"))
                        .append(Component.translatable("talents.guardians_oath.description6"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                );
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("talents.talent_type.stat")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))));
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" "))
                        .append(Component.literal("+" + guardianResistance + "%")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.damage_resistance"))
                        .append(Component.translatable("along_with"))
                        .append(Component.literal(" "))
                        .append(Component.literal("+" + guardianArmorToughness)
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.armor_toughness"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + guardianHealingEffect + "%")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.healing_effectiveness"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + guardianKnockbackResist + "%")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.knockback_resistance"))
                        .append(Component.literal(", and "))
                        .append(Component.literal("+" + guardianHealth)
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.health_points"))
                );
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.press_alt")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
            } else {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("talents.word_of_honor")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.talent_type.active")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))));
                tooltip.add(Component.translatable("talents.cost_stamina")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                        .append(Component.literal(String.valueOf(baseCost))
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.cooldown")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                                .append(Component.literal(String.format("%.1f", adjustedCooldown) + "s")
                                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        ));
                tooltip.add(Component.translatable("talents.word_of_honor.description1")
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.turtlerpgclasses.bastion")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD169"))))
                        .append(Component.translatable("talents.word_of_honor.description2"))
                        .append(Component.literal(bastionMaxAbsorb + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("for"))
                        .append(Component.literal(" "))
                        .append(Component.literal(bastionDuration + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("seconds"))
                        .append(Component.translatable("talents.word_of_honor.description3"))
                        .append(Component.literal(bastionHeal + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.health_points"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("per_second"))
                        .append(Component.translatable("talents.word_of_honor.description4"))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.word_of_honor.description5"))
                        .append(Component.translatable("effect.turtlerpgclasses.bastion")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD169"))))
                        .append(Component.translatable("talents.word_of_honor.description6"))
                        .append(Component.literal("+" + bastionArmor + " ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.armor_points"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + bastionResistance + "% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.damage_resistance"))
                        .append(Component.literal(", and "))
                        .append(Component.literal("+" + bastionHealingEffect + "% ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                        .append(Component.translatable("talents.healing_effectiveness"))
                        .append(Component.translatable("talents.word_of_honor.description7"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.minecraft.mining_fatigue")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4A4217"))))
                        .append(Component.literal(" III ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4A4217"))))
                        .append(Component.translatable("and"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.minecraft.slowness")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#5A6C81"))))
                        .append(Component.literal(" II ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#5A6C81"))))
                        .append(Component.translatable("talents.word_of_honor.description8"))
                );
                tooltip.add(Component.translatable("talents.not_learned")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.release_alt")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
            }
        } else if (currentPoints == maxPoints){
            if(!isAltPressed) {
                tooltip.add(Component.translatable("talents.guardians_oath.description1")
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.guardians_oath")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#E1A63B"))))
                        .append(Component.translatable("talents.guardians_oath.description2"))
                        .append(Component.literal(effectArmor + " "))
                        .append(Component.translatable("talents.armor_points"))
                        .append(Component.translatable("talents.guardians_oath.description3"))
                        .append(Component.literal(effectAbsorb + " "))
                        .append(Component.translatable("talents.points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.guardians_oath.description4"))
                        .append(Component.literal(effectMaxAbsorb + " "))
                        .append(Component.translatable("talents.max_points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.guardians_oath.description5"))
                        .append(Component.literal(effectHeal + " "))
                        .append(Component.translatable("talents.health_points"))
                        .append(Component.translatable("talents.guardians_oath.description6"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                );
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("talents.talent_type.stat")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))));
                tooltip.add(Component.translatable("talents.player_gains")
                        .append(Component.literal(" "))
                        .append(Component.literal("+" + guardianResistance + "%"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.damage_resistance"))
                        .append(Component.translatable("along_with"))
                        .append(Component.literal(" "))
                        .append(Component.literal("+" + guardianArmorToughness))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.armor_toughness"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + guardianHealingEffect + "%"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.healing_effectiveness"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + guardianKnockbackResist + "%"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.knockback_resistance"))
                        .append(Component.literal(", and "))
                        .append(Component.literal("+" + guardianHealth))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.health_points"))
                );
                tooltip.add(Component.translatable("talents.max_rank")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.press_alt")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
            } else {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("talents.word_of_honor")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.talent_type.active")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))));
                tooltip.add(Component.translatable("talents.cost_stamina")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                        .append(Component.literal(String.valueOf(baseCost)))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.cooldown")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                                .append(Component.literal(String.format("%.1f", adjustedCooldown) + "s"))
                        ));
                tooltip.add(Component.translatable("talents.word_of_honor.description1")
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.turtlerpgclasses.bastion")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD169"))))
                        .append(Component.translatable("talents.word_of_honor.description2"))
                        .append(Component.literal(bastionMaxAbsorb + " "))
                        .append(Component.translatable("talents.points_of"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.literal(" "))
                        .append(Component.translatable("for"))
                        .append(Component.literal(" "))
                        .append(Component.literal(bastionDuration + " "))
                        .append(Component.translatable("seconds"))
                        .append(Component.translatable("talents.word_of_honor.description3"))
                        .append(Component.literal(bastionHeal + " "))
                        .append(Component.translatable("talents.health_points"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("per_second"))
                        .append(Component.translatable("talents.word_of_honor.description4"))
                        .append(Component.translatable("talents.absorption")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFFF55"))))
                        .append(Component.translatable("talents.word_of_honor.description5"))
                        .append(Component.translatable("effect.turtlerpgclasses.bastion")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD169"))))
                        .append(Component.translatable("talents.word_of_honor.description6"))
                        .append(Component.literal("+" + bastionArmor + " "))
                        .append(Component.translatable("talents.armor_points"))
                        .append(Component.literal(", "))
                        .append(Component.literal("+" + bastionResistance + "% "))
                        .append(Component.translatable("talents.damage_resistance"))
                        .append(Component.literal(", and "))
                        .append(Component.literal("+" + bastionHealingEffect + "% "))
                        .append(Component.translatable("talents.healing_effectiveness"))
                        .append(Component.translatable("talents.word_of_honor.description7"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.minecraft.mining_fatigue")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4A4217"))))
                        .append(Component.literal(" III ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4A4217"))))
                        .append(Component.translatable("and"))
                        .append(Component.literal(" "))
                        .append(Component.translatable("effect.minecraft.slowness")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#5A6C81"))))
                        .append(Component.literal(" II ")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#5A6C81"))))
                        .append(Component.translatable("talents.word_of_honor.description8"))
                );
                tooltip.add(Component.translatable("talents.max_rank")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                        .append(Component.literal(" "))
                        .append(Component.translatable("talents.release_alt")
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
            }
        }
        return tooltip;
    }
}

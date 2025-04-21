package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.CapstoneTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.WarlordsPresenceTalent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarlordsPresenceTalentNode extends CapstoneTalentButton {
    public WarlordsPresenceTalentNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
        this.talentClass = talentClass;
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        if (talentClass instanceof WarlordsPresenceTalent talent) {
            List<Component> tooltip = new ArrayList<>();
            double currentPoints = getCurrentPoints();
            Player player = Minecraft.getInstance().player;

            boolean isAltPressed = Screen.hasAltDown();
            double warlordAttack = talent.getAttackDamage();
            double warlordCritChance = talent.getCriticalChance();
            double warlordCritDamage = talent.getCriticalDamage() * 100;
            double warlordMovespeed = talent.getMovementSpeed() * 100;
            double warlordHealth = talent.getMaxHealth();

            double warlordEffectAttack = talent.getEffectAttack();
            double warlordEffectAttackSpeed = talent.getEffectAttackSpeed() * 100;

            double warlordRallyDuration = talent.getRallyDuration();

            double wrathStunDuration = talent.getWrathStunDuration();
            double wrathDuration = talent.getDurationSeconds();

            assert player != null;
            double cooldownReduction = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get());
            double baseCooldown = talent.getCooldownSeconds();
            double adjustedCooldown = baseCooldown * (cooldownReduction / 100.0);

            double baseCost = 0.0;
            Map<String, Integer> costs = talent.getResourceCosts(player);
            if (costs.containsKey("stamina")) {
                baseCost = costs.get("stamina").doubleValue();
            }


            tooltip.add(Component.translatable("talents.warlords_presence")//Change this value for each talent
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                    .append(Component.literal(" "))
                    .append(Component.translatable("talents.talent_type.capstone")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                    ));
            if (currentPoints == 0) {
                if (!isAltPressed) {
                    tooltip.add(Component.translatable("talents.warlords_presence.description1")
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#EA4D30"))))
                            .append(Component.translatable("talents.warlords_presence.description2"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordEffectAttack))
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.melee_damage"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("and"))
                            .append(Component.literal(" "))
                            .append(Component.literal(warlordEffectAttackSpeed + "%")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.attack_speed"))
                            .append(Component.translatable("talents.warlords_presence.description3"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence.description4"))
                            .append(Component.translatable("effect.turtlerpgclasses.rally")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#DD3030"))))
                            .append(Component.translatable("talents.warlords_presence.description5"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.spell_effects.invulnerability")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                            .append(Component.translatable("talents.warlords_presence.description6"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordRallyDuration))
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence.description7"))
                    );
                    tooltip.add(Component.literal(" "));
                    tooltip.add(Component.translatable("talents.talent_type.stat")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))));
                    tooltip.add(Component.translatable("talents.player_gains")
                            .append(Component.literal(" "))
                            .append(Component.literal("+" + warlordAttack)
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.melee_damage"))
                            .append(Component.translatable("along_with"))
                            .append(Component.literal(" "))
                            .append(Component.literal("+" + warlordCritChance + "%")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.critical_hit_chance"))
                            .append(Component.literal(", "))
                            .append(Component.literal("+" + warlordCritDamage + "%")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.critical_hit_damage"))
                            .append(Component.literal(", "))
                            .append(Component.literal("+" + warlordMovespeed + "%")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.movement_speed"))
                            .append(Component.literal(", and "))
                            .append(Component.literal("+" + warlordHealth)
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
                    tooltip.add(Component.translatable("talents.wrath_of_the_warlord")
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
                    tooltip.add(Component.translatable("talents.wrath_of_the_warlord.description1")
                            .append(Component.translatable("effect.turtlerpgclasses.rally")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#DD3030"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description2"))
                            .append(Component.translatable("talents.spell_effects.invulnerability")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description3"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordRallyDuration))
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds"))
                            .append(Component.literal(". "))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description4"))
                            .append(Component.translatable("talents.wrath_of_the_warlord")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#B03030"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description5"))
                            .append(Component.translatable("talents.spell_effect.bleed")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description6"))
                            .append(Component.translatable("effect.turtlecore.stunned")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#71C7FF"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("for"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(wrathStunDuration))
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds"))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description7"))
                            .append(Component.literal(String.valueOf(wrathDuration))
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds")));
                    tooltip.add(Component.translatable("talents.not_learned")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.release_alt")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
                }
            } else if (currentPoints == maxPoints) {
                if (!isAltPressed) {
                    tooltip.add(Component.translatable("talents.warlords_presence.description1")
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#EA4D30"))))
                            .append(Component.translatable("talents.warlords_presence.description2"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordEffectAttack)))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.melee_damage"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("and"))
                            .append(Component.literal(" "))
                            .append(Component.literal(warlordEffectAttackSpeed + "%"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.attack_speed"))
                            .append(Component.translatable("talents.warlords_presence.description3"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence.description4"))
                            .append(Component.translatable("effect.turtlerpgclasses.rally")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#DD3030"))))
                            .append(Component.translatable("talents.warlords_presence.description5"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.spell_effects.invulnerability")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                            .append(Component.translatable("talents.warlords_presence.description6"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordRallyDuration)))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.warlords_presence.description7"))
                    );
                    tooltip.add(Component.literal(" "));
                    tooltip.add(Component.translatable("talents.talent_type.stat")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080"))));
                    tooltip.add(Component.translatable("talents.player_gains")
                            .append(Component.literal(" "))
                            .append(Component.literal("+" + warlordAttack))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.melee_damage"))
                            .append(Component.translatable("along_with"))
                            .append(Component.literal(" "))
                            .append(Component.literal("+" + warlordCritChance + "%"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.critical_hit_chance"))
                            .append(Component.literal(", "))
                            .append(Component.literal("+" + warlordCritDamage + "%"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.critical_hit_damage"))
                            .append(Component.literal(", "))
                            .append(Component.literal("+" + warlordMovespeed + "%"))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.movement_speed"))
                            .append(Component.literal(", and "))
                            .append(Component.literal("+" + warlordHealth))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.health_points"))
                    );
                    tooltip.add(Component.translatable("talents.max_rank")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.press_alt")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
                } else {
                    tooltip.add(Component.translatable("talents.wrath_of_the_warlord")
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
                    tooltip.add(Component.translatable("talents.wrath_of_the_warlord.description1")
                            .append(Component.translatable("effect.turtlerpgclasses.rally")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#DD3030"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description2"))
                            .append(Component.translatable("talents.spell_effects.invulnerability")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description3"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(warlordRallyDuration)))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds"))
                            .append(Component.literal(". "))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description4"))
                            .append(Component.translatable("talents.wrath_of_the_warlord")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#B03030"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description5"))
                            .append(Component.translatable("talents.spell_effect.bleed")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555"))))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description6"))
                            .append(Component.translatable("effect.turtlecore.stunned")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#71C7FF"))))
                            .append(Component.literal(" "))
                            .append(Component.translatable("for"))
                            .append(Component.literal(" "))
                            .append(Component.literal(String.valueOf(wrathStunDuration)))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds"))
                            .append(Component.translatable("talents.wrath_of_the_warlord.description7"))
                            .append(Component.literal(String.valueOf(wrathDuration)))
                            .append(Component.literal(" "))
                            .append(Component.translatable("seconds")));
                    tooltip.add(Component.translatable("talents.max_rank")
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))
                            .append(Component.literal(" "))
                            .append(Component.translatable("talents.release_alt")
                                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555")))));
                }
            }
            return tooltip;
        }
        return null;
    }
}


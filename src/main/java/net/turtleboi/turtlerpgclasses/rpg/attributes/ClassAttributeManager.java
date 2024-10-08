package net.turtleboi.turtlerpgclasses.rpg.attributes;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.capabilities.PlayerClassProvider;
import net.turtleboi.turtlerpgclasses.init.ModAttributes;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.network.packet.ClassSelectionS2CPacket;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.classes.Ranger;
import net.turtleboi.turtlerpgclasses.rpg.classes.Mage;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.EfficientEnergyTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.EvasiveManeuversTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.RenownedHunterTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.WeakPointsTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.WarlordsPresenceTalent;

import java.util.*;

public class ClassAttributeManager {
    private static final String warrior = Component.translatable("class.warrior.name").getString();
    private static final String ranger = Component.translatable("class.ranger.name").getString();
    private static final String mage = Component.translatable("class.mage.name").getString();

    private static final String barbarian = Component.translatable("subclass.barbarian.name").getString();
    private static final String juggernaut = Component.translatable("subclass.juggernaut.name").getString();
    private static final String paladin = Component.translatable("subclass.paladin.name").getString();

    public static void applyClassAttributes(Player player) {
        Warrior warriorClass = new Warrior();
        if (warriorClass.isActive(player)) {
            warriorClass.applyAttributes(player);
        } else {
            warriorClass.removeModifier(player);
        }

        Ranger rangerClass = new Ranger();
        if (rangerClass.isActive(player)) {
            rangerClass.applyAttributes(player);
        } else {
            rangerClass.removeModifier(player);
        }

        Mage mageClass = new Mage();
        if (mageClass.isActive(player)) {
            mageClass.applyAttributes(player);
        } else {
            mageClass.removeModifier(player);
        }
    }

    public static void applyTalentAttributes(Player player) {
        VigorTalent vigorTalent = new VigorTalent();
        if (vigorTalent.getPoints(player) >= 1) {
            vigorTalent.applyAttributes(player);
        } else if (vigorTalent.getPoints(player) == 0) {
            vigorTalent.removeModifier(player);
        }

        MightyBlowsTalent mightyBlowsTalent = new MightyBlowsTalent();
        if (mightyBlowsTalent.getPoints(player) >= 1){
            mightyBlowsTalent.applyAttributes(player);
        } else if (mightyBlowsTalent.getPoints(player) == 0) {
            mightyBlowsTalent.removeModifier(player);
        }

        BattleHardenedTalent battleHardenedTalent = new BattleHardenedTalent();
        if (battleHardenedTalent.getPoints(player) >= 1){
            battleHardenedTalent.applyAttributes(player);
        } else if (battleHardenedTalent.getPoints(player) == 0) {
            battleHardenedTalent.removeModifier(player);
        }

        SteadyFootingTalent steadyFootingTalent = new SteadyFootingTalent();
        if (steadyFootingTalent.getPoints(player) >= 1) {
            steadyFootingTalent.applyAttributes(player);
        } else if (steadyFootingTalent.getPoints(player) == 0) {
            steadyFootingTalent.removeModifier(player);
        }

        SwiftHandsTalent swiftHandsTalent = new SwiftHandsTalent();
        if (swiftHandsTalent.getPoints(player) >= 1) {
            swiftHandsTalent.applyAttributes(player);
        } else if (swiftHandsTalent.getPoints(player) == 0) {
            swiftHandsTalent.removeModifier(player);
        }

        PathOfTheBarbarianSubclass pathOfTheBarbarianSubclass = new PathOfTheBarbarianSubclass();
        PathOfTheJuggernautSubclass pathOfTheJuggernautSubclass = new PathOfTheJuggernautSubclass();
        PathOfThePaladinSubclass pathOfThePaladinSubclass = new PathOfThePaladinSubclass();

        if (pathOfTheBarbarianSubclass.isActive(player)) {
            player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                String subclass = PathOfTheBarbarianSubclass.getSubclassName();
                playerClass.setRpgSubclass(subclass);
                ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(playerClass.getRpgClass(), subclass), (ServerPlayer) player);
            });
        } else if (pathOfTheJuggernautSubclass.isActive(player)) {
            player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                String subclass = PathOfTheJuggernautSubclass.getSubclassName();
                playerClass.setRpgSubclass(subclass);
                ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(playerClass.getRpgClass(), subclass), (ServerPlayer) player);
            });
        } else if (pathOfThePaladinSubclass.isActive(player)) {
            player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                String subclass = PathOfThePaladinSubclass.getSubclassName();
                playerClass.setRpgSubclass(subclass);
                ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(playerClass.getRpgClass(), subclass), (ServerPlayer) player);
            });
            pathOfThePaladinSubclass.applyAttributes(player);
        } else {
            player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                playerClass.setRpgSubclass(null);
                ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(playerClass.getRpgClass(), null), (ServerPlayer) player);
            });
            pathOfThePaladinSubclass.removeModifier(player);
        }

        MarathonerTalent marathonerTalent = new MarathonerTalent();
        if (marathonerTalent.getPoints(player) >= 1){
            marathonerTalent.applyAttributes(player);
        } else if (marathonerTalent.getPoints(player) == 0) {
            marathonerTalent.removeModifier(player);
        }

        StaminaMasteryTalent staminaMasteryTalent = new StaminaMasteryTalent();
        if (staminaMasteryTalent.getPoints(player) >= 1) {
            staminaMasteryTalent.applyAttributes(player);
        } else if (staminaMasteryTalent.getPoints(player) == 0) {
            staminaMasteryTalent.removeModifier(player);
        }

        QuickRecoveryTalent quickRecoveryTalent = new QuickRecoveryTalent();
        if (quickRecoveryTalent.getPoints(player) >= 1){
            quickRecoveryTalent.applyAttributes(player);
        } else if (quickRecoveryTalent.getPoints(player) == 0) {
            quickRecoveryTalent.removeModifier(player);
        }

        LifeLeechTalent lifeLeechTalent = new LifeLeechTalent();
        if (lifeLeechTalent.getPoints(player) >= 1){
            lifeLeechTalent.applyAttributes(player);
        } else if (lifeLeechTalent.getPoints(player) == 0) {
            lifeLeechTalent.removeModifier(player);
        }

        BrawlersTenacityTalent brawlersTenacityTalent = new BrawlersTenacityTalent();
        if (brawlersTenacityTalent.getPoints(player) >= 1){
            //brawlersTenacityTalent.applyValues(player);
        } else if (brawlersTenacityTalent.getPoints(player) == 0) {
            brawlersTenacityTalent.removeEffectModifier(player, "brawlerstenacity");
        }

        CombatVeteranTalent combatVeteranTalent = new CombatVeteranTalent();
        if (combatVeteranTalent.getPoints(player) >= 1){
            combatVeteranTalent.applyAttributes(player);
        } else if (combatVeteranTalent.getPoints(player) == 0) {
            combatVeteranTalent.removeModifier(player);
        }

        WarlordsPresenceTalent warlordsPresenceTalent = new WarlordsPresenceTalent();
        if (warlordsPresenceTalent.getPoints(player) >= 1){
            warlordsPresenceTalent.applyAttributes(player);
        } else if (warlordsPresenceTalent.getPoints(player) == 0) {
            warlordsPresenceTalent.removeModifier(player);
        }

        GuardiansOathTalent guardiansOathTalent = new GuardiansOathTalent();
        if (guardiansOathTalent.getPoints(player) >= 1){
            guardiansOathTalent.applyAttributes(player);
        } else if (guardiansOathTalent.getPoints(player) == 0) {
            guardiansOathTalent.removeModifier(player);
        }

        WeakPointsTalent weakPointsTalent = new WeakPointsTalent();
        if (weakPointsTalent.getPoints(player) >= 1) {
            weakPointsTalent.applyAttributes(player);
        } else if (weakPointsTalent.getPoints(player) == 0) {
            weakPointsTalent.removeModifier(player);
        }

        LethalityTalent lethalityTalent = new LethalityTalent();
        if (lethalityTalent.getPoints(player) >= 1) {
            lethalityTalent.applyAttributes(player);
        } else if (lethalityTalent.getPoints(player) == 0) {
            lethalityTalent.removeModifier(player);
        }

        EvasiveManeuversTalent evasiveManeuversTalent = new EvasiveManeuversTalent();
        if (evasiveManeuversTalent.getPoints(player) >= 1) {
            evasiveManeuversTalent.applyAttributes(player);
        } else if (evasiveManeuversTalent.getPoints(player) == 0) {
            evasiveManeuversTalent.removeModifier(player);
        }

        EfficientEnergyTalent efficientEnergyTalent = new EfficientEnergyTalent();
        if (efficientEnergyTalent.getPoints(player) >= 1) {
            efficientEnergyTalent.applyAttributes(player);
        } else if (efficientEnergyTalent.getPoints(player) == 0) {
            efficientEnergyTalent.removeModifier(player);
        }

        RenownedHunterTalent renownedHunterTalent = new RenownedHunterTalent();
        if (renownedHunterTalent.getPoints(player) >= 1) {
            renownedHunterTalent.applyAttributes(player);
        } else if (renownedHunterTalent.getPoints(player) == 0) {
            renownedHunterTalent.removeModifier(player);
        }
    }

    private static final List<Attribute> RPG_ATTRIBUTES = Arrays.asList(
            Attributes.MAX_HEALTH,
            Attributes.MOVEMENT_SPEED,
            Attributes.ATTACK_DAMAGE,
            Attributes.ATTACK_SPEED,
            Attributes.ARMOR,
            Attributes.ARMOR_TOUGHNESS,
            Attributes.KNOCKBACK_RESISTANCE,
            CoreAttributes.RANGED_DAMAGE.get(),
            ModAttributes.MAX_STAMINA.get(),
            ModAttributes.MAX_ENERGY.get(),
            ModAttributes.MAX_MANA.get(),
            ModAttributes.STAMINA_RECHARGE.get(),
            ModAttributes.ENERGY_RECHARGE.get(),
            ModAttributes.MANA_RECHARGE.get(),
            CoreAttributes.CRITICAL_DAMAGE.get(),
            CoreAttributes.CRITICAL_CHANCE.get(),
            CoreAttributes.DAMAGE_RESISTANCE.get(),
            CoreAttributes.LIFE_STEAL.get(),
            CoreAttributes.HEALING_EFFECTIVENESS.get(),
            CoreAttributes.ARMOR_PENETRATION.get(),
            CoreAttributes.DODGE_CHANCE.get()
    );

    public static void resetAttributes(Player player) {
        for (Attribute attribute : RPG_ATTRIBUTES) {
            AttributeInstance attributeInstance = player.getAttribute(attribute);
            if (attributeInstance != null) {
                Collection<AttributeModifier> modifiers = new ArrayList<>(attributeInstance.getModifiers());
                for (AttributeModifier modifier : modifiers) {
                    if (modifier.getName().startsWith("RPGClass_")) {
                        attributeInstance.removeModifier(modifier.getId());
                    }
                    if (modifier.getName().startsWith("RPGTalents_")) {
                        attributeInstance.removeModifier(modifier.getId());
                    }
                }
            }
        }
    }
}
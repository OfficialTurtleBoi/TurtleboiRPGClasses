package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbilityProvider;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BrawlersTenacityTalent;
import org.jetbrains.annotations.NotNull;

public class BrawlersTenacityEffect extends MobEffect {

    public BrawlersTenacityEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        BrawlersTenacityTalent brawlersTenacityTalent = new BrawlersTenacityTalent();
        if (entity instanceof Player player) {
            int talentPoints = brawlersTenacityTalent.getPoints(player);
            double finalArmorValue = Math.min(
                    brawlersTenacityTalent.getArmorIncrease(talentPoints) * (amplifier + 1),
                    brawlersTenacityTalent.getMaxArmor(talentPoints)
            );
            brawlersTenacityTalent.applyEffectModifier(player,
                    Attributes.ARMOR,
                    brawlersTenacityTalent.getEffectAttributeName("Armor", "brawlerstenacity"),
                    finalArmorValue,
                    AttributeModifier.Operation.ADDITION);
            //player.sendSystemMessage(Component.literal("Armor bonus! Additional armor: " + finalArmorValue)); //debug code
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            new BrawlersTenacityTalent().removeEffectModifier(player, "brawlerstenacity");
            player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
            //player.sendSystemMessage(Component.literal("Damage bonus lost...")); //debug code
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }

    public static void updateEffectDuration(Player player, int duration) {
        if (player.hasEffect(ModEffects.BRAWLERS_TENACITY.get())) {
            MobEffectInstance effectInstance = player.getEffect(ModEffects.BRAWLERS_TENACITY.get());
            if (effectInstance != null) {
                effectInstance.update(new MobEffectInstance(ModEffects.BRAWLERS_TENACITY.get(), duration, effectInstance.getAmplifier()));
            }
        } else {
            player.addEffect(new MobEffectInstance(ModEffects.BRAWLERS_TENACITY.get(), duration, 0));
        }
    }
}

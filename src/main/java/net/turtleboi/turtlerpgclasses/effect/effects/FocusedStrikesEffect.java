package net.turtleboi.turtlerpgclasses.effect.effects;

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
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbility;
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbilityProvider;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.FocusedStrikesTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BrawlersTenacityTalent;
import org.jetbrains.annotations.NotNull;

public class FocusedStrikesEffect extends MobEffect {

    public FocusedStrikesEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        FocusedStrikesTalent focusedStrikesTalent = new FocusedStrikesTalent();
        if (entity instanceof Player player) {
            int talentPoints = focusedStrikesTalent.getPoints(player);
            double finalAttackValue = Math.min(
                    focusedStrikesTalent.getDamageIncrease(talentPoints) * (amplifier + 1),
                    focusedStrikesTalent.getDamageMaximum(talentPoints)
            );
            focusedStrikesTalent.applyEffectModifier(player,
                    Attributes.ATTACK_DAMAGE,
                    focusedStrikesTalent.getEffectAttributeName("AttackDamage", "focusedstrikes"),
                    finalAttackValue,
                    AttributeModifier.Operation.ADDITION);
            //player.sendSystemMessage(Component.literal("Attack bonus! Additional attack: " + finalAttackValue)); //debug code
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            new FocusedStrikesTalent().removeEffectModifier(player, "focusedstrikes");
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }

    public static void updateEffectDuration(Player player, int duration) {
        if (player.hasEffect(ModEffects.FOCUSED_STRIKES.get())) {
            MobEffectInstance effectInstance = player.getEffect(ModEffects.FOCUSED_STRIKES.get());
            if (effectInstance != null) {
                effectInstance.update(new MobEffectInstance(ModEffects.FOCUSED_STRIKES.get(), duration, effectInstance.getAmplifier()));
            }
        } else {
            player.addEffect(new MobEffectInstance(ModEffects.FOCUSED_STRIKES.get(), duration, 0));
        }
    }
}

package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;
import org.jetbrains.annotations.NotNull;

public class GuardiansOathEffect extends MobEffect {
    private final GuardiansOathTalent talent = new GuardiansOathTalent();

    public GuardiansOathEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            new GuardiansOathTalent().applyEffectAttributes(player);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            new GuardiansOathTalent().removeEffectModifier(player, "guardiansOath");
        }
    }

    public static void onEnemyDefeated(Player player, Entity defeatedEntity, GuardiansOathTalent talent) {
        if (defeatedEntity.getType().getCategory() == MobCategory.MONSTER) {
            int healAmount = (int) talent.getHealPoints();
            int absorptionPoints = (int) talent.getAbsorbPoints();
            int maxAbsorptionPoints = (int) talent.getMaxAbsorbPoints();

            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(healAmount);
                float newAbsorption = Math.min(player.getAbsorptionAmount() + absorptionPoints, maxAbsorptionPoints);
                player.setAbsorptionAmount(newAbsorption);
            } else {
                float newAbsorption = Math.min(player.getAbsorptionAmount() + absorptionPoints, maxAbsorptionPoints);
                player.setAbsorptionAmount(newAbsorption);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public static void updateEffectDuration(Player player, int duration) {
        if (player.hasEffect(ModEffects.GUARDIANS_OATH.get())) {
            MobEffectInstance effectInstance = player.getEffect(ModEffects.GUARDIANS_OATH.get());
            if (effectInstance != null) {
                effectInstance.update(new MobEffectInstance(ModEffects.GUARDIANS_OATH.get(), duration, effectInstance.getAmplifier(), true, true, true));
            }
        } else {
            player.addEffect(new MobEffectInstance(ModEffects.GUARDIANS_OATH.get(), duration, 0, true, true, true));
        }
    }
}

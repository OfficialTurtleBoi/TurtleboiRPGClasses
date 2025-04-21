package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbilityProvider;
import org.jetbrains.annotations.NotNull;

public class WindedEffect extends MobEffect {
    public WindedEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility ->{
                if (!playerAbility.isSecondWindTriggered()) {
                    playerAbility.setSecondWindTriggered(true);
                }
                int duration = player.getEffect(this).getDuration();
                playerAbility.setWindedDuration(duration);
            });
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility ->{
                playerAbility.setSecondWindTriggered(false);
                playerAbility.setWindedDuration(0);
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }
}

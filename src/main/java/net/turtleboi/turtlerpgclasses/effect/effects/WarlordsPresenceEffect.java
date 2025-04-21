package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.WarlordsPresenceTalent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WarlordsPresenceEffect extends MobEffect {

    public WarlordsPresenceEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            new WarlordsPresenceTalent().applyEffectAttributes(player);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            new WarlordsPresenceTalent().removeEffectModifier(player, "warlordsPresence");
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }

    public static void updateEffectDuration(Player player, int duration) {
        if (player.hasEffect(ModEffects.WARLORDS_PRESENCE.get())) {
            MobEffectInstance effectInstance = player.getEffect(ModEffects.WARLORDS_PRESENCE.get());
            if (effectInstance != null) {
                effectInstance.update(new MobEffectInstance(ModEffects.WARLORDS_PRESENCE.get(), duration, effectInstance.getAmplifier(), true, true, true));
            }
        } else {
            player.addEffect(new MobEffectInstance(ModEffects.WARLORDS_PRESENCE.get(), duration, 0, true, true, true));
        }
    }
}

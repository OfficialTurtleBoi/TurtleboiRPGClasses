package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.ColossusTalent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ColossusEffect extends MobEffect {
    private static final UUID sizeModifierUUID = UUID.fromString("f9c5fd94-fd42-4ef5-bbd0-515b80f1dd39");
    private static final double targetSize = 1.5;
    private static final int growTicks = 20;

    public ColossusEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            player.getActiveEffects().stream()
                    .map(MobEffectInstance::getEffect)
                    .filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL)
                    .forEach(player::removeEffect);
            double currentSize = player.getAttributeValue(CoreAttributes.PLAYER_SIZE.get());
            int remainingTicks = entity.getEffect(this).getDuration();
            double sizeIncrement = (targetSize - 1) / growTicks;

            if (remainingTicks > growTicks && currentSize < targetSize) {
                double newSize = Math.min(currentSize + sizeIncrement, targetSize);
                updatePlayerSize(player, newSize);
            } else if (remainingTicks <= growTicks && currentSize > 1.0) {
                double shrinkIncrement = (targetSize - 1) / remainingTicks;
                double newSize = Math.max(currentSize - shrinkIncrement, 1.0);
                updatePlayerSize(player, newSize);
            }
            new ColossusTalent().applyEffectAttributes(player);
        }
    }

    private void updatePlayerSize(Player player, double newSize) {
        AttributeModifier existingModifier = player.getAttribute(CoreAttributes.PLAYER_SIZE.get()).getModifier(sizeModifierUUID);
        if (existingModifier != null) {
            player.getAttribute(CoreAttributes.PLAYER_SIZE.get()).removeModifier(existingModifier);
        }
        player.getAttribute(CoreAttributes.PLAYER_SIZE.get()).addTransientModifier(
                new AttributeModifier(sizeModifierUUID, "Colossus_Size", newSize - 1, AttributeModifier.Operation.ADDITION)
        );
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            player.getAttribute(CoreAttributes.PLAYER_SIZE.get()).removeModifier(sizeModifierUUID);
            new ColossusTalent().removeEffectModifier(player, "colossus");
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }
}

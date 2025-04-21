package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class StealthedEffect extends MobEffect {
    public StealthedEffect(MobEffectCategory mobEffectCategory, int color){
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (!player.isCrouching()) {
                player.setForcedPose(Pose.CROUCHING);    // Simulate crouch by changing pose
                        // Adjust the player's bounding box
                player.setInvisible(true);
                player.refreshDimensions();// Make the player invisible for stealth
            }
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            player.setForcedPose(Pose.STANDING);         // Reset pose to standing
                      // Restore player's normal bounding box
            player.setInvisible(false);
            player.refreshDimensions();
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier){
        return true;
    }
}

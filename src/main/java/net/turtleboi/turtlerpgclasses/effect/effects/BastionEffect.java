package net.turtleboi.turtlerpgclasses.effect.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.turtleboi.turtlecore.util.PartyUtils;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class BastionEffect extends MobEffect {
    GuardiansOathTalent talent = new GuardiansOathTalent();
    private final int maxAbsorptionPoints = (int) talent.getBastionAbsorption();
    private final int healAmount = (int) talent.getBastionHealPerSecond();

    public BastionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            new GuardiansOathTalent().applyBastionAttributes(player);
            applyAbsorption(player);
            applyHealing(player);
            applyEnemyDebuff(player);
        }
    }

    private void applyAbsorption(Player player) {
        float newAbsorption = Math.min(player.getAbsorptionAmount() + maxAbsorptionPoints, maxAbsorptionPoints);
        player.setAbsorptionAmount(newAbsorption);
    }

    private void applyHealing(Player player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(healAmount);
        } else {
            float newAbsorption = Math.min(player.getAbsorptionAmount() + healAmount, maxAbsorptionPoints);
            player.setAbsorptionAmount(newAbsorption);
        }
    }

    private void applyEnemyDebuff(Player player) {
        double radius = 3.0;
        AABB aabb = new AABB(player.blockPosition()).inflate(radius);
        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, aabb)) {
            MobCategory category = entity.getType().getCategory();
            if (entity != player && !(entity instanceof Player && PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) entity)) && !isAllowedCategory(category)) {
                entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 2));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            }
        }
    }

    public boolean isAllowedCategory(MobCategory category) {
        return getAllowedCategories().contains(category);
    }

    protected Set<MobCategory> getAllowedCategories() {
        return EnumSet.of(
                MobCategory.AMBIENT,
                MobCategory.CREATURE,
                MobCategory.AXOLOTLS,
                MobCategory.UNDERGROUND_WATER_CREATURE,
                MobCategory.WATER_CREATURE
        );
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            new GuardiansOathTalent().removeEffectModifier(player, "bastion");
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}

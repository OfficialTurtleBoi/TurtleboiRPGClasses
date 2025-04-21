package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.turtleboi.turtlecore.util.PartyUtils;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class SecondWindTalent extends Talent {
    private static final String Name = Component.translatable("talents.second_wind").getString();

    @Override
    public String getName() {
        return Name;
    }

    public static double getSpeedBoostValue(int points) {
        double[] speedBoostValues = {0.10, 0.20, 0.30, 0.40, 0.50};
        int currentRankIndex = Math.max(0, Math.min(points - 1, speedBoostValues.length - 1));
        return speedBoostValues[currentRankIndex];
    }

    public static double getHealValue(int points) {
        double[] healValues = {10.0, 12.0, 14.0, 18.0, 24.0};
        int currentRankIndex = Math.max(0, Math.min(points - 1, healValues.length - 1));
        return healValues[currentRankIndex];
    }

    @Override
    public void applyAttributes(Player player) {
        int talentPoints = getPoints(player);
        double healAmount = getHealValue(talentPoints);
        player.heal((float) healAmount);
        player.addEffect(new MobEffectInstance(ModEffects.WINDED.get(), 6000));
        player.addEffect(new MobEffectInstance(ModEffects.SECOND_WIND.get(), 20 * 10, 0));
        applyKnockback(player);
    }

    @Override
    public void applyEffectAttributes(Player player) {
        int talentPoints = getPoints(player);
        double speedBoost = getSpeedBoostValue(talentPoints);
        applyEffectModifier(player,
                Attributes.MOVEMENT_SPEED,
                getEffectAttributeName("MovementSpeed", "secondWind"),
                speedBoost,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<Attribute> getRPGAttributes() {
        return List.of(
        );
    }

    @Override
    public List<Attribute> getRPGEffectAttributes() {
        return List.of(
                Attributes.MOVEMENT_SPEED
        );
    }

    @Override
    protected Set<MobCategory> getAllowedCategories() {
        return EnumSet.of(
                MobCategory.AMBIENT,
                MobCategory.CREATURE,
                MobCategory.AXOLOTLS,
                MobCategory.UNDERGROUND_WATER_CREATURE,
                MobCategory.WATER_CREATURE
        );
    }

    private void applyKnockback(Player player) {
        AABB aabb = new AABB(player.blockPosition()).inflate(3.0);
        List<Entity> entities = player.level().getEntities(player, aabb, entity -> entity instanceof LivingEntity && entity != player);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                MobCategory category = entity.getType().getCategory();
                if (entity instanceof Player ally && PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally)) {
                    continue;
                }

                if (isAllowedCategory(category)) {
                    continue;
                }

                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);

                if (distance < 3.0) {
                    double knockbackX = (dx / distance) * 1.25;
                    double knockbackZ = (dz / distance) * 1.25;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockbackX, 0.5, knockbackZ));
                }
            }
        }
    }
}

package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.turtleboi.turtlecore.effect.effects.BleedEffect;
import net.turtleboi.turtlecore.entity.CoreEntities;
import net.turtleboi.turtlecore.entity.abilities.UnleashFuryEntity;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.entity.ModEntities;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnleashFuryTalent extends ActiveAbility {
    private static final String Name = Component.translatable("talents.subclass.barbarian.unleash_fury").getString();

    @Override
    public ResourceLocation getAbilityIcon() {
        return new ResourceLocation(TurtleRPGClasses.MOD_ID,
                "textures/gui/talents/talent_icons/pathofthebarbariansubclass_icon.png");
    }

    @Override
    public void applyAttributes(Player player) {

    }

    @Override
    public void applyEffectAttributes(Player player) {

    }

    @Override
    public List<Attribute> getRPGAttributes() {
        return List.of();
    }

    @Override
    public List<Attribute> getRPGEffectAttributes() {
        return List.of();
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public Map<String, Integer> getResourceCosts(Player player) {
        Map<String, Integer> costs = new HashMap<>();
        costs.put("stamina", 8);
        return costs;
    }

    @Override
    public int getCooldownSeconds() {
        return 4;
    }

    @Override
    public boolean activate(Player player) {
        double RANGE = 3.0;
        List<Entity> nearbyEntities = player.level().getEntitiesOfClass(Entity.class, new AABB(
                player.getX() - RANGE, player.getY() - RANGE, player.getZ() - RANGE,
                player.getX() + RANGE, player.getY() + RANGE, player.getZ() + RANGE));

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity livingEntity && entity != player && !isAlly(player, livingEntity)) {
                entity.hurt(player.level().damageSources().playerAttack(player), (float) getDamage());
                BleedEffect.applyOrAmplifyBleed(livingEntity, getBleedDurationTicks(), 0, player);
            }
        }
        return true;
    }

    public double getDamage(){
        return 10.0;
    }

    public double getBleedDurationSeconds(){
        return 6.0;
    }

    public int getBleedDurationTicks(){
        return (int) (getBleedDurationSeconds() * 20);
    }

    @Override
    public SoundEvent[] getAbilitySounds() {
        return new SoundEvent[]{SoundEvents.POLAR_BEAR_DEATH, SoundEvents.PLAYER_ATTACK_SWEEP};
    }

    @Override
    public void spawnAbilityEntity(Player player, Level level) {
        UnleashFuryEntity unleashFuryEntity = new UnleashFuryEntity(CoreEntities.UNLEASH_FURY.get(), level);
        unleashFuryEntity.setOwner(player);
        unleashFuryEntity.setYRot(player.getYRot());
        player.level().addFreshEntity(unleashFuryEntity);
    }

    @Override
    public int getDuration() {
        return 0;
    }
}

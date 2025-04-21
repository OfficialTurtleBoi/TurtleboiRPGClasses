package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;

import java.util.*;

public class IntimidatingPresenceTalent extends ActiveAbility {
    private static final String Name = Component.translatable("talents.intimidating_presence").getString();

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
        return Map.of("stamina", 7);
    }

    @Override
    public int getCooldownSeconds() {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        int talentPoints = getPoints(player);
        return (int) getCooldownValue(talentPoints);
    }

    private static double getCooldownValue(int points) {
        double[] cooldownValues = {20.0, 16.0, 12.0};
        int currentRankIndex = Math.max(0, Math.min(points - 1, cooldownValues.length - 1));
        return cooldownValues[currentRankIndex];
    }

    public static int getWeaknessLevel(int points) {
        int[] weaknessLevels = {0, 0, 1};
        int currentRankIndex = Math.max(0, Math.min(points - 1, weaknessLevels.length - 1));
        return weaknessLevels[currentRankIndex];
    }

    public static int getSlownessLevel(int points) {
        int[] slownessLevels = {0, 0, 1};
        int currentRankIndex = Math.max(0, Math.min(points - 1, slownessLevels.length - 1));
        return slownessLevels[currentRankIndex];
    }

    public static int getAbsorptionValue(int points) {
        int[] absorptionValues = {1, 2, 4};
        int currentRankIndex = Math.max(0, Math.min(points - 1, absorptionValues.length - 1));
        return absorptionValues[currentRankIndex];
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

    private static final Map<UUID, Integer> playerAbsorptionTimers = new HashMap<>();
    private static final Map<UUID, Integer> playerAbsorptionAmounts = new HashMap<>();

    @Override
    public boolean activate(Player player) {
        int talentPoints = getPoints(player);
        int debuffDuration = 300;
        int enemiesAffected = 0;

        int weaknessLevel = getWeaknessLevel(talentPoints);
        int slownessLevel = getSlownessLevel(talentPoints);
        int absorptionPerEnemy = getAbsorptionValue(talentPoints);

        double radius = 10.0;
        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius));

        for (LivingEntity target : targets) {
            MobCategory category = target.getType().getCategory();
            if (target != player && !isAlly(player, target) && !isAllowedCategory(category)) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, debuffDuration, weaknessLevel));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, debuffDuration, slownessLevel));
                target.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
                if (target instanceof Mob mobTarget) {
                    mobTarget.goalSelector.addGoal(
                            0, new NearestAttackableTargetGoal<>(
                                    mobTarget,
                                    Player.class,
                                    1,
                                    false,
                                    false,
                                    (targetPlayer) -> targetPlayer.equals(player)));
                    mobTarget.setTarget(player);
                }
                enemiesAffected++;
            }
        }

        if (enemiesAffected > 0) {
            int absorptionDuration = enemiesAffected * 200;
            int totalAbsorption = enemiesAffected * absorptionPerEnemy;
            float currentAbsorption = player.getAbsorptionAmount();
            player.setAbsorptionAmount(currentAbsorption + totalAbsorption);
            playerAbsorptionTimers.put(player.getUUID(), absorptionDuration);
            playerAbsorptionAmounts.put(player.getUUID(), totalAbsorption);
            //player.sendSystemMessage(Component.literal("Intimidating Presence activated. Enemies affected: " + enemiesAffected)); //Debug code
            return true;
        } else {
            player.sendSystemMessage(Component.literal("No enemies in range"));
            return false;
        }
    }

    @Override
    public SoundEvent[] getAbilitySounds() {
        return new SoundEvent[0];
    }

    @Override
    public void spawnAbilityEntity(Player player, Level level) {

    }

    @Override
    public int getDuration() {
        return getCooldownSeconds() * 20;
    }

    public static void tickPlayer(Player player) {
        UUID playerId = player.getUUID();
        if (playerAbsorptionTimers.containsKey(playerId)) {
            int ticksLeft = playerAbsorptionTimers.get(playerId);
            if (ticksLeft > 0) {
                playerAbsorptionTimers.put(playerId, ticksLeft - 1);
            } else {
                int totalAbsorption = playerAbsorptionAmounts.getOrDefault(playerId, 0);
                playerAbsorptionTimers.remove(playerId);
                playerAbsorptionAmounts.remove(playerId);
                float currentAbsorption = player.getAbsorptionAmount();
                player.setAbsorptionAmount(Math.max(0, currentAbsorption - totalAbsorption));
            }
        }
    }
}

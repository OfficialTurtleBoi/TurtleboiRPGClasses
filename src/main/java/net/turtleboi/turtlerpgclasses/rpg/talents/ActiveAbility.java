package net.turtleboi.turtlerpgclasses.rpg.talents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.turtleboi.turtlecore.capabilities.party.PlayerPartyProvider;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.capabilities.resources.PlayerResource;

import java.util.HashMap;
import java.util.Map;

public abstract class ActiveAbility extends Talent {
    private static final Map<String, Long> abilityCooldowns = new HashMap<>();
    private static final Map<String, Boolean> resetCooldownFlags = new HashMap<>();
    private static final Map<String, Long> durationEndTimes = new HashMap<>();

    public abstract String getName();
    public abstract Map<String, Integer> getResourceCosts(Player player);
    public abstract int getCooldownSeconds();
    public abstract boolean activate(Player player);
    public abstract SoundEvent[] getAbilitySounds();
    public abstract void spawnAbilityEntity(Player player, Level level);
    public abstract int getDuration(); // Duration in ticks, override in subclass for duration abilities
    public double getDurationSeconds(){
        return (double) getDuration() / 20;
    }

    private String getAbilityKey(Player player) {
        return player.getUUID() + ":" + getName();
    }

    public long getAbilityCooldownTime(Player player) {
        String key = getAbilityKey(player);
        return Math.max(0, abilityCooldowns.getOrDefault(key, 0L) - player.level().getGameTime());
    }

    public double getCooldownProgress(Player player) {
        long remainingTicks = getAbilityCooldownTime(player);
        double cooldownReduction = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get());
        return Math.min(1.0, remainingTicks / ((getCooldownSeconds() * 20) * (cooldownReduction / 100.0)));
    }

    public boolean abilityIsOffCooldown(Player player) {
        return getAbilityCooldownTime(player) <= 0;
    }

    public void setAbilityOnCooldown(Player player, int baseCooldownTicks) {
        double cooldownReductionPercentage = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get()) / 100.0;
        int adjustedCooldown = (int) (baseCooldownTicks * cooldownReductionPercentage);
        String key = getAbilityKey(player);
        abilityCooldowns.put(key, player.level().getGameTime() + adjustedCooldown);
        resetCooldownFlags.put(key, false);
    }

    public void resetAbilityCooldown(Player player) {
        String key = getAbilityKey(player);
        abilityCooldowns.put(key, player.level().getGameTime());
    }

    public void setCooldownResetFlag(Player player, boolean value) {
        String key = getAbilityKey(player);
        resetCooldownFlags.put(key, value);
    }

    public boolean getCooldownResetFlag(Player player) {
        String key = getAbilityKey(player);
        return resetCooldownFlags.getOrDefault(key, false);
    }

    public void setAbilityOnDuration(Player player, int durationTicks) {
        String key = getAbilityKey(player);
        durationEndTimes.put(key, player.level().getGameTime() + durationTicks);
    }

    public long getAbilityDurationTime(Player player) {
        String key = getAbilityKey(player);
        return Math.max(0, durationEndTimes.getOrDefault(key, 0L) - player.level().getGameTime());
    }

    public boolean isDurationActive(Player player) {
        return getAbilityDurationTime(player) > 0;
    }

    public void sendCooldownOrResourceMessage(Player player, long cooldownTime, Map<String, Integer> resourceCosts, Map<String, Integer> currentResources) {
        for (Map.Entry<String, Integer> entry : resourceCosts.entrySet()) {
            String resource = entry.getKey();
            double cost = entry.getValue();
            double current = currentResources.getOrDefault(resource, 0);

            if (current < cost) {
                player.sendSystemMessage(Component.literal("Not enough " + resource));
            } else if (cooldownTime > 0) {
                MutableComponent cooldownMessage = Component.literal("Ability is on cooldown ")
                        .append(Component.literal(String.format("%.1f", cooldownTime / 20.0) + "s").withStyle(ChatFormatting.AQUA))
                        .append(" remain");
                player.sendSystemMessage(cooldownMessage);
            }
        }
    }

    public ResourceLocation getAbilityIcon() {
        return getIconTexture();
    }

    public static boolean isAlly(Player player, Entity entity) {
        if (entity instanceof Player otherPlayer) {
            return player.getCapability(PlayerPartyProvider.PLAYER_PARTY).map(party ->
                    party.getPartyMembers().contains(otherPlayer.getUUID())).orElse(false);
        }
        return false;
    }

    public void playAbilitySound(Player player, Level level) {
        for (SoundEvent soundEvent : getAbilitySounds()) {
            level.playSound(null, player.blockPosition(), soundEvent, SoundSource.PLAYERS, 1.25F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public void useAbility(ServerPlayer player, PlayerResource playerResource) {
        long cooldownTime = getAbilityCooldownTime(player);
        Map<String, Integer> resourceCosts = getResourceCosts(player);
        int cooldownTicks = getCooldownSeconds() * 20;
        int duration = getDuration();

        boolean hasEnoughResources = true;
        Map<String, Integer> currentResources = new HashMap<>();
        currentResources.put("stamina", playerResource.getStamina());
        currentResources.put("mana", playerResource.getMana());
        currentResources.put("energy", playerResource.getEnergy());

        for (Map.Entry<String, Integer> entry : resourceCosts.entrySet()) {
            String resource = entry.getKey();
            double cost = entry.getValue();
            double current = currentResources.getOrDefault(resource, 0);
            if (current < cost) {
                hasEnoughResources = false;
                break;
            }
        }

        if (!player.isCreative()) {
            if (hasEnoughResources && abilityIsOffCooldown(player)) {
                boolean activated = activate(player);
                if (activated) {
                    playAbilitySound(player, player.level());
                    spawnAbilityEntity(player, player.level());
                    player.sendSystemMessage(Component.literal("Used " + getName()));
                    for (Map.Entry<String, Integer> entry : resourceCosts.entrySet()) {
                        String resource = entry.getKey();
                        int cost = entry.getValue();
                        if (resource.equals("stamina")) {
                            playerResource.subStamina(cost);
                        } else if (resource.equals("mana")) {
                            playerResource.subMana(cost);
                        } else if (resource.equals("energy")) {
                            playerResource.subEnergy(cost);
                        }
                    }
                    setAbilityOnCooldown(player, cooldownTicks);
                    if (duration > 0) {
                        setAbilityOnDuration(player, duration);
                    }
                }
            } else {
                sendCooldownOrResourceMessage(player, cooldownTime, resourceCosts, currentResources);
            }
        } else {
            if (abilityIsOffCooldown(player)) {
                boolean activated = activate(player);
                if (activated) {
                    playAbilitySound(player, player.level());
                    spawnAbilityEntity(player, player.level());
                    player.sendSystemMessage(Component.literal("Used " + getName()));
                    setAbilityOnCooldown(player, 20); // Reduced cooldown for creative mode
                    if (duration > 0) {
                        setAbilityOnDuration(player, duration);
                    }
                }
            } else {
                sendCooldownOrResourceMessage(player, cooldownTime, resourceCosts, currentResources);
            }
        }
    }

    public static void resetAllAbilityCooldowns() {
        abilityCooldowns.clear();
        resetCooldownFlags.clear();
        durationEndTimes.clear();
    }
}

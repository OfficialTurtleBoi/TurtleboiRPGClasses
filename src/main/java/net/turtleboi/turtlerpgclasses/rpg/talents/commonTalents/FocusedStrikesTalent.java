package net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbilityProvider;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FocusedStrikesTalent extends Talent {
    private static final String Name = Component.translatable("talents.focused_strikes").getString();

    @Override
    public String getName() {
        return Name;
    }

    public double getDamageIncrease(int points) {
        int[] damageValues = {1, 1, 2, 2, 3};
        int currentRankIndex = Math.max(0, Math.min(points - 1, damageValues.length - 1));
        return damageValues[currentRankIndex];
    }

    public double getHitThreshold(int points) {
        int[] hitThresholds = {4, 3, 3, 2, 2};
        int currentRankIndex = Math.max(0, Math.min(points - 1, hitThresholds.length - 1));
        return hitThresholds[currentRankIndex];
    }

    public double getDamageMaximum(int points) {
        int[] damageMaximumValues = {4, 6, 8, 12, 15};
        int currentRankIndex = Math.max(0, Math.min(points - 1, damageMaximumValues.length - 1));
        return damageMaximumValues[currentRankIndex];
    }

    public int getMaxUpgrades(Player player) {
        int talentsPoints = getPoints(player);
        return (int) (getDamageMaximum(talentsPoints)/getDamageIncrease(talentsPoints));
    }

    @Override
    public void applyAttributes(Player player) {

    }

    @Override
    public void applyEffectAttributes(Player player) {

    }

    @Override
    public List<Attribute> getRPGAttributes() {
        return List.of(

        );
    }

    @Override
    public List<Attribute> getRPGEffectAttributes() {
        return List.of(
                Attributes.ATTACK_DAMAGE
        );
    }

    private static final float basePitch = 1.0f;
    private static final float pitchIncrement = 1.05946f;

    public void playHitSound(Player player, int currentBonus) {
        float pitch = basePitch * (float) Math.pow(pitchIncrement, currentBonus);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, pitch);
    }

    public void playUpgradeSound(Player player, int currentBonus) {
        float pitch = basePitch * (float) Math.pow(pitchIncrement, currentBonus);
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, pitch);
    }
}

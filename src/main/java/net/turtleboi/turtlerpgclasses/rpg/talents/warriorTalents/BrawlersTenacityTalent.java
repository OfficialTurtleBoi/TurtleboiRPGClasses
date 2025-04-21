package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.List;

public class BrawlersTenacityTalent extends Talent {
    private static final String Name = Component.translatable("talents.brawlers_tenacity").getString();

    @Override
    public String getName() {
        return Name;
    }

    public double getArmorIncrease(int points) {
        int[] armorValues = {1, 1, 2, 2, 3};
        int currentRankIndex = Math.max(0, Math.min(points - 1, armorValues.length - 1));
        return armorValues[currentRankIndex];
    }

    public double getMaxArmor(int points) {
        int[] maxArmorValues = {3, 5, 8, 12, 18};
        int currentRankIndex = Math.max(0, Math.min(points - 1, maxArmorValues.length - 1));
        return maxArmorValues[currentRankIndex];
    }

    public int getMaxHits(Player player) {
        int talentsPoints = getPoints(player);
        return (int) (getMaxArmor(talentsPoints)/getArmorIncrease(talentsPoints));
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
        return List.of(
                Attributes.ARMOR
        );
    }

    private static final float basePitch = 1.0f;
    private static final float pitchIncrement = 1.05946f;

    public void playEffectSound(Player player, int currentBonus) {
        float pitch = basePitch * (float)Math.pow(pitchIncrement, currentBonus);
        player.level().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_CHAIN, SoundSource.PLAYERS, 0.5f, pitch);
    }
}

package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.init.ModAttributes;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;

import java.util.List;

public class LifeLeechTalent extends Talent {
    private static final String Name = Component.translatable("talents.life_leech").getString();

    @Override
    public String getName() {
        return Name;
    }

    public static double getLifeLeechValue(int points) {
        double[] leechValues = {2.5, 5.0, 10.0, 15.0, 20.0};
        int currentRankIndex = Math.max(0, Math.min(points - 1, leechValues.length - 1));
        return leechValues[currentRankIndex];
    }

    @Override
    public void applyAttributes(Player player) {
        int talentPoints = getPoints(player);
        double lifeLeechValue = getLifeLeechValue(talentPoints);

        applyModifier(player,
                CoreAttributes.LIFE_STEAL.get(),
                getAttributeName("LifeSteal"),
                lifeLeechValue,
                AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectAttributes(Player player) {

    }

    @Override
    public List<Attribute> getRPGAttributes() {
        return List.of(
                CoreAttributes.LIFE_STEAL.get()
        );
    }

    @Override
    public List<Attribute> getRPGEffectAttributes() {
        return List.of();
    }
}

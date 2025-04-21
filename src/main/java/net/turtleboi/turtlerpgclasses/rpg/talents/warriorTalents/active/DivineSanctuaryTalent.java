package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.turtleboi.turtlecore.entity.CoreEntities;
import net.turtleboi.turtlecore.entity.abilities.SanctuaryDomeEntity;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.entity.ModEntities;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DivineSanctuaryTalent extends ActiveAbility {
    private static final String Name = Component.translatable("talents.subclass.paladin.divine_sanctuary").getString();

    @Override
    public ResourceLocation getAbilityIcon() {
        return new ResourceLocation(TurtleRPGClasses.MOD_ID,
                "textures/gui/talents/talent_icons/pathofthepaladinsubclass_icon.png");
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
        costs.put("mana", 50);
        return costs;
    }

    @Override
    public int getCooldownSeconds() {
        return 60;
    }

    @Override
    public boolean activate(Player player) {
        return true;
    }

    @Override
    public SoundEvent[] getAbilitySounds() {
        return new SoundEvent[]{SoundEvents.PLAYER_LEVELUP};
    }

    @Override
    public void spawnAbilityEntity(Player player, Level level) {
        SanctuaryDomeEntity domeEntity = new SanctuaryDomeEntity(CoreEntities.SANCTUARY_DOME.get(), level);
        domeEntity.setOwner(player);
        domeEntity.setPos(player.getX(), player.getY(), player.getZ());
        domeEntity.setYRot(player.getYRot());
        player.level().addFreshEntity(domeEntity);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.GLOW, player.getX(), player.getY(), player.getZ(), 100, 1.0, 1.0, 1.0, 0.1);
        }
    }

    @Override
    public int getDuration() {
        return 240;
    }

    public double getHeal(){
        return 3.0;
    }
}

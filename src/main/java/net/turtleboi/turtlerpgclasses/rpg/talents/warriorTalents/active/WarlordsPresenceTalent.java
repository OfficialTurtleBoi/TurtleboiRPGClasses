package net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlecore.util.PartyUtils;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.effect.effects.WarlordsPresenceEffect;
import net.turtleboi.turtlerpgclasses.init.ModAttributes;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarlordsPresenceTalent extends ActiveAbility {
    private static final String Name = Component.translatable("talents.wrath_of_the_warlord").getString();

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public Map<String, Integer> getResourceCosts(Player player) {
        Map<String, Integer> costs = new HashMap<>();
        costs.put("stamina", 10);
        return costs;
    }

    @Override
    public int getCooldownSeconds() {
        return 90;
    }

    @Override
    public boolean activate(Player player) {
        player.addEffect(new MobEffectInstance(ModEffects.WRATH.get(), getDuration(), 0));
        Level level = player.level();
        AABB aabb = new AABB(player.blockPosition()).inflate(getWrathRadius());
        int rallyDuration = (int) getRallyDuration() * 20;
        level.getEntitiesOfClass(Player.class, aabb).forEach(ally -> {
            if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) || ally == player) {
                if (!ally.hasEffect(ModEffects.RALLY.get())) {
                    ally.addEffect(new MobEffectInstance(ModEffects.RALLY.get(), rallyDuration, 0));
                }
            }
        });
        return true;
    }

    public double getWarlordsRadius(){
        return 5.0;
    }

    public double getWrathRadius(){
        return 10.0;
    }

    public void applyWarlordsPresenceEffects(Player player) {
        Level level = player.level();
        AABB aabb = new AABB(player.blockPosition()).inflate(getWarlordsRadius());

        level.getEntitiesOfClass(Player.class, aabb).forEach(ally -> {
            if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) || ally == player) {
                if (!ally.hasEffect(ModEffects.WARLORDS_PRESENCE.get())) {
                    ally.addEffect(new MobEffectInstance(ModEffects.WARLORDS_PRESENCE.get(), 60, 0, true, true, true));
                } else {
                    WarlordsPresenceEffect.updateEffectDuration(player, 60);
                }
            }
        });
    }

    @Override
    public SoundEvent[] getAbilitySounds() {
        return new SoundEvent[]{SoundEvents.POLAR_BEAR_DEATH};
    }

    @Override
    public void spawnAbilityEntity(Player player, Level level) {

    }

    @Override
    public int getDuration() {
        return 300;
    }

    public double getAttackDamage() {
        return 3.0;
    }

    public double getCriticalChance() {
        return 20.0;
    }

    public double getCriticalDamage() {
        return 0.40;
    }

    public double getMovementSpeed() {
        return 0.20;
    }

    public double getMaxHealth() {
        return 10.0;
    }

    public double getEffectAttack() {
        return 2.0;
    }

    public double getEffectAttackSpeed() {
        return 0.10;
    }

    public double getRallyDuration() {
        return 5.0;
    }

    public double getRallyAttack() {
        return 0.5;
    }

    public double getRallyMoveSpeed() {
        return 0.25;
    }

    public double getWrathStunDuration(){ return 2.0;}

    @Override
    public void applyAttributes(Player player) {
        applyModifier(player,
                Attributes.ATTACK_DAMAGE,
                getAttributeName("AttackDamage"),
                getAttackDamage(),
                AttributeModifier.Operation.ADDITION);
        applyModifier(player,
                CoreAttributes.CRITICAL_CHANCE.get(),
                getAttributeName("CriticalChance"),
                getCriticalChance(),
                AttributeModifier.Operation.ADDITION);
        applyModifier(player,
                CoreAttributes.CRITICAL_DAMAGE.get(),
                getAttributeName("CriticalDamage"),
                getCriticalDamage(),
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        applyModifier(player,
                Attributes.MOVEMENT_SPEED,
                getAttributeName("MovementSpeed"),
                getMovementSpeed(),
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        applyModifier(player,
                Attributes.MAX_HEALTH,
                getAttributeName("Health"),
                getMaxHealth(),
                AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectAttributes(Player player) {
        applyEffectModifier(player,
                Attributes.ATTACK_DAMAGE,
                getEffectAttributeName("Attack", "warlordsPresence"),
                getEffectAttack(),
                AttributeModifier.Operation.ADDITION);
        applyEffectModifier(player,
                Attributes.ATTACK_SPEED,
                getEffectAttributeName("AttackSpeed", "warlordsPresence"),
                getEffectAttackSpeed(),
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public void applyRallyAttributes(Player player) {
        applyEffectModifier(player,
                Attributes.ATTACK_DAMAGE,
                getEffectAttributeName("Attack", "rally"),
                getRallyAttack(),
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        applyEffectModifier(player,
                Attributes.MOVEMENT_SPEED,
                getEffectAttributeName("MovementSpeed", "rally"),
                getRallyMoveSpeed(),
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<Attribute> getRPGAttributes() {
        return Arrays.asList(
                Attributes.ATTACK_DAMAGE,
                Attributes.MOVEMENT_SPEED,
                Attributes.MAX_HEALTH,
                CoreAttributes.CRITICAL_CHANCE.get(),
                CoreAttributes.CRITICAL_DAMAGE.get()
        );
    }

    @Override
    public List<Attribute> getRPGEffectAttributes() {
        return Arrays.asList(
                Attributes.ATTACK_DAMAGE,
                Attributes.ATTACK_SPEED,
                Attributes.MOVEMENT_SPEED
        );
    }
}

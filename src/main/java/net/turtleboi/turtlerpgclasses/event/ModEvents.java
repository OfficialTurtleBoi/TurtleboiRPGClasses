package net.turtleboi.turtlerpgclasses.event;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.turtleboi.turtlecore.capabilities.targeting.PlayerTargetingProvider;
import net.turtleboi.turtlecore.effect.CoreEffects;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlecore.util.PartyUtils;
import net.turtleboi.turtlecore.util.TargetingUtils;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.capabilities.talents.PlayerAbilityProvider;
import net.turtleboi.turtlerpgclasses.capabilities.PlayerClassProvider;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStatesProvider;
import net.turtleboi.turtlerpgclasses.capabilities.resources.PlayerResourceProvider;
import net.turtleboi.turtlerpgclasses.client.ui.cooldowns.CooldownOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.resources.ResourceOverlay;
import net.turtleboi.turtlerpgclasses.effect.effects.*;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.network.packet.ClassSelectionS2CPacket;
import net.turtleboi.turtlerpgclasses.network.packet.OpenClassSelectionScreenPacket;
import net.turtleboi.turtlerpgclasses.network.packet.resources.PlayerResourcesS2CPacket;
import net.turtleboi.turtlerpgclasses.rpg.attributes.ClassAttributeManager;
import net.turtleboi.turtlerpgclasses.rpg.classes.Mage;
import net.turtleboi.turtlerpgclasses.rpg.classes.Ranger;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.FocusedStrikesTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.EvasiveManeuversTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.QuickDrawTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.SteadyBreathingTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.VineWhipTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.BrawlersTenacityTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfThePaladinSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.SecondWindTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.*;
import net.turtleboi.turtlerpgclasses.util.EffectUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TurtleRPGClasses.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!event.getObject().getCapability(PlayerClassProvider.PLAYER_RPGCLASS).isPresent()) {
                event.addCapability(new ResourceLocation(TurtleRPGClasses.MOD_ID, "player_class"), new PlayerClassProvider());
            }
            if (!event.getObject().getCapability(TalentStatesProvider.TALENT_STATES).isPresent()) {
                event.addCapability(new ResourceLocation(TurtleRPGClasses.MOD_ID, "talent_states"), new TalentStatesProvider());
            }
            if (!event.getObject().getCapability(PlayerResourceProvider.PLAYER_RESOURCE).isPresent()) {
                event.addCapability(new ResourceLocation(TurtleRPGClasses.MOD_ID, "player_resource"), new PlayerResourceProvider(player));
            }
            if (!event.getObject().getCapability(PlayerAbilityProvider.PLAYER_ABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(TurtleRPGClasses.MOD_ID, "player_ability"), new PlayerAbilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(oldStore ->
                    event.getEntity().getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(oldStore ->
                    event.getEntity().getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(oldStore ->
                    event.getEntity().getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(PlayerResourceProvider.PLAYER_RESOURCE).ifPresent(oldStore ->
                    event.getEntity().getCapability(PlayerResourceProvider.PLAYER_RESOURCE).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(talentStates -> {
            CompoundTag compound = new CompoundTag();
            talentStates.saveNBTData(compound);
            player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(newTalentStates -> {
                newTalentStates.loadNBTData(compound);
            });
        });
        if (player.level().isClientSide()) {
            CooldownOverlay.initializeSlots(player);
            ResourceOverlay.initializeResourceBars(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        player.reviveCaps();
        player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(talentStates -> {
            CompoundTag compound = new CompoundTag();
            talentStates.saveNBTData(compound);
            player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(newTalentStates -> {
                newTalentStates.loadNBTData(compound);
            });
        });
        player.invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass ->
                        ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(playerClass.getRpgClass(), playerClass.getRpgSubclass()), player));
                player.getCapability(PlayerResourceProvider.PLAYER_RESOURCE).ifPresent(playerResource ->
                        ModNetworking.sendToPlayer(
                        new PlayerResourcesS2CPacket(
                                playerResource.getMaxStamina(),
                                playerResource.getMaxEnergy(),
                                playerResource.getMaxMana(),
                                playerResource.getStamina(),
                                playerResource.getEnergy(),
                                playerResource.getMana()), player));
                CooldownOverlay.initializeSlots(player);
                ResourceOverlay.initializeResourceBars(player);

                player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                    String className = playerClass.getRpgClass();
                    switch (className) {
                        case "Warrior":
                            if (!new Warrior().isActive(player)) {
                                TalentStates.resetAllTalents(player);
                                new Warrior().setActive(player);
                                break;
                            }
                            break;
                        case "Ranger":
                            if (!new Ranger().isActive(player)) {
                                TalentStates.resetAllTalents(player);
                                new Ranger().setActive(player);
                                break;
                            }
                            break;
                        case "Mage":
                            if (!new Mage().isActive(player)) {
                                TalentStates.resetAllTalents(player);
                                new Mage().setActive(player);
                                break;
                            }
                            break;
                    }
                });
                //player.sendSystemMessage(Component.literal("Thank you downloading ")
                //        .append(Component.literal("TurtleBoi's RPG Classes")
                //                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B"))))
                //        .append(Component.literal("! The mod recently updated it's core and you may experience some bugs if you" +
                //                " had previously downloaded version 0.1.0. If Talent Trees are unavailable, use the command /tbrpg resetTalents" +
                //                " command, or simply leave and rejoin the world. If issues persists, don't hesitate to leave a bug report on our CurseForge page.")));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorldClassCheck(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                    String currentClass = playerClass.getRpgClass();
                    if(currentClass == null || currentClass.equals("No Class")) {
                        TalentStates.resetAllTalents(player);
                        ActiveAbility.resetAllAbilityCooldowns();
                        ModNetworking.sendToPlayer(new OpenClassSelectionScreenPacket(), player);
                    }
                });
                CooldownOverlay.initializeSlots(player);
                ResourceOverlay.initializeResourceBars(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity targetEntity = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();
        if (targetEntity instanceof Player player) {
            if (player.hasEffect(ModEffects.STEEL_BARBS.get()) && sourceEntity instanceof LivingEntity attacker) {
                float thornsDamage = 1.0F + (player.getArmorValue() / 3.0F);
                attacker.hurt(player.level().damageSources().thorns(player), thornsDamage);
            }

            SecondWindTalent secondWindTalent = new SecondWindTalent();
            if (secondWindTalent.isActive(player)) {
                player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    if (player.getHealth() - event.getAmount() <= 0) {
                        if (!player.hasEffect(ModEffects.WINDED.get())) {
                            secondWindTalent.applyAttributes(player);
                            event.setCanceled(true);
                        }
                    }
                });
            }

            BrawlersTenacityTalent brawlersTenacityTalent = new  BrawlersTenacityTalent();
            if (brawlersTenacityTalent.isActive(player)) {
                player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    int hitCounter = playerAbility.getBrawlerHitCounter();

                    if (!player.hasEffect(ModEffects.BRAWLERS_TENACITY.get())) {
                        player.addEffect(new MobEffectInstance(ModEffects.BRAWLERS_TENACITY.get(), 120, 0));
                        playerAbility.setBrawlerHitCounter(1);
                    } else if (player.hasEffect(ModEffects.BRAWLERS_TENACITY.get())) {
                        // Increment armor bonus on subsequent hits
                        if (hitCounter < brawlersTenacityTalent.getMaxHits(player)) {
                            playerAbility.incrementBrawlerHitCounter();
                            player.addEffect(new MobEffectInstance(ModEffects.BRAWLERS_TENACITY.get(), 120, hitCounter));
                        } else if (hitCounter >= brawlersTenacityTalent.getMaxHits(player)){
                            // Max armor reached
                            BrawlersTenacityEffect.updateEffectDuration(player, 120);
                        }
                    }
                    brawlersTenacityTalent.playEffectSound(player, hitCounter);
                });
            }

            WarlordsPresenceTalent warlordsPresenceTalent = new WarlordsPresenceTalent();
            if (warlordsPresenceTalent.isActive(player)) {
                Level level = player.level();
                AABB warlordAABB = new AABB(player.blockPosition()).inflate(warlordsPresenceTalent.getWarlordsRadius());
                AABB wrathAABB = new AABB(player.blockPosition()).inflate(warlordsPresenceTalent.getWrathRadius());

                level.getEntitiesOfClass(Player.class, warlordAABB).forEach(ally -> {
                    if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) && ally != player) {
                        float damage = event.getAmount();
                        player.hurt(player.level().damageSources().generic(), damage * 0.5f);
                        event.setAmount(damage * 0.5f);
                    }
                    if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) || ally == player){
                        if (player.getHealth() <= player.getMaxHealth() * 0.3) {
                            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility ->{
                                if (!ally.hasEffect(ModEffects.DEFEATED.get()) && !playerAbility.isRallyTriggered()) {
                                    ally.addEffect(new MobEffectInstance(ModEffects.RALLY.get(), 20 * 5, 0));
                                    ally.addEffect(new MobEffectInstance(ModEffects.DEFEATED.get(), 20 * 120, 0));
                                }
                            });
                        }
                    }
                });
                level.getEntitiesOfClass(Player.class, wrathAABB).forEach(ally -> {
                    if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) || ally == player){
                        if (player.hasEffect(ModEffects.WRATH.get())) {
                            float damage = event.getAmount();
                            event.setAmount(damage * 0.5f);
                            if (sourceEntity != null) {
                                sourceEntity.hurt(player.level().damageSources().generic(), damage * 0.25f);
                            }
                        }
                    }
                });
            }

            if (player.hasEffect(ModEffects.RALLY.get())) {
                event.setCanceled(true);
            }

            EvasiveManeuversTalent evasiveManeuversTalent = new EvasiveManeuversTalent();
            if (evasiveManeuversTalent.isActive(player)) {
                player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    if (!player.hasEffect(ModEffects.EVASIVE_MANEUVERS.get())) {
                        player.addEffect(new MobEffectInstance(ModEffects.EVASIVE_MANEUVERS.get(), 100, 0));
                        AttributeInstance dodgeChanceAttribute = player.getAttribute(CoreAttributes.DODGE_CHANCE.get());
                        if (dodgeChanceAttribute != null) {
                            double dodgeChance = dodgeChanceAttribute.getValue();
                            //player.sendSystemMessage(Component.literal("Dodge chance increased! Current dodge chance: " + dodgeChance + "%")); // debug code
                        } else {
                            //player.sendSystemMessage(Component.literal("Dodge chance attribute not found!")); // debug code
                        }
                    } else if (player.hasEffect(ModEffects.EVASIVE_MANEUVERS.get())) {
                        EvasiveManeuversEffect.updateEffectDuration(player, 100);
                    }
                });
            }

            SteadyBreathingTalent steadyBreathingTalent = new SteadyBreathingTalent();
            if (steadyBreathingTalent.isActive(player)) {
                player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    playerAbility.resetSteadyBreathing();
                    if (player.hasEffect(ModEffects.STEADY_BREATHING.get())) {
                        player.removeEffect(ModEffects.STEADY_BREATHING.get());
                    }
                });
            }
        }

        if (sourceEntity instanceof Player player) {
             //Logic for if player hurts something, but that's also handled by onPlayerAttack
            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                SteadyBreathingTalent steadyBreathingTalent = new SteadyBreathingTalent();
                if (steadyBreathingTalent.isActive(player)) {
                    if (!player.hasEffect(ModEffects.STEADY_BREATHING.get()) && playerAbility.getSteadyBreathingStacks() < 1) {
                        playerAbility.resetSteadyBreathing();
                        if (player.hasEffect(ModEffects.STEADY_BREATHING.get())) {
                            player.removeEffect(ModEffects.STEADY_BREATHING.get());
                        }
                    } else if (player.hasEffect(ModEffects.STEADY_BREATHING.get())){
                        int talentPoints = steadyBreathingTalent.getPoints(player);
                        int damageStacks = playerAbility.getSteadyBreathingStacks();
                        double damageBonus = steadyBreathingTalent.getDamage(talentPoints) * damageStacks;
                        event.setAmount((float) (event.getAmount() + damageBonus));
                        player.sendSystemMessage(Component.literal("Consumed Steady Breathing stacks! Damage bonus: " + damageBonus)); //debug code
                        player.removeEffect(ModEffects.STEADY_BREATHING.get());
                        playerAbility.resetSteadyBreathing();
                    }
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerAttack(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity trueSource = source.getEntity();
        if (!(trueSource instanceof Player player)) return;

        LivingEntity target = event.getEntity();
        if (!(target instanceof LivingEntity)) return;

        //Focused Strikes Talent Logic
        FocusedStrikesTalent focusedStrikesTalent = new FocusedStrikesTalent();
        if (focusedStrikesTalent.isActive(player)) {
            int comboTime = (int) ((player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED) * 20));

            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                int talentPoints = focusedStrikesTalent.getPoints(player);
                int hitCounter = playerAbility.getFocusedStrikesCounter();
                int tierCounter = playerAbility.getFocusedStrikesTier();
                int maxUpgrades = focusedStrikesTalent.getMaxUpgrades(player);
                int upgradeThreshold = (int) focusedStrikesTalent.getHitThreshold(talentPoints);
                int hitsLeft = upgradeThreshold - hitCounter;
                float attackStrength = player.getAttackStrengthScale(0.0F);
                int buffer = 20;

                if (!player.hasEffect(ModEffects.FOCUSED_STRIKES.get())) {
                    playerAbility.setFocusedStrikesCounter(1);
                    playerAbility.setFocusedStrikesTier(1);
                    player.addEffect(new MobEffectInstance(ModEffects.FOCUSED_STRIKES.get(), comboTime + buffer, 0));
                    //player.sendSystemMessage(Component.literal("Combo started! " + hitsLeft + " hits left until next damage bonus")); //Debug code
                } else if (player.hasEffect(ModEffects.FOCUSED_STRIKES.get())) {
                    if (tierCounter < maxUpgrades) {
                        if (attackStrength >= 1.0F) {
                            if (hitCounter < upgradeThreshold) {
                                playerAbility.incrementFocusedStrikesCounter();
                                //player.sendSystemMessage(Component.literal("Combo scored! " + hitsLeft + " hits left until next damage bonus")); //Debug code
                            } else {
                                playerAbility.incrementFocusedStrikesTier();
                                focusedStrikesTalent.playUpgradeSound(player, tierCounter);
                                playerAbility.setFocusedStrikesCounter(1);
                                player.addEffect(new MobEffectInstance(ModEffects.FOCUSED_STRIKES.get(), comboTime + buffer, tierCounter));
                                //player.sendSystemMessage(Component.literal("Damage upgrade! Attack bonus: " + (tierCounter * focusedStrikesTalent.getDamageIncrease(talentPoints)))); //Debug code
                            }
                        } else {
                            playerAbility.setFocusedStrikesCounter(1);
                            FocusedStrikesEffect.updateEffectDuration(player, comboTime + buffer);
                            //player.sendSystemMessage(Component.literal("Too soon... " + hitsLeft + " hits left until next damage bonus")); //Debug code
                        }
                    } else {
                        // Max attack reached
                        FocusedStrikesEffect.updateEffectDuration(player, comboTime + buffer);
                        //player.sendSystemMessage(Component.literal("Max upgrade! Attack bonus: " + (tierCounter * focusedStrikesTalent.getDamageIncrease(talentPoints)))); //Debug code
                    }
                }
            });
        }

        VictoriousCryTalent victoriousCryTalent = new VictoriousCryTalent();
        if (victoriousCryTalent.isActive(player)){
            VictoriousCryTalent.handleDamageBoost(victoriousCryTalent, player, target, event);
        }

        WarlordsPresenceTalent warlordsPresenceTalent = new WarlordsPresenceTalent();
        if (warlordsPresenceTalent.isActive(player)) {
            Level level = player.level();
            AABB wrathAABB = new AABB(player.blockPosition()).inflate(warlordsPresenceTalent.getWrathRadius());

            if (player.hasEffect(ModEffects.WRATH.get())) {
                level.getEntitiesOfClass(Player.class, wrathAABB).forEach(ally -> {
                    if (PartyUtils.isAlly((ServerPlayer) player, (ServerPlayer) ally) || ally == player) {
                        if (target != player && target != ally) {
                            target.addEffect(new MobEffectInstance(CoreEffects.BLEEDING.get(), 20 * 10, 0));
                            Random random = new Random();
                            if (random.nextInt(100) < 20) {
                                target.addEffect(new MobEffectInstance(CoreEffects.STUNNED.get()));
                            }
                        }
                    }
                });
            }
        }

        VineWhipTalent vineWhipTalent = new VineWhipTalent();
        if (vineWhipTalent.isActive(player)){
            player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(ability -> {
                UUID targetUUID = target.getUUID();
                long currentTime = System.currentTimeMillis();
                long lastRootedTime = ability.getLastRootedTime(targetUUID);
                double rootDuration = 20 * vineWhipTalent.getRootDuration(vineWhipTalent.getPoints(player));

                if (currentTime - lastRootedTime >= 15000) {
                    EffectUtils.applyRootedEffect(player, target, (int) rootDuration, 0);
                    ability.setLastRootedTime(targetUUID, currentTime);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance currentRootedEffect = entity.getEffect(CoreEffects.ROOTED.get());
        if (currentRootedEffect != null) {
            UUID playerUUID = EffectUtils.getRootingPlayerUUID(entity);
            if (playerUUID != null) {
                Player player = entity.level().getPlayerByUUID(playerUUID);
                VineWhipTalent vineWhipTalent = new VineWhipTalent();
                if (player != null && vineWhipTalent.isActive(player)) {
                    double rootDuration = 20 * vineWhipTalent.getRootDuration(vineWhipTalent.getPoints(player));
                    int swiftnessAmplifier = vineWhipTalent.getSwiftnessAmplifier(vineWhipTalent.getPoints(player));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int) rootDuration, swiftnessAmplifier));
                    EffectUtils.clearRootingPlayer(entity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseTick(LivingEntityUseItemEvent.Start event) {
        Entity source = event.getEntity();
        //source.sendSystemMessage(Component.literal("Use Item Event triggered for " + source.getName().getString())); //debug code
        if (source instanceof Player player) {
            QuickDrawTalent quickDrawTalent = new QuickDrawTalent();
            //player.sendSystemMessage(Component.literal("Quick Draw is active? " + (quickDrawTalent.isActive(player)))); //debug code
            if (quickDrawTalent.isActive(player)) {
                ItemStack itemStack = event.getItem();
                int currentPoints = quickDrawTalent.getPoints(player);
                double drawSpeedMultiplier = (quickDrawTalent.getDrawSpeed(currentPoints)) / 100;
                //player.sendSystemMessage(Component.literal("Draw speed bonus: " + (quickDrawTalent.getDrawSpeed(currentPoints)))); //debug code

                if (itemStack.getItem() instanceof BowItem) {
                    int useDuration = event.getDuration();
                    int chargeDuration = BowItem.MAX_DRAW_DURATION;
                    //player.sendSystemMessage(Component.literal("Original draw duration: " + useDuration));//debug code
                    int adjustedDuration = (int) (useDuration - (chargeDuration * drawSpeedMultiplier));
                    adjustedDuration = Math.max(1, adjustedDuration); // Ensure duration isn't too low
                    //player.sendSystemMessage(Component.literal("Adjusted draw duration: " + adjustedDuration)); //debug code
                    event.setDuration(adjustedDuration);
                }

                if (itemStack.getItem() instanceof CrossbowItem) {
                    int useDuration = event.getDuration();
                    int chargeDuration = CrossbowItem.getChargeDuration(itemStack);
                    int quickChargeLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack);
                    int quickChargeTicks = 5 * quickChargeLevel;
                    //player.sendSystemMessage(Component.literal("Original draw duration: " + useDuration)); //debug code
                    int adjustedDuration = (int) (useDuration - ((chargeDuration * drawSpeedMultiplier) + (quickChargeTicks * drawSpeedMultiplier)));
                    adjustedDuration = Math.max(1, adjustedDuration);
                    //player.sendSystemMessage(Component.literal("Adjusted draw duration: " + adjustedDuration)); //debug code
                    event.setDuration(adjustedDuration);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        //Player target logic
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity target) {
            Entity source = event.getSource().getEntity();
            if (source instanceof Player player) {
                VictoriousCryTalent victoriousCryTalent = new VictoriousCryTalent();
                if (victoriousCryTalent.isActive(player)) {
                    victoriousCryTalent.onTargetDeath(player, target);
                }

                player.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    if (target == playerAbility.getTargetEntity()){
                        playerAbility.setTaunting(false);
                        playerAbility.setTargetEntity(null);
                        //player.sendSystemMessage(Component.literal("Cleared taunted target! Target: " + playerAbility.getTargetEntity()));
                    }
                });

                if (player.hasEffect(ModEffects.GUARDIANS_OATH.get())) {
                    GuardiansOathTalent talent = new GuardiansOathTalent();
                    GuardiansOathEffect.onEnemyDefeated(player, target, talent);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            ServerPlayer serverPlayer = (ServerPlayer) event.player;
            if (event.phase == TickEvent.Phase.START) {
            serverPlayer.getCapability(PlayerClassProvider.PLAYER_RPGCLASS).ifPresent(playerClass -> {
                String className = playerClass.getRpgClass();
                String subclassName = playerClass.getRpgSubclass();
                ModNetworking.sendToPlayer(new ClassSelectionS2CPacket(className, subclassName), serverPlayer);
                ClassAttributeManager.applyClassAttributes(serverPlayer);
                ClassAttributeManager.applyTalentAttributes(serverPlayer);
                serverPlayer.getCapability(PlayerResourceProvider.PLAYER_RESOURCE).ifPresent(playerResource -> {
                    String warrior = Component.translatable("class.warrior.name").getString();
                    String ranger = Component.translatable("class.ranger.name").getString();
                    String mage = Component.translatable("class.mage.name").getString();

                    String paladin = Component.translatable("subclass.paladin.name").getString();
                    //Stamina active declarations
                    playerResource.setStaminaActive(
                            warrior.equals(className));

                    //Energy active declarations
                    playerResource.setEnergyActive(
                            ranger.equals(className));


                    playerResource.setManaActive(
                            mage.equals(className) ||
                                    paladin.equals(subclassName) && new PathOfThePaladinSubclass().isActive(serverPlayer));

                    double deltaTime = 1.0 / 20.0; //1 second is 40 ticks due to double counting // Nevermind? I guess I fixed the double ticking
                    playerResource.updateRechargeRates(deltaTime);

                    ModNetworking.sendToPlayer(
                            new PlayerResourcesS2CPacket(
                                    playerResource.getMaxStamina(),
                                    playerResource.getMaxEnergy(),
                                    playerResource.getMaxMana(),
                                    playerResource.getStamina(),
                                    playerResource.getEnergy(),
                                    playerResource.getMana()), serverPlayer);

                    if (paladin.equals(subclassName) && new PathOfThePaladinSubclass().isActive(serverPlayer)) {
                        //serverPlayer.sendSystemMessage(Component.literal("You're a paladin! I just send your mana. You have " + playerResource.getMana() + "/" + playerResource.getMaxMana()));
                    }
                });
            });

            SecondWindTalent secondWindTalent = new SecondWindTalent();
            if (secondWindTalent.isActive(serverPlayer)) {
                serverPlayer.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    if (!serverPlayer.hasEffect(ModEffects.WINDED.get())) {
                        if (playerAbility.getWindedDuration() >= 20) { //Anything below 1 second (20 ticks) shouldn't matter
                            MobEffectInstance windedEffect = new MobEffectInstance(
                                    ModEffects.WINDED.get(),
                                    playerAbility.getWindedDuration(),
                                    0,
                                    false,
                                    true,
                                    true);
                            serverPlayer.addEffect(windedEffect);
                        }
                    }
                });
            }
        }

            if (event.phase == TickEvent.Phase.END) {
                AttributeInstance dodgeChanceAttribute = serverPlayer.getAttribute(CoreAttributes.DODGE_CHANCE.get());
                if (dodgeChanceAttribute != null) {
                    double dodgeChance = dodgeChanceAttribute.getValue();
                    //serverPlayer.sendSystemMessage(Component.literal("Current dodge chance: " + dodgeChance + "%")); // debug code
                } else {
                    //serverPlayer.sendSystemMessage(Component.literal("Dodge chance attribute not found!")); // debug code
                }

                SteadyBreathingTalent steadyBreathingTalent = new SteadyBreathingTalent();
                if (steadyBreathingTalent.isActive(serverPlayer)) {
                    serverPlayer.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                        if (!serverPlayer.isCrouching()) {
                            playerAbility.resetSteadyBreathing();
                            if (serverPlayer.hasEffect(ModEffects.STEADY_BREATHING.get())) {
                                serverPlayer.removeEffect(ModEffects.STEADY_BREATHING.get());
                            }
                            return;
                        }

                        // Increment idle ticks and add a stack if the player remains still
                        playerAbility.incrementSteadyBreathingIdleTicks();
                        if (playerAbility.getSteadyBreathingIdleTicks() >= 40) {
                            playerAbility.incrementSteadyBreathingStacks();
                            playerAbility.setSteadyBreathingIdleTicks(0); // Reset idle ticks after gaining a stack
                            serverPlayer.sendSystemMessage(Component.literal("Gained a Steady Breathing stack! Current stacks: " + playerAbility.getSteadyBreathingStacks())); // Debug message

                            if (!serverPlayer.hasEffect(ModEffects.STEADY_BREATHING.get())) {
                                serverPlayer.addEffect(new MobEffectInstance(ModEffects.STEADY_BREATHING.get(), 100, playerAbility.getSteadyBreathingStacks() - 1));
                            } else {
                                SteadyBreathingEffect.updateEffectDuration(serverPlayer, 100); // Refresh duration
                            }
                        }
                    });
                }

                //Charge logic
                serverPlayer.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbilities ->
                        serverPlayer.getCapability(PlayerTargetingProvider.PLAYER_TARGET).ifPresent(targetData -> {
                    if (playerAbilities.isCharging()) {
                        if (serverPlayer.isShiftKeyDown()) {
                            playerAbilities.setCharging(false);
                            return;
                        }

                        ClientEvents.showChargeCancelMessage = true;

                        LivingEntity targetEntity;
                        if (targetData.isLockedOn()){
                            targetEntity = targetData.getLockedTarget();
                        } else {
                            targetEntity = TargetingUtils.getTarget(serverPlayer);
                        }

                        if (targetEntity == null) {
                            playerAbilities.setCharging(false);
                            ClientEvents.showChargeCancelMessage = false;
                            return;
                        }

                        Vec3 targetPos = targetEntity.position();
                        Vec3 targetEyePos = targetEntity.getEyePosition();
                        Vec3 playerPos = serverPlayer.position();
                        Vec3 direction = targetPos.subtract(playerPos).normalize();
                        Vec3 lookDirection = serverPlayer.getLookAngle();
                        double distance = playerPos.distanceTo(targetPos);
                        int timeout_ticks = 60;
                        double strafeDistance = 0.5;
                        double stoppingDistance = 2.0;

                        if (distance > stoppingDistance) {
                            ((Player) serverPlayer).lookAt(EntityAnchorArgument.Anchor.EYES, targetEyePos);
                            double speed = Math.max(0.1, Math.min(1.5, distance / 5.0));
                            Vec3 motion = direction.scale(speed);

                            double playerY = playerPos.y;
                            double targetY = targetPos.y;

                            boolean isOnGround = serverPlayer.onGround();

                            if (isOnGround) {
                                motion = motion.add(0, -0.1, 0);
                                serverPlayer.hurtMarked = true;
                            } else if (playerY > targetY) {
                                motion = motion.add(0, -0.25, 0);
                                serverPlayer.hurtMarked = true;
                            }

                            boolean obstacleAbove = checkObstacles(serverPlayer, playerPos, lookDirection, 2, 0);
                            boolean obstacleBelow = checkObstacles(serverPlayer, playerPos, lookDirection, -1, 0);
                            boolean obstacleFrontFeet = checkObstacles(serverPlayer, playerPos, lookDirection, 0, 0);
                            boolean obstacleFrontEyes = checkObstacles(serverPlayer, playerPos, lookDirection, 1, 0);
                            boolean obstacleLeft = checkObstacles(serverPlayer, playerPos, lookDirection, 0, 1, true);
                            boolean obstacleRight = checkObstacles(serverPlayer, playerPos, lookDirection, 0, -1, true);
                            boolean obstacleFence = checkFencesAndGates(serverPlayer, playerPos, lookDirection);

                            if (obstacleFence) {
                                //serverPlayer.sendSystemMessage(Component.literal("Fence!")); // Debug code
                                motion = handleFenceJumping(serverPlayer, motion, isOnGround);
                                playerAbilities.setLastObstacle("Fence");
                            } else if (obstacleBelow || obstacleFrontFeet) {
                                motion = motion.add(0, 0.2, 0);
                                playerAbilities.setLastObstacle("Below");
                            } else if (obstacleAbove) {
                                motion = motion.add(0, -0.2, 0);
                                playerAbilities.setLastObstacle("Above");
                            } else if (obstacleFrontEyes) {
                                Vec3 strafeDirection = playerAbilities.getStrafeDirection();
                                if (strafeDirection == null) {
                                    Vec3 leftStrafe = lookDirection.cross(new Vec3(0, 1, 0)).normalize().scale(strafeDistance);
                                    Vec3 rightStrafe = lookDirection.cross(new Vec3(0, -1, 0)).normalize().scale(strafeDistance);
                                    if (obstacleRight) {
                                        strafeDirection = leftStrafe;
                                    } else if (obstacleLeft) {
                                        strafeDirection = rightStrafe;
                                    }
                                    playerAbilities.setStrafeDirection(strafeDirection);
                                }
                                if (strafeDirection != null) {
                                    motion = strafeDirection.add(direction.scale(speed));
                                }
                                playerAbilities.setLastObstacle("FrontEyes");
                            } else {
                                motion = motion.add(0, -0.1, 0);
                                playerAbilities.setStrafeDirection(null);
                                if ("Fence".equals(playerAbilities.getLastObstacle()) && isAirInFront(serverPlayer, playerPos, lookDirection)) {
                                    motion = motion.add(0, 0.5, 0);
                                } else {
                                    playerAbilities.setLastObstacle("None");
                                }
                            }
                            //if (isOnGround) {
                            spawnSmokeParticles(serverPlayer);
                            //}

                            serverPlayer.setDeltaMovement(motion);
                        } else {
                            serverPlayer.setPos(targetPos.x - direction.x, targetPos.y - direction.y, targetPos.z - direction.z);
                            serverPlayer.setDeltaMovement(Vec3.ZERO);
                            playerAbilities.setCharging(false);
                            playerAbilities.setStrafeDirection(null);
                        }
                        serverPlayer.hurtMarked = true;

                        if (playerAbilities.getChargeTicks() >= timeout_ticks) {
                            playerAbilities.setCharging(false);
                            playerAbilities.setChargeTicks(0);
                            ((Player) serverPlayer).sendSystemMessage(Component.literal("No path found..."));
                        } else {
                            if (playerPos.equals(playerAbilities.getLastChargePosition())) {
                                playerAbilities.incrementChargeTicks();
                            } else {
                                playerAbilities.setChargeTicks(0);
                            }
                            playerAbilities.setLastChargePosition(playerPos);
                        }
                    } else {
                        ClientEvents.showChargeCancelMessage = false;
                    }
                }));

                //Stampede Logic
                serverPlayer.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                    StampedeTalent stampedeTalent = new StampedeTalent();
                    if (stampedeTalent.isActive(serverPlayer)) {
                        int talentPoints = stampedeTalent.getPoints(serverPlayer);
                        double damage = 5 + (talentPoints == 3 ? 5 : (talentPoints == 2 ? 2 : 0));
                        Vec3 playerPos = serverPlayer.position();

                        float yaw = serverPlayer.getYRot();
                        double xDirection = -Math.sin(Math.toRadians(yaw));
                        double zDirection = Math.cos(Math.toRadians(yaw));
                        Vec3 direction = new Vec3(xDirection, 0, zDirection).normalize();
                        Vec3 center = playerPos.add(direction.scale(0.5d));

                        if (playerAbility.isStampeding()) {
                            Vec3 motion = direction.scale(1.0);
                            AABB boundingBox = serverPlayer.getBoundingBox().move(direction.scale(0.5)).inflate(1.0);
                            List<LivingEntity> targets = serverPlayer.level().getEntitiesOfClass(LivingEntity.class, boundingBox);

                            for (LivingEntity target : targets) {
                                if (target != serverPlayer) {
                                    target.hurt(serverPlayer.level().damageSources().playerAttack(serverPlayer), (float) damage);
                                    Vec3 knockback = new Vec3(direction.x, 0.1, direction.z).normalize().scale(1.5);
                                    target.setDeltaMovement(knockback);
                                }
                            }
                            boolean obstacleAbove = checkObstacles(serverPlayer, playerPos, direction, 2, 0);
                            boolean obstacleFrontFeet = checkObstacles(serverPlayer, playerPos, direction, 0, 0);
                            boolean obstacleFrontEyes = checkObstacles(serverPlayer, playerPos, direction, 1, 0);

                            BlockPos pos1 = BlockPos.containing(center.x() - 0.5, center.y(), center.z());
                            BlockPos pos2 = BlockPos.containing(center.x() + 0.5, center.y(), center.z());
                            BlockPos pos3 = BlockPos.containing(center.x(), center.y(),center.z() - 0.5);
                            BlockPos pos4 = BlockPos.containing(center.x(), center.y(),center.z() + 0.5);

                            boolean cornerObstacle1 = isObstacle(serverPlayer.level().getBlockState(pos1));
                            boolean cornerObstacle2 = isObstacle(serverPlayer.level().getBlockState(pos2));
                            boolean cornerObstacle3 = isObstacle(serverPlayer.level().getBlockState(pos3));
                            boolean cornerObstacle4 = isObstacle(serverPlayer.level().getBlockState(pos4));

                            if (obstacleFrontFeet || cornerObstacle1 || cornerObstacle2 || cornerObstacle3 || cornerObstacle4 && !obstacleFrontEyes) {
                                BlockPos topBlockPos = BlockPos.containing(center).above();
                                if (serverPlayer.level().getBlockState(topBlockPos).isAir()) {
                                    Vec3 nextPos = new Vec3(playerPos.x + direction.x, topBlockPos.getY(), playerPos.z + direction.z);
                                    serverPlayer.teleportTo(nextPos.x, nextPos.y, nextPos.z);
                                }
                            } else {
                                if (obstacleAbove) {
                                    motion = motion.add(0, -0.2, 0);
                                } else {
                                    motion = motion.add(0, -0.1, 0);
                                }
                                serverPlayer.setDeltaMovement(motion);
                            }
                            spawnSmokeParticles(serverPlayer);
                            serverPlayer.hurtMarked = true;
                            playerAbility.incrementStampedeTicks();
                            if (playerAbility.getStampedeTicks() >= 16) {
                                playerAbility.setStampeding(false);
                                playerAbility.setStampedeTicks(0);
                            }
                        }
                    }
                });

                //Intimidating Presence Logic
                IntimidatingPresenceTalent.tickPlayer(serverPlayer);

                GuardiansOathTalent guardiansOathTalent = new GuardiansOathTalent();
                if (guardiansOathTalent.isActive(serverPlayer)){
                    guardiansOathTalent.applyGuardianOathEffects(serverPlayer);
                }

                WarlordsPresenceTalent warlordsPresenceTalent = new WarlordsPresenceTalent();
                if (warlordsPresenceTalent.isActive(serverPlayer)){
                    warlordsPresenceTalent.applyWarlordsPresenceEffects(serverPlayer);
                    serverPlayer.getCapability(PlayerAbilityProvider.PLAYER_ABILITY).ifPresent(playerAbility -> {
                        if (!serverPlayer.hasEffect(ModEffects.DEFEATED.get())) {
                            if (playerAbility.getExhaustedDuration()>= 20) { //Anything below 1 second (20 ticks) shouldn't matter
                                MobEffectInstance exhaustedEffect = new MobEffectInstance(
                                        ModEffects.DEFEATED.get(),
                                        playerAbility.getExhaustedDuration(),
                                        0,
                                        false,
                                        true,
                                        true);
                                serverPlayer.addEffect(exhaustedEffect);
                            }
                        }
                    });
                }
            }
        }
    }

    // Static methods to check obstacles
    private static boolean checkObstacles(ServerPlayer serverPlayer, Vec3 playerPos, Vec3 lookDirection, int yOffset, int zOffset) {
        return checkObstacles(serverPlayer, playerPos, lookDirection, yOffset, zOffset, false);
    }

    private static boolean checkObstacles(ServerPlayer serverPlayer, Vec3 playerPos, Vec3 lookDirection, int yOffset, int zOffset, boolean lateral) {
        int[][] offsets = lateral ? new int[][]{{1, 1}, {-1, 1}} : new int[][]{{1, 0}, {0, 0}, {-1, 0}};
        for (int[] offset : offsets) {
            Vec3 testPoint = playerPos
                    .add(lookDirection.scale(offset[0]))
                    .add(0, yOffset, offset[1] * zOffset);
            BlockPos checkPos = BlockPos.containing(testPoint);
            //serverPlayer.sendSystemMessage(Component.literal("Checking block at " + checkPos + ": " + serverPlayer.level.getBlockState(checkPos).getBlock().getName().getString())); // Debug code
            if (isObstacle(serverPlayer.level().getBlockState(checkPos))) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkFencesAndGates(ServerPlayer serverPlayer, Vec3 playerPos, Vec3 lookDirection) {
        int[][] offsets = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 1}, {0, 2}, {-1, 0}, {-1, 1}, {-1, 2}};
        for (int[] offset : offsets) {
            Vec3 testPoint = playerPos
                    .add(lookDirection.scale(offset[0]))
                    .add(0, offset[1], 0);
            BlockPos checkPos = BlockPos.containing(testPoint);
            //serverPlayer.sendSystemMessage(Component.literal("Checking fence or gate at " + checkPos + ": " + serverPlayer.level.getBlockState(checkPos).getBlock().getName().getString())); // Debug code
            if (isFenceOrGate(serverPlayer.level().getBlockState(checkPos))) {
                return true;
            }
        }
        return false;
    }


    private static Vec3 handleFenceJumping(ServerPlayer serverPlayer, Vec3 motion, boolean isOnGround) {
        if (!isOnGround) {
            motion = motion.add(0, 0.5, 0);
        } else {
            motion = motion.add(0, 1.0, 0);
        }
        return motion;
    }

    private static boolean isObstacle(BlockState blockState) {
        return blockState.blocksMotion() && !blockState.canBeReplaced() && !(blockState.getBlock() instanceof FenceBlock) && !(blockState.getBlock() instanceof FenceGateBlock);
    }

    private static boolean isFenceOrGate(BlockState state) {
        return state.getBlock() instanceof FenceBlock || state.getBlock() instanceof FenceGateBlock;
    }

    private static boolean isAirInFront(ServerPlayer serverPlayer, Vec3 playerPos, Vec3 lookDirection) {
        Vec3 target = playerPos.add(lookDirection);
        BlockPos checkPos = BlockPos.containing(target);
        return serverPlayer.level().getBlockState(checkPos).isAir();
    }

    private static void spawnSmokeParticles(ServerPlayer player) {
        Level level = player.level();
        if (level instanceof ServerLevel) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            double offsetX = 0.2;
            double offsetY = 0.1;
            double offsetZ = 0.2;
            double speed = 0.02;

            for (int i = 0; i < 5; i++) {
                double particleX = x + (level.random.nextDouble() - 0.5) * offsetX;
                double particleY = y + (level.random.nextDouble() - 0.5) * offsetY;
                double particleZ = z + (level.random.nextDouble() - 0.5) * offsetZ;
                ((ServerLevel) level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, particleX, particleY, particleZ, 1, 0, 0, 0, speed);
            }
        }
    }
}

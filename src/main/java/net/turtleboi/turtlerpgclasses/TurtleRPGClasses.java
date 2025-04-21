package net.turtleboi.turtlerpgclasses;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.turtleboi.turtlecore.util.BetterBrewingRecipe;
import net.turtleboi.turtlerpgclasses.commands.OpenEditUICommand;
import net.turtleboi.turtlerpgclasses.commands.ResetTalentsCommand;
import net.turtleboi.turtlerpgclasses.commands.SetTalentPointsCommand;
import net.turtleboi.turtlerpgclasses.config.UIConfig;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.entity.ModEntities;
import net.turtleboi.turtlerpgclasses.event.CooldownResetListener;
import net.turtleboi.turtlerpgclasses.init.ModAttributes;
import net.turtleboi.turtlerpgclasses.item.ModCreativeModeTab;
import net.turtleboi.turtlerpgclasses.item.ModItems;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.potion.ModPotions;
import org.slf4j.Logger;

@Mod(TurtleRPGClasses.MOD_ID)
public class TurtleRPGClasses {
    public static final String MOD_ID = "turtlerpgclasses";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurtleRPGClasses() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModCreativeModeTab.register(eventBus);

        ModEntities.register(eventBus);
        ModEffects.register(eventBus);
        ModPotions.register(eventBus);

        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CooldownResetListener());

        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, UIConfig.spec, "turtlerpgclasses/turtlerpgclasses-client.toml");

        ModAttributes.REGISTRY.register(eventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetworking.register();

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.THICK,
                Items.SUGAR_CANE, ModPotions.LESSER_ENERGY_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.LESSER_ENERGY_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.ENERGY_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.ENERGY_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.GREATER_ENERGY_POTION.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.THICK,
                Items.BOOK, ModPotions.LESSER_MANA_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.LESSER_MANA_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.MANA_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.MANA_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.GREATER_MANA_POTION.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.THICK,
                Items.COOKED_BEEF, ModPotions.LESSER_STAMINA_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.LESSER_STAMINA_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.STAMINA_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.STAMINA_POTION.get(),
                Items.GLOWSTONE_DUST, ModPotions.GREATER_STAMINA_POTION.get()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        SetTalentPointsCommand.register(event.getServer().getCommands().getDispatcher());
        ResetTalentsCommand.register(event.getServer().getCommands().getDispatcher());
        OpenEditUICommand.register(event.getServer().getCommands().getDispatcher());
    }
}
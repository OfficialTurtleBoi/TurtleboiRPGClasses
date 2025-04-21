package net.turtleboi.turtlerpgclasses.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.turtleboi.turtlecore.TurtleCore;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TurtleCore.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TURTLERPGCLASSES_TAB = CREATIVE_MODE_TABS.register("turtlerpgclassestab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.NETHERITE_INGOT))
                    .title(Component.translatable("creativetab.turtlerpgclassestab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WOODEN_DAGGER.get());
                        pOutput.accept(ModItems.FLINT_DAGGER.get());
                        pOutput.accept(ModItems.IRON_DAGGER.get());
                        pOutput.accept(ModItems.GOLDEN_DAGGER.get());
                        pOutput.accept(ModItems.DIAMOND_DAGGER.get());
                        pOutput.accept(ModItems.NETHERITE_DAGGER.get());

                        pOutput.accept(ModItems.WOODEN_HANDAXE.get());
                        pOutput.accept(ModItems.STONE_HANDAXE.get());
                        pOutput.accept(ModItems.IRON_HANDAXE.get());
                        pOutput.accept(ModItems.GOLD_HANDAXE.get());
                        pOutput.accept(ModItems.DIAMOND_HANDAXE.get());
                        pOutput.accept(ModItems.NETHERITE_HANDAXE.get());

                        pOutput.accept(ModItems.AMETHYST_WAND.get());
                        pOutput.accept(ModItems.ARCANE_STAFF.get());
                        pOutput.accept(ModItems.PYRO_WAND.get());
                        pOutput.accept(ModItems.INFERNO_STAFF.get());
                        pOutput.accept(ModItems.DEATH_RATTLE.get());
                        pOutput.accept(ModItems.NECROMANCER_STAFF.get());
                        pOutput.accept(ModItems.MAGICAL_STICK.get());
                        pOutput.accept(ModItems.DRUID_STAFF.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

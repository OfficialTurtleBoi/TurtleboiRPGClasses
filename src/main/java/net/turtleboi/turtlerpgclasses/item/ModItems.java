package net.turtleboi.turtlerpgclasses.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.item.weapon.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TurtleRPGClasses.MOD_ID);

    public static final RegistryObject<Item> WOODEN_DAGGER = ITEMS.register("wooden_dagger",
            () -> new WoodenDaggerItem(Tiers.WOOD, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> FLINT_DAGGER = ITEMS.register("flint_dagger",
            () -> new FlintDaggerItem(Tiers.STONE, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> IRON_DAGGER = ITEMS.register("iron_dagger",
            () -> new IronDaggerItem(Tiers.IRON, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> GOLDEN_DAGGER = ITEMS.register("golden_dagger",
            () -> new GoldenDaggerItem(Tiers.GOLD, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_DAGGER = ITEMS.register("diamond_dagger",
            () -> new DiamondDaggerItem(Tiers.DIAMOND, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_DAGGER = ITEMS.register("netherite_dagger",
            () -> new NetheriteDaggerItem(Tiers.NETHERITE, 1, -1.6F, new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_HANDAXE = ITEMS.register("wooden_handaxe",
            () -> new WoodenHandaxeItem(Tiers.WOOD, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> STONE_HANDAXE = ITEMS.register("stone_handaxe",
            () -> new StoneHandaxeItem(Tiers.STONE, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> IRON_HANDAXE = ITEMS.register("iron_handaxe",
            () -> new IronHandaxeItem(Tiers.IRON, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> GOLD_HANDAXE = ITEMS.register("golden_handaxe",
            () -> new GoldenHandaxeItem(Tiers.GOLD, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_HANDAXE = ITEMS.register("diamond_handaxe",
            () -> new DiamondHandaxeItem(Tiers.DIAMOND, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_HANDAXE = ITEMS.register("netherite_handaxe",
            () -> new NetheriteHandaxeItem(Tiers.NETHERITE, 2, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_WAND = ITEMS.register("amethyst_wand",
            () -> new AmethystWandItem(new Item.Properties()));

    public static final RegistryObject<Item> ARCANE_STAFF = ITEMS.register("arcane_staff",
            () -> new ArcaneStaffItem(new Item.Properties()));

    public static final RegistryObject<Item> PYRO_WAND = ITEMS.register("pyro_wand",
            () -> new PyroWandItem(new Item.Properties()));

    public static final RegistryObject<Item> INFERNO_STAFF = ITEMS.register("inferno_staff",
            () -> new InfernoStaffItem(new Item.Properties()));

    public static final RegistryObject<Item> DEATH_RATTLE = ITEMS.register("death_rattle",
            () -> new DeathRattleItem(new Item.Properties()));

    public static final RegistryObject<Item> NECROMANCER_STAFF = ITEMS.register("necromancer_staff",
            () -> new NecromancerStaffItem(new Item.Properties()));

    public static final RegistryObject<Item> MAGICAL_STICK = ITEMS.register("magical_stick",
            () -> new MagicalStickItem(new Item.Properties()));

    public static final RegistryObject<Item> DRUID_STAFF = ITEMS.register("druid_staff",
            () -> new DruidStaffItem(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}

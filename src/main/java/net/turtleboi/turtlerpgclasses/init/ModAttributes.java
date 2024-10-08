package net.turtleboi.turtlerpgclasses.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;

@Mod.EventBusSubscriber(modid = TurtleRPGClasses.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static final DeferredRegister<Attribute> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TurtleRPGClasses.MOD_ID);
    //Stamina Attribute
    public static final RegistryObject<Attribute> MAX_STAMINA =
            create("max_stamina", 10D, 0D);
    //Energy Attribute
    public static final RegistryObject<Attribute> MAX_ENERGY =
            create("max_energy", 50D, 0D);
    //Mana Attribute
    public static final RegistryObject<Attribute> MAX_MANA =
            create("max_mana", 200D, 0D);

    public static final RegistryObject<Attribute> STAMINA_RECHARGE =
            create("stamina_recharge", 0.1D, 0D);

    public static final RegistryObject<Attribute> ENERGY_RECHARGE =
            create("energy_recharge", 4.0D, 0D);

    public static final RegistryObject<Attribute> MANA_RECHARGE =
            create("mana_recharge", 0.33D, 0D);

    private static RegistryObject<Attribute> create(
            String name, double defaultValue, double minValue) {
        String descriptionId = "attribute.name." + TurtleRPGClasses.MOD_ID + "." + name;
        return REGISTRY.register(
                name,
                () -> new RangedAttribute(descriptionId, defaultValue, minValue, 1024D).setSyncable(true));
    }

    @SubscribeEvent
    public static void attachAttributes(EntityAttributeModificationEvent event) {
        REGISTRY.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(attribute -> event.add(EntityType.PLAYER, attribute));
    }
}

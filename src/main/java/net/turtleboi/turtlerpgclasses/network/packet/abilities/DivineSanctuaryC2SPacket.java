package net.turtleboi.turtlerpgclasses.network.packet.abilities;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.turtleboi.turtlerpgclasses.capabilities.resources.PlayerResourceProvider;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.network.packet.resources.PlayerResourcesS2CPacket;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfThePaladinSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.DivineSanctuaryTalent;

import java.util.function.Supplier;

public class DivineSanctuaryC2SPacket {

    public DivineSanctuaryC2SPacket(){
    }

    public DivineSanctuaryC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // We're on the server :)
            ServerPlayer player = context.getSender();
            if (player == null) return;
            player.getCapability(PlayerResourceProvider.PLAYER_RESOURCE).ifPresent(playerResource -> {
                ModNetworking.sendToPlayer(
                        new PlayerResourcesS2CPacket(
                                playerResource.getMaxStamina(),
                                playerResource.getMaxEnergy(),
                                playerResource.getMaxMana(),
                                playerResource.getStamina(),
                                playerResource.getEnergy(),
                                playerResource.getMana()), player);
                String playerClass = ClientClassData.getPlayerClass();
                if ("Warrior".equals(playerClass)) {
                    ActiveAbility ability = null;
                    PathOfThePaladinSubclass pathOfThePaladinSubclass = new PathOfThePaladinSubclass();
                    if (pathOfThePaladinSubclass.isActive(player)) {
                        ability = new DivineSanctuaryTalent();
                    }
                    if (ability != null) {
                        ability.useAbility(player, playerResource);
                    } else if (pathOfThePaladinSubclass.isActive(player)) {
                        player.sendSystemMessage(Component.translatable("not_bound"));
                    }
                }
            });
        });
        return true;
    }
}

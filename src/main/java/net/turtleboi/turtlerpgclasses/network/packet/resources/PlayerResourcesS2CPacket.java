package net.turtleboi.turtlerpgclasses.network.packet.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.turtleboi.turtlerpgclasses.client.ClientResourceData;

import java.util.function.Supplier;

public class PlayerResourcesS2CPacket {
    private final int currentStamina;
    private final int maxStamina;
    private final int currentEnergy;
    private final int maxEnergy;
    private final int currentMana;
    private final int maxMana;

    public PlayerResourcesS2CPacket(int maxStamina, int maxEnergy, int maxMana, int currentStamina,  int currentEnergy,  int currentMana) {
        this.maxStamina = maxStamina;
        this.maxEnergy = maxEnergy;
        this.maxMana = maxMana;
        this.currentStamina = currentStamina;
        this.currentEnergy = currentEnergy;
        this.currentMana = currentMana;
    }

    public PlayerResourcesS2CPacket(FriendlyByteBuf buf) {
        this.maxStamina = buf.readInt();
        this.maxEnergy = buf.readInt();
        this.maxMana = buf.readInt();
        this.currentStamina = buf.readInt();
        this.currentEnergy = buf.readInt();
        this.currentMana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(maxStamina);
        buf.writeInt(maxEnergy);
        buf.writeInt(maxMana);
        buf.writeInt(currentStamina);
        buf.writeInt(currentEnergy);
        buf.writeInt(currentMana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT :)
            //Minecraft minecraft = Minecraft.getInstance();
            //LocalPlayer player = minecraft.player;
            //if (player != null){
            //    //player.sendSystemMessage(Component.literal("Sending resource data!")); //debug code
            //}
            ClientResourceData.setMaxStamina(maxStamina);
            ClientResourceData.setMaxEnergy(maxEnergy);
            ClientResourceData.setMaxMana(maxMana);
            ClientResourceData.setStamina(currentStamina);
            ClientResourceData.setEnergy(currentEnergy);
            ClientResourceData.setMana(currentMana);
        });
        return true;
    }
}

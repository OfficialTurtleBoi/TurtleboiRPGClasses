package net.turtleboi.turtlerpgclasses.event;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.render.AuraRenderer;
import net.turtleboi.turtlerpgclasses.client.ui.ClassSelectionScreen;
import net.turtleboi.turtlerpgclasses.client.ui.cooldowns.CooldownOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.resources.ResourceOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentScreen;
import net.turtleboi.turtlerpgclasses.effect.ModEffects;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.network.packet.abilities.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.WarlordsPresenceTalent;
import net.turtleboi.turtlerpgclasses.util.KeyBinding;

public class ClientEvents {
    public static boolean showChargeCancelMessage = false;

    @Mod.EventBusSubscriber(modid = TurtleRPGClasses.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void openClassSelectionGUI(InputEvent.Key event) {
            if (KeyBinding.OPEN_CLASSSELECT_KEY.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.setScreen(new ClassSelectionScreen());
            }
        }

        @SubscribeEvent
        public static void openTalentTreeGUI(InputEvent.Key event) {
            if (KeyBinding.OPEN_TALENTTREE_KEY.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.setScreen(new TalentScreen());
            }
        }

        @SubscribeEvent
        public static void useActiveAbility(InputEvent.Key event) {
            if (KeyBinding.ACTIVE1.consumeClick()) {
                ModNetworking.sendToServer(new UnleashFuryC2SPacket());
                ModNetworking.sendToServer(new SteelBarbsC2SPacket());
                ModNetworking.sendToServer(new DivineSanctuaryC2SPacket());
            }
            if (KeyBinding.ACTIVE2.consumeClick()) {
                ModNetworking.sendToServer(new ChargeC2SPacket());
                ModNetworking.sendToServer(new TauntC2SPacket());
            }
            if (KeyBinding.ACTIVE3.consumeClick()) {
                ModNetworking.sendToServer(new ColossusC2SPacket());
                ModNetworking.sendToServer(new ExecuteC2SPacket());
            }
            if (KeyBinding.ACTIVE4.consumeClick()) {
                ModNetworking.sendToServer(new WrathOfTheWarlordC2SPacket());
                ModNetworking.sendToServer(new WordOfHonorC2SPacket());
            }
        }

        @SubscribeEvent
        public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
            if (!showChargeCancelMessage) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Window window = event.getWindow();
            int scaledWidth = window.getGuiScaledWidth();
            int scaledHeight = window.getGuiScaledHeight();

            Minecraft minecraft = Minecraft.getInstance();
            KeyMapping sneakKey = minecraft.options.keyShift;
            String sneakKeyName = sneakKey.getTranslatedKeyMessage().getString();
            String msg = "Press " + sneakKeyName + " to cancel Charge";

            int x = scaledWidth  / 2;
            int y = scaledHeight / 2 + 30;

            guiGraphics.drawCenteredString(minecraft.font, msg, x, y, 0xFFFFFF);
        }
    }

    @Mod.EventBusSubscriber(modid = TurtleRPGClasses.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RenderEvents {

        @SubscribeEvent
        public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
            Player player = event.getEntity();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource bufferSource = event.getMultiBufferSource();

            // Check if the player has the custom Stealthed effect
            if (player.hasEffect(ModEffects.STEALTHED.get())) {
                // Clone the pose stack for rendering the translucent player separately
                PoseStack newPoseStack = new PoseStack();
                newPoseStack.mulPoseMatrix(poseStack.last().pose());  // Copy original pose to new PoseStack
                newPoseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
                newPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale( 1.05F, 1.05F, 1.05F);
                // Apply some transformation to distinguish the copy if necessary (optional)
                newPoseStack.translate(0, -player.getBbHeight() + 0.3, 0);  // Slightly offset to avoid overlap (optional)

                // Set the transparency level
                float alpha = 0.35F;  // 35% transparent

                // Retrieve the player's texture (you can modify or add custom textures)
                ResourceLocation playerTexture = Minecraft.getInstance().player.getSkinTextureLocation();

                // Render the translucent player copy with reduced alpha
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(playerTexture));
                event.getRenderer().getModel().renderToBuffer(
                        newPoseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                        1.0F, 1.0F, 1.0F, alpha
                );
                //event.setCanceled(true);
                // Don't cancel the event; this ensures the original player model is rendered as usual
            }
        }

        @SubscribeEvent
        public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = event.getEntity();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

            if (new WarlordsPresenceTalent().isActive(player)) {
                AuraRenderer.renderAura(player, poseStack, bufferSource, event.getPartialTick(), 2);
            } else if (new GuardiansOathTalent().isActive(player)){
                AuraRenderer.renderAura(player, poseStack, bufferSource, event.getPartialTick(), 1);
            }
            bufferSource.endBatch();
        }

        private static int lastScreenWidth = -1;
        private static int lastScreenHeight = -1;

        @SubscribeEvent
        public static void onScreenRender(ScreenEvent.Render.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Screen currentScreen = minecraft.screen;

            if (currentScreen != null) {
                int currentWidth = minecraft.getWindow().getGuiScaledWidth();
                int currentHeight = minecraft.getWindow().getGuiScaledHeight();

                if (currentWidth != lastScreenWidth || currentHeight != lastScreenHeight) {
                    lastScreenWidth = currentWidth;
                    lastScreenHeight = currentHeight;
                    if (minecraft.player != null) {
                        CooldownOverlay.initializeSlots(minecraft.player);
                        ResourceOverlay.initializeResourceBars(minecraft.player);
                    }
                }
            }
        }
    }
}

package net.turtleboi.turtlerpgclasses.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.turtleboi.turtlecore.client.util.VertexBuilder.vertex;

public class AuraRenderer {
    private static final ResourceLocation AURA_TEXTURE = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/aura.png");

    public static void renderAura(Player player, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int auraType) {

        float time = player.tickCount + partialTicks;

        float rotationSpeed = 1.0f;
        float rotationAngle = (time * rotationSpeed) % 360f;

        float baseScale = 0.1f;
        float pulsateAmount = 0.01f;
        float pulsateSpeed = 0.05f;
        float scale = baseScale + pulsateAmount * (float)Math.sin(time * pulsateSpeed);

        float playerYaw = -player.getYRot();
        float alpha = 1.0F, red, green, blue;
        switch (auraType) {
            case 1: // Golden
                red = 1f; green = 0.953f; blue = 0.408f;
                break;
            case 2: // Deep Red
                red = 0.718f; green = 0.098f; blue = 0.176f;
                break;
            default: //Silver
                red = green = blue = 0.616f;
                break;
        }

        int rInt = Math.round(red   * 255f);
        int gInt = Math.round(green * 255f);
        int bInt = Math.round(blue  * 255f);
        int aInt = Math.round(alpha * 255f);

        poseStack.pushPose();
        poseStack.translate(0, 0.2, 0);
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(playerYaw + 45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle + 45));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(AURA_TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        vertex(vertexConsumer, matrix, normalMatrix, -8, -8, 0, 0, 0, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, 8, -8, 0, 1, 0, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, 8, 8, 0, 1, 1, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, -8, 8, 0, 0, 1, rInt, gInt, bInt, aInt);

        vertex(vertexConsumer, matrix, normalMatrix, -8, 8, 0, 0, 1, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, 8, 8, 0, 1, 1, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, 8, -8, 0, 1, 0, rInt, gInt, bInt, aInt);
        vertex(vertexConsumer, matrix, normalMatrix, -8, -8, 0, 0, 0, rInt, gInt, bInt, aInt);
        poseStack.popPose();
    }
}

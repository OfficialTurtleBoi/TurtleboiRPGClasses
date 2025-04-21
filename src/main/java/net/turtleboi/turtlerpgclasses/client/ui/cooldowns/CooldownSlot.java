package net.turtleboi.turtlerpgclasses.client.ui.cooldowns;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;

import java.util.HashMap;
import java.util.Map;

public class CooldownSlot {
    private static final ResourceLocation slotTexture = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/cooldown_bay.png");
    private static final int slotSize = 22;
    private static final int iconSize = 16;
    private static final int slotSpacing = 4;
    private static final int instantRefreshFrameCount = 14;
    private static final int durationRefreshFrameCount = 14;
    private static final int durationFrameCount = 14;
    private static final int frameDuration = 50; 

    private int xPos;
    private int yPos;
    private final KeyMapping keyBinding;
    private ActiveAbility ability;
    private static final Map<ActiveAbility, Long> cooldownEndTimes = new HashMap<>();

    public CooldownSlot(int xPos, int yPos, KeyMapping keyBinding, ActiveAbility ability) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.keyBinding = keyBinding;
        this.ability = ability;
    }

    public void setAbility(ActiveAbility ability) {
        this.ability = ability;
    }

    public static int getSlotSize() {
        return slotSize;
    }

    public static int getSlotSpacing() {
        return slotSpacing;
    }

    public void render(GuiGraphics guiGraphics, Player player) {
        if (ability == null) {
            return;
        }

        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //RenderSystem.setShaderTexture(0, slotTexture);
        //RenderSystem.enableBlend();

        boolean isOffCooldown = ability.abilityIsOffCooldown(player);

        if (isOffCooldown && !cooldownEndTimes.containsKey(ability)) {
            cooldownEndTimes.put(ability, System.currentTimeMillis());
        }

        long currentTime = System.currentTimeMillis();
        long cooldownEndTime = cooldownEndTimes.getOrDefault(ability, -1L);
        long elapsedTimeSinceCooldownEnd = currentTime - cooldownEndTime;
        boolean showGlow = cooldownEndTime != -1 && elapsedTimeSinceCooldownEnd <= instantRefreshFrameCount * frameDuration;

        int frameIndex = showGlow ? (int) (elapsedTimeSinceCooldownEnd / frameDuration) + 1 : 0;
        frameIndex = Math.min(frameIndex, instantRefreshFrameCount - 1);

        int uvY = frameIndex * slotSize;
        guiGraphics.blit(slotTexture, xPos, yPos, 0, uvY, slotSize, slotSize, slotSize * 3, slotSize * instantRefreshFrameCount);

        //RenderSystem.setShaderTexture(0, ability.getAbilityIcon());
        int iconX = xPos + (slotSize - iconSize) / 2;
        int iconY = yPos + (slotSize - iconSize) / 2;
        guiGraphics.blit(ability.getAbilityIcon(), iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);

        if (!isOffCooldown) {
            cooldownEndTimes.remove(ability);
            double cooldownProgress = ability.getCooldownProgress(player);
            int cooldownHeight = (int) (iconSize * cooldownProgress);
            int cooldownY = iconY + (iconSize - cooldownHeight);
            guiGraphics.fill(iconX, cooldownY, iconX + iconSize, iconY + iconSize, 0x77FFFFFF);
        }

        String keyName = keyBinding.getTranslatedKeyMessage().getString().toUpperCase();
        int keyX = xPos + slotSize - 6;
        int keyY = yPos + slotSize - 6;
        drawKeyWithOutline(guiGraphics, keyName, keyX, keyY);

        RenderSystem.disableBlend();
    }

    private void drawKeyWithOutline(GuiGraphics guiGraphics, String keyName, int x, int y) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, keyName, x + 1, y, 0x000000, false);
        guiGraphics.drawString(font, keyName, x - 1, y, 0x000000, false);
        guiGraphics.drawString(font, keyName, x, y + 1, 0x000000, false);
        guiGraphics.drawString(font, keyName, x, y - 1, 0x000000, false);
        guiGraphics.drawString(font, keyName, x, y, 0xFFFFFF, false);
    }
}

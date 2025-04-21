package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStatesProvider;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;

import java.util.ArrayList;
import java.util.List;

public abstract class TalentTree {
    protected static final int buttonSize = 26;
    protected static final int verticalSpacing = 36;
    protected final List<TalentButton> talentButtons = new ArrayList<>();
    protected final List<TalentButton> visibleButtons = new ArrayList<>();
    protected final List<Runnable> tooltipRenderers = new ArrayList<>();
    protected int scrollY;
    protected boolean isDragging;
    protected int connectionTextureY;
    protected int connectionTextureX;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected boolean isWorkInProgress;
    private final TalentScreen talentScreen;

    private static final ResourceLocation backgroundTexture = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/background.png");
    private static final ResourceLocation borderTexture = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/border.png");

    public TalentTree(int posX, int posY, int width, int height, boolean isWorkInProgress, TalentScreen talentScreen) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.isWorkInProgress = isWorkInProgress;
        this.talentScreen = talentScreen;
    }

    public void init() {
        setupTalentButtons();
        setupConnectionsTexture();
        for (TalentButton talentButton : talentButtons) {
            talentButton.forceUpdateStateBasedOnParents();
        }
        updateButtonPositions();
    }

    public void refreshTree() {
        if (talentScreen != null) {
            talentScreen.initializeTalentTrees();
        }
    }

    protected abstract void setupTalentButtons();

    public List<TalentButton> getTalentButtons() {
        return talentButtons;
    }

    protected <T extends TalentButton> void createTalentButton(T talentButton, TalentButton.TalentState defaultState, TalentButton... parents) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            talentStatesLazyOptional.ifPresent(talentStates -> {
                if (defaultState != null) {
                    talentButton.setState(defaultState);
                } else {
                    TalentButton.TalentState savedState = TalentStates.getState(talentButton.getIdentifier());
                    talentButton.setState(savedState);
                }
                for (TalentButton parent : parents) {
                    talentButton.addParent(parent);
                }
                talentButtons.add(talentButton);
                int savedPoints = TalentStates.getPoints(talentButton.getIdentifier());
                talentButton.setCurrentPoints(savedPoints);
            });
        }
    }

    protected void setupConnectionsTexture() {
        this.connectionTextureY = posY + 13;
        this.connectionTextureX = posX + (width / 2) - 95;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        draw9SliceBackground(guiGraphics, posX, posY, posX + width, posY + height);

        enableScissor(posX, posY, width, height);
        drawConnectionsTexture(guiGraphics);

        updateVisibleButtons();

        for (TalentButton button : visibleButtons) {
            button.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

        disableScissor();

        draw9SliceBorder(guiGraphics, posX, posY, posX + width, posY + height);

        if (isWorkInProgress) {
            renderWorkInProgressMessage(guiGraphics);
        }
        collectTooltips(guiGraphics, mouseX, mouseY);
    }

    private void renderWorkInProgressMessage(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        String message = "Work in Progress";
        PoseStack poseStack = guiGraphics.pose();
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(message);
        int x = posX + (width / 2) - (textWidth / 2);
        int y = posY + (height / 2) - (minecraft.font.lineHeight / 2);

        poseStack.pushPose();
        poseStack.translate(0, 0, 200);
        guiGraphics.drawString(font, message, x, y, 0xFFAAAAAA);
        poseStack.popPose();
    }

    public void updateAllTooltips() {
        for (TalentButton talentButton : talentButtons) {
            talentButton.updateTooltip();
        }
    }

    private void collectTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        tooltipRenderers.clear();
        for (TalentButton button : visibleButtons) {
            if (button.isMouseOver(mouseX, mouseY) && isInsideDraggableArea(mouseX, mouseY)) {
                tooltipRenderers.add(() -> button.renderTooltip(guiGraphics, mouseX, mouseY));
            }
        }
    }

    public void renderTooltips(PoseStack poseStack, int mouseX, int mouseY) {
        for (Runnable tooltipRenderer : tooltipRenderers) {
            tooltipRenderer.run();
        }
    }

    private void updateVisibleButtons() {
        visibleButtons.clear();
        for (TalentButton button : talentButtons) {
            if (isWithinVisibleArea(button)) {
                visibleButtons.add(button);
            }
        }
    }

    private boolean isWithinVisibleArea(TalentButton button) {
        int buttonBottom = button.getY() + buttonSize;
        int buttonTop = button.getY();
        int visibleBottom = posY + height;
        int visibleTop = posY;
        return buttonBottom > visibleTop && buttonTop < visibleBottom;
    }

    protected abstract void drawConnectionsTexture(GuiGraphics guiGraphics);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 || button == 1) {
            for (TalentButton talentButton : talentButtons) {
                if (talentButton.isMouseOver(mouseX, mouseY)) {
                    if (button == 0) {
                        talentButton.onLeftClick(mouseX, mouseY);
                    } else {
                        talentButton.onRightClick(mouseX, mouseY);
                    }
                    return true;
                }
            }
        }
        if (isInsideDraggableArea(mouseX, mouseY)) {
            this.isDragging = true;
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging) {
            this.scrollY += (int) deltaY;
            updateButtonPositions();
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isInsideDraggableArea(mouseX, mouseY)) {
            int scrollAmount = -8;
            this.scrollY += delta > 0 ? -scrollAmount : scrollAmount;
            updateButtonPositions();
            return true;
        }
        return false;
    }

    public int getScrollY() {
        return scrollY;
    }

    private void updateButtonPositions() {
        if (talentButtons.isEmpty()) {
            return;
        }

        int treeHeight = (verticalSpacing * 9) + buttonSize + 7;
        int visibleAreaHeight = height - 20;
        int minY = posY + 13 - (treeHeight - visibleAreaHeight);
        int maxY = posY + 13;

        int firstY = talentButtons.get(0).getY();
        int targetFirstY = Math.min(maxY, Math.max(minY, firstY + this.scrollY));
        int offset = targetFirstY - firstY;

        for (TalentButton button : talentButtons) {
            button.setY(button.getY() + offset);
        }

        this.connectionTextureY += offset;
        this.scrollY = 0;
    }

    private boolean isInsideDraggableArea(double mouseX, double mouseY) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }

    protected void draw9SliceBackground(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2) {
        guiGraphics.blit(backgroundTexture, x1, y1, 0, 0, 16, 16, 48, 48);
        guiGraphics.blit(backgroundTexture, x2 - 16, y1, 32, 0, 16, 16, 48, 48);
        guiGraphics.blit(backgroundTexture, x1, y2 - 16, 0, 32, 16, 16, 48, 48);
        guiGraphics.blit(backgroundTexture, x2 - 16, y2 - 16, 32, 32, 16, 16, 48, 48);
        for (int x = x1 + 16; x < x2 - 16; x += 16) {
            guiGraphics.blit(backgroundTexture, x, y1, 16, 0, 16, 16, 48, 48);
            guiGraphics.blit(backgroundTexture, x, y2 - 16, 16, 32, 16, 16, 48, 48);
        }
        for (int y = y1 + 16; y < y2 - 16; y += 16) {
            guiGraphics.blit(backgroundTexture, x1, y, 0, 16, 16, 16, 48, 48);
            guiGraphics.blit(backgroundTexture, x2 - 16, y, 32, 16, 16, 16, 48, 48);
        }
        for (int x = x1 + 16; x < x2 - 16; x += 16) {
            for (int y = y1 + 16; y < y2 - 16; y += 16) {
                guiGraphics.blit(backgroundTexture, x, y, 16, 16, 16, 16, 48, 48);
            }
        }
    }

    protected void draw9SliceBorder(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2) {
        guiGraphics.blit(borderTexture, x1, y1, 0, 0, 16, 16, 48, 48);
        guiGraphics.blit(borderTexture, x2 - 16, y1, 32, 0, 16, 16, 48, 48);
        guiGraphics.blit(borderTexture, x1, y2 - 16, 0, 32, 16, 16, 48, 48);
        guiGraphics.blit(borderTexture, x2 - 16, y2 - 16, 32, 32, 16, 16, 48, 48);
        for (int x = x1 + 16; x < x2 - 16; x += 16) {
            guiGraphics.blit(borderTexture, x, y1, 16, 0, 16, 16, 48, 48);
            guiGraphics.blit(borderTexture, x, y2 - 16, 16, 32, 16, 16, 48, 48);
        }
        for (int y = y1 + 16; y < y2 - 16; y += 16) {
            guiGraphics.blit(borderTexture, x1, y, 0, 16, 16, 16, 48, 48);
            guiGraphics.blit(borderTexture, x2 - 16, y, 32, 16, 16, 16, 48, 48);
        }
        for (int x = x1 + 16; x < x2 - 16; x += 16) {
            for (int y = y1 + 16; y < y2 - 16; y += 16) {
                guiGraphics.blit(borderTexture, x, y, 16, 16, 16, 16, 48, 48);
            }
        }
    }

    private void enableScissor(int x, int y, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        double scale = minecraft.getWindow().getGuiScale();
        int scissorX = (int) (x * scale);
        int scissorY = (int) (minecraft.getWindow().getScreenHeight() - (y + height) * scale);
        int scissorWidth = (int) (width * scale);
        int scissorHeight = (int) (height * scale);
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    private void disableScissor() {
        RenderSystem.disableScissor();
    }
}

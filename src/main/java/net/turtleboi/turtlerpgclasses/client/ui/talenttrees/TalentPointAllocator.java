package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.turtleboi.turtlecore.network.CoreNetworking;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStatesProvider;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;
import net.turtleboi.turtlecore.network.packet.util.experience.RemoveExperienceC2SPacket;
import net.turtleboi.turtlecore.network.packet.util.experience.UpdateExperienceC2SPacket;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TalentPointAllocator {
    private final int centerX;
    private final int bottomY;
    private final CustomButton buyPointButton;
    private final CustomButton subtractPointButton;
    private final CustomButton confirmPurchaseButton;
    private int pointsToBuy;
    private final List<TalentButton> talentButtons;
    private boolean isExperienceSynced = false;

    public TalentPointAllocator(int centerX, int bottomY, List<TalentButton> talentButtons) {
        this.centerX = centerX;
        this.bottomY = bottomY;
        this.talentButtons = talentButtons;

        this.buyPointButton = createButton(centerX - 24, bottomY, 20, Component.literal("+"), this::handleBuyPoint);
        this.subtractPointButton = createButton(centerX - 54, bottomY, 20, Component.literal("-"), this::handleSubtractPoint);
        this.confirmPurchaseButton = createButton(centerX + 6, bottomY, 48, Component.literal("Confirm"), this::handleConfirmPurchase);

        updateButtonStates();
        requestExperienceSync();
    }

    public void requestExperienceSync() {
        CoreNetworking.sendToServer(new UpdateExperienceC2SPacket());
        //System.out.println("Exp requested");  //debug code
    }

    public void handleExperienceSync(int totalExperience, int experienceLevel, float experienceProgress) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.totalExperience = totalExperience;
            player.experienceLevel = experienceLevel;
            player.experienceProgress = experienceProgress;
            isExperienceSynced = true;
            updateButtonStates();
        }
    }

    private CustomButton createButton(int x, int y, int width, Component title, Consumer<CustomButton> onPress) {
        return new CustomButton(x, y, width, 20, title, onPress);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return buyPointButton.mouseClicked(mouseX, mouseY, button)
                || subtractPointButton.mouseClicked(mouseX, mouseY, button)
                || confirmPurchaseButton.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return buyPointButton.mouseReleased(mouseX, mouseY, button)
                || subtractPointButton.mouseReleased(mouseX, mouseY, button)
                || confirmPurchaseButton.mouseReleased(mouseX, mouseY, button);
    }

    private void testButton(CustomButton button){
        System.out.println("Clicked!");
    }

    private void handleBuyPoint(CustomButton button) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (isExperienceSynced) {
                int currentXP = player.totalExperience;
                int xpNeeded = calculateXPNeeded(pointsToBuy);
                //System.out.println("Current XP: " + currentXP + ", XP Needed: " + xpNeeded); //debug code
                if (player.isCreative()) {
                    pointsToBuy++;
                    //System.out.println("Points to Buy: " + pointsToBuy);  //debug code
                    updateButtonStates();
                } else if (currentXP >= xpNeeded && !player.isCreative()) {
                    pointsToBuy++;
                    //System.out.println("Points to Buy: " + pointsToBuy);  //debug code
                    updateButtonStates();
                }
            } else {
                requestExperienceSync();
            }
        }
    }

    private void handleConfirmPurchase(CustomButton button) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (isExperienceSynced) {

                int currentXP = player.totalExperience;
                int xpNeeded = calculateXPNeeded(pointsToBuy);
                //System.out.println("Current XP: " + currentXP + ", XP Needed: " + xpNeeded);  //debug code
                if (player.isCreative()) {
                    player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(talentStates -> {
                        TalentStates.addPurchasedTalentPoints(pointsToBuy);

                        for (TalentButton talentButton : talentButtons) {
                            talentButton.forceUpdateStateBasedOnParents();
                        }
                    });

                    pointsToBuy = 0;
                    updateButtonStates();
                    isExperienceSynced = false;
                } else if (currentXP >= xpNeeded && !player.isCreative()) {
                    //System.out.println("Confirming purchase of " + pointsToBuy + " points.");  //debug code
                    CoreNetworking.sendToServer(new RemoveExperienceC2SPacket(xpNeeded));
                    player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(talentStates -> {
                        TalentStates.addPurchasedTalentPoints(pointsToBuy);

                        for (TalentButton talentButton : talentButtons) {
                            talentButton.forceUpdateStateBasedOnParents();
                        }
                    });

                    pointsToBuy = 0;
                    updateButtonStates();
                    isExperienceSynced = false;
                }
            } else {
                requestExperienceSync();
            }
        }
    }


    private void handleSubtractPoint(CustomButton button) {
        if (pointsToBuy > 0) {
            pointsToBuy--;
            updateButtonStates();
        }
    }

    private int calculateXPNeeded(int points) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            return talentStatesLazyOptional.map(talentStates -> {
                int totalPointsBought = TalentStates.getPurchasedTalentPoints();
                int totalPoints = totalPointsBought + points;
                return calculateTotalExperienceForLevel(totalPoints) - calculateTotalExperienceForLevel(totalPointsBought);
            }).orElse(0);
        }
        return 0;
    }

    private int calculateTotalExperienceForLevel(int level) {
        if (level <= 16) {
            return 3 * ((level * level) + 6 * level);
        } else if (level <= 31) {
            return (int) (3 * (2.5 * (level * level) - 40.5 * level + 360));
        } else if (level <= 61){
            return (int) (3 * (4.5 * (level * level) - 162.5 * level + 2220));
        } else {
            return (int) (3 * (6.5 * (level * level) - 652 * level + 13690));
        }
    }

    private void updateButtonStates() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int currentXP = player.totalExperience;
            int xpNeeded = calculateXPNeeded(pointsToBuy + 1);
            buyPointButton.active = player.isCreative() || currentXP >= xpNeeded;
            subtractPointButton.active = pointsToBuy > 0;
            confirmPurchaseButton.active = pointsToBuy > 0;
        }
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        buyPointButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        subtractPointButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        confirmPurchaseButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderXPNeededString(guiGraphics);
        renderTotalPointsString(guiGraphics);
    }

    private void renderXPNeededString(GuiGraphics guiGraphics) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int xpNeeded = calculateXPNeeded(pointsToBuy + (buyPointButton.isHoveredOrFocused() ? 1 : 0));
            String xpCostString = "Cost: " + xpNeeded + " XP";
            int stringWidth = Minecraft.getInstance().font.width(xpCostString);
            int xPosition = centerX - (stringWidth / 2);
            int yPosition = bottomY - 20;
            int color = 0xFF0000;

            if (pointsToBuy > 0) {
                color = 0x00FF00;
            }

            if (buyPointButton.isHoveredOrFocused()) {
                int hoverXpNeeded = calculateXPNeeded(pointsToBuy + 1);
                if (player.totalExperience >= hoverXpNeeded) {
                    color = 0x00FF00;
                } else {
                    color = 0xFF0000;
                }
            }

            if (pointsToBuy > 0 || buyPointButton.isHoveredOrFocused()) {
                guiGraphics.drawString(Minecraft.getInstance().font, xpCostString, xPosition, yPosition, color);
            }
        }
    }


    private void renderTotalPointsString(GuiGraphics guiGraphics) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(TalentStatesProvider.TALENT_STATES).ifPresent(talentStates -> {
                int purchasedPoints = TalentStates.getPurchasedTalentPoints();
                int spentPoints = TalentStates.getTotalSpentTalentPoints();
                String totalPointsString = "Talent Points: " + (purchasedPoints - spentPoints) + "/" + (purchasedPoints);
                int stringWidth = Minecraft.getInstance().font.width(totalPointsString);
                int xPosition = centerX - (stringWidth / 2);
                int yPostion = bottomY - 10;

                if (pointsToBuy > 0) {
                    String additionalPointsString = " +" + pointsToBuy;
                    int stringAdditionalWidth = Minecraft.getInstance().font.width(additionalPointsString);
                    int xPostion2 = centerX - ((stringWidth + stringAdditionalWidth) / 2);

                    guiGraphics.drawString(Minecraft.getInstance().font, totalPointsString, xPostion2, yPostion, 0xFFFFFF);
                    guiGraphics.drawString(Minecraft.getInstance().font, additionalPointsString, xPostion2 + stringWidth, yPostion, 0x00FF00);
                } else {
                    guiGraphics.drawString(Minecraft.getInstance().font, totalPointsString, xPosition, yPostion, 0xFFFFFF);
                }
            });
        }
    }

    public List<CustomButton> getButtons() {
        List<CustomButton> buttons = new ArrayList<>();
        buttons.add(buyPointButton);
        buttons.add(subtractPointButton);
        buttons.add(confirmPurchaseButton);
        return buttons;
    }

    private static class CustomButton extends AbstractButton {
        private final Consumer<CustomButton> onPress;

        public CustomButton(int x, int y, int width, int height, Component title, Consumer<CustomButton> onPress) {
            super(x, y, width, height, title);
            this.onPress = onPress;
        }

        @Override
        public void onPress() {
            onPress.accept(this);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
}

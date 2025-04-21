package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStatesProvider;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class TalentButton extends Button {
    private final TalentTree talentTree;
    public int getCurrentPoints = getCurrentPoints();
    private final int originalY;
    private final List<TalentButton> children = new ArrayList<>();
    private final List<TalentButton> parents = new ArrayList<>();
    private List<TalentButton> buttonsToLock = new ArrayList<>();
    private List<Component> tooltipText;
    private TalentState state;
    private final String identifier;
    private int currentPoints = 0;
    public int maxPoints;
    public int requiredPoints;
    private final boolean alwaysActive;

    public TalentButton(TalentTree talentTree, String identifier, int x, int y, int width, int height, int maxPoints, int requiredPoints, Component title, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, title, onPress, createNarration);
        this.talentTree = talentTree;
        this.identifier = identifier;
        this.maxPoints = maxPoints;
        this.requiredPoints = requiredPoints;
        this.alwaysActive = alwaysActive;
        this.state = TalentState.UNLOCKED;
        this.originalY = y;

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            talentStatesLazyOptional.ifPresent(talentStates ->
                    this.currentPoints = TalentStates.getPoints(this.identifier));
        }
        TalentStates.registerTalentButton(this);
    }


    public int getOriginalY() {
        return originalY;
    }

    public void addParent(TalentButton parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
            parent.children.add(this);
        }
    }

    public List<TalentButton> getChildren() {
        return children;
    }

    public List<TalentButton> getParents() {
        return parents;
    }

    public void setState(TalentState newState) {
        this.state = newState;
        for (TalentButton child : children) {
            child.updateStateBasedOnParents();
        }
    }

    public TalentState getState() {
        return state;
    }

    public boolean isActive() {
        return this.getState() == TalentState.ACTIVE;
    }

    public boolean isUnlocked() {
        return this.getState() == TalentState.UNLOCKED;
    }

    public enum TalentState {
        ACTIVE, UNLOCKED, LOCKED, UNIQUE_LOCKED
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isAlwaysActive() {
        return alwaysActive;
    }

    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        if (tooltipText != null && !tooltipText.isEmpty()) {
            assert Minecraft.getInstance().screen != null;
            guiGraphics.renderComponentTooltip(font, tooltipText, mouseX, mouseY);
        } else {
            List<Component> defaultTooltip = Collections.singletonList(Component.literal("Default Tooltip"));
            assert Minecraft.getInstance().screen != null;
            guiGraphics.renderComponentTooltip(font, defaultTooltip, mouseX, mouseY);
        }
    }

    public void setTooltipText(List<Component> text) {
        this.tooltipText = text;
    }

    public void forceUpdateStateBasedOnParents() {
        updateStateBasedOnParents();
    }

    public void updateStateBasedOnParents() {
        if (alwaysActive) {
            return;
        }

        AtomicBoolean isActiveParentPresent = new AtomicBoolean(false);
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            talentStatesLazyOptional.ifPresent(talentStates -> {
                int purchasedPoints = TalentStates.getPurchasedTalentPoints();
                for (TalentButton parent : getParents()) {
                    if (parent.getState() == TalentState.ACTIVE) {
                        isActiveParentPresent.set(true);
                        break;
                    }
                }

                if (isActiveParentPresent.get()) {
                    if (this.getState() != TalentState.ACTIVE && this.getState() != TalentState.UNIQUE_LOCKED && purchasedPoints >= requiredPoints) {
                        unlockOtherButtons();
                        this.setState(TalentState.UNLOCKED);
                        TalentStates.setState(this.getIdentifier(), TalentState.UNLOCKED);
                    }
                } else {
                    if (this.getState() != TalentState.LOCKED && this.getState() != TalentState.UNIQUE_LOCKED) {
                        this.setState(TalentState.LOCKED);
                        TalentStates.setState(this.getIdentifier(), TalentState.LOCKED);
                        setCurrentPoints(0);
                        TalentStates.setPoints(this.getIdentifier(), 0);
                    }
                }
            });
        }
    }

    public void setButtonsToLock(List<TalentButton> buttons) {
        this.buttonsToLock = buttons;
    }

    public void onLeftClick(double mouseX, double mouseY) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            talentStatesLazyOptional.ifPresent(talentStates -> {
                int purchasedPoints = TalentStates.getPurchasedTalentPoints();
                int spentPoints = TalentStates.getTotalSpentTalentPoints();
                if (purchasedPoints != 0 && spentPoints <= (purchasedPoints - 1) && purchasedPoints >= requiredPoints) {
                    if (this.getState() == TalentState.UNLOCKED) {
                        this.setState(TalentState.ACTIVE);
                        TalentStates.setState(this.getIdentifier(), TalentState.ACTIVE);
                        lockOtherButtons();
                    }
                    if (this.getState() == TalentState.ACTIVE && currentPoints < maxPoints) {
                        setCurrentPoints(currentPoints + 1);
                        TalentStates.setPoints(this.getIdentifier(), this.currentPoints);
                    }
                }
            });
        }
        talentTree.updateAllTooltips();
    }

    public void onRightClick(double mouseX, double mouseY) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            talentStatesLazyOptional.ifPresent(talentStates -> {
                if (this.getState() == TalentState.ACTIVE && currentPoints > 0) {
                    setCurrentPoints(currentPoints - 1);
                    TalentStates.setPoints(this.getIdentifier(), this.currentPoints);
                    if (currentPoints == 0) {
                        this.setState(TalentState.UNLOCKED);
                        TalentStates.setState(this.getIdentifier(), TalentState.UNLOCKED);
                    }
                    unlockOtherButtons();
                }
            });
        }
        talentTree.updateAllTooltips();
    }

    private void lockOtherButtons() {
        if (this.getState() == TalentState.ACTIVE) {
            for (TalentButton buttonToLock : buttonsToLock) {
                if (buttonToLock.getState() != TalentState.ACTIVE) {
                    buttonToLock.setState(TalentState.UNIQUE_LOCKED);
                    Player player = Minecraft.getInstance().player;
                    if (player != null) {
                        LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
                        talentStatesLazyOptional.ifPresent(talentStates -> {
                            TalentStates.setState(buttonToLock.getIdentifier(), TalentState.UNIQUE_LOCKED);
                        });
                    }
                }
            }
        }
    }

    private void unlockOtherButtons() {
        if (this.getState() != TalentState.ACTIVE) {
            for (TalentButton buttonToUnlock : buttonsToLock) {
                if (buttonToUnlock.getState() == TalentState.UNIQUE_LOCKED) {
                    boolean isActiveParentPresent = false;
                    for (TalentButton parent : getParents()) {
                        if (parent.getState() == TalentState.ACTIVE) {
                            isActiveParentPresent = true;
                            break;
                        }
                    }

                    if (isActiveParentPresent) {
                        if (buttonToUnlock.getState() != TalentState.ACTIVE) {
                            buttonToUnlock.setState(TalentState.UNLOCKED);
                            Player player = Minecraft.getInstance().player;
                            if (player != null) {
                                LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
                                talentStatesLazyOptional.ifPresent(talentStates -> {
                                    TalentStates.setState(buttonToUnlock.getIdentifier(), TalentState.UNLOCKED);
                                });
                            }
                        }
                    } else {
                        if (buttonToUnlock.getState() != TalentState.LOCKED) {
                            buttonToUnlock.setState(TalentState.LOCKED);
                            Player player = Minecraft.getInstance().player;
                            if (player != null) {
                                LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
                                talentStatesLazyOptional.ifPresent(talentStates -> {
                                    TalentStates.setState(buttonToUnlock.getIdentifier(), TalentState.LOCKED);
                                    buttonToUnlock.setCurrentPoints(0);
                                    TalentStates.setPoints(this.getIdentifier(), 0);
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public int getCurrentPoints() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LazyOptional<TalentStates> talentStatesLazyOptional = player.getCapability(TalentStatesProvider.TALENT_STATES);
            return talentStatesLazyOptional.map(talentStates -> talentStates.getPoints(this.identifier)).orElse(0);
        }
        return 0;
    }

    public void setCurrentPoints(int points) {
        this.currentPoints = Math.max(0, Math.min(points, maxPoints));
    }

    public void updateTooltip() {
        this.setTooltipText(generateDynamicTooltip());
    }

    public abstract List<Component> generateDynamicTooltip();

    protected MutableComponent buildValueComponent(int currentPoints, int maxPoints, boolean isShiftPressed,
                                                   Function<Integer, Double> valueGetter) {
        double value = valueGetter.apply(currentPoints);
        double nextValue = isShiftPressed && currentPoints < maxPoints ? valueGetter.apply(currentPoints + 1) : value;

        StringBuilder ranks = new StringBuilder();
        for (int i = 1; i <= maxPoints; i++) {
            if (i > 1) {
                ranks.append("/");
            }
            ranks.append(valueGetter.apply(i));
        }

        return currentPoints != 0
                ? Component.literal(" " + nextValue + " ")
                .withStyle(isShiftPressed && currentPoints < maxPoints
                        ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))
                        : Style.EMPTY)
                : Component.literal(" " + ranks + " ")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00")));
    }

    protected MutableComponent buildValuePercentComponent(int currentPoints, int maxPoints, boolean isShiftPressed,
                                                   Function<Integer, Double> valueGetter) {
        double value = valueGetter.apply(currentPoints);
        double nextValue = isShiftPressed && currentPoints < maxPoints ? valueGetter.apply(currentPoints + 1) : value;

        StringBuilder ranks = new StringBuilder();
        for (int i = 1; i <= maxPoints; i++) {
            if (i > 1) {
                ranks.append("/");
            }
            ranks.append(valueGetter.apply(i)).append("%");
        }

        return currentPoints != 0
                ? Component.literal(" " + nextValue + "% ")
                .withStyle(isShiftPressed && currentPoints < maxPoints
                        ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))
                        : Style.EMPTY)
                : Component.literal(" " + ranks + " ")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00")));
    }

    protected MutableComponent buildPlusValueComponent(int currentPoints, int maxPoints, boolean isShiftPressed,
                                                       Function<Integer, Double> valueGetter) {
        double value = valueGetter.apply(currentPoints);
        double nextValue = isShiftPressed && currentPoints < maxPoints ? valueGetter.apply(currentPoints + 1) : value;

        StringBuilder ranks = new StringBuilder();
        for (int i = 1; i <= maxPoints; i++) {
            if (i > 1) {
                ranks.append("/");
            }
            ranks.append(valueGetter.apply(i));
        }

        return currentPoints != 0
                ? Component.literal(" +" + nextValue + " ")
                .withStyle(isShiftPressed && currentPoints < maxPoints
                        ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))
                        : Style.EMPTY)
                : Component.literal(" +" + ranks + " ")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00")));
    }

    protected MutableComponent buildPlusValuePercentComponent(int currentPoints, int maxPoints, boolean isShiftPressed,
                                                          Function<Integer, Double> valueGetter) {
        double value = valueGetter.apply(currentPoints);
        double nextValue = isShiftPressed && currentPoints < maxPoints ? valueGetter.apply(currentPoints + 1) : value;

        StringBuilder ranks = new StringBuilder();
        for (int i = 1; i <= maxPoints; i++) {
            if (i > 1) {
                ranks.append("/");
            }
            ranks.append(valueGetter.apply(i)).append("%");
        }

        return currentPoints != 0
                ? Component.literal(" +" + nextValue + "% ")
                .withStyle(isShiftPressed && currentPoints < maxPoints
                        ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))
                        : Style.EMPTY)
                : Component.literal(" +" + ranks + " ")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00")));
    }

    protected MutableComponent buildRankedStringComponent(int currentPoints, int maxPoints, boolean isShiftPressed,
                                                          Function<Integer, String> valueGetter, String hexcode) {
        String value = valueGetter.apply(currentPoints);
        String nextValue = isShiftPressed && currentPoints < maxPoints ? valueGetter.apply(currentPoints + 1) : value;

        StringBuilder ranks = new StringBuilder();
        for (int i = 1; i <= maxPoints; i++) {
            if (i > 1) {
                ranks.append("/");
            }
            ranks.append(valueGetter.apply(i));
        }

        return currentPoints != 0
                ? Component.literal(" " + nextValue + " ")
                .withStyle(isShiftPressed && currentPoints < maxPoints
                        ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00"))
                        : Style.EMPTY.withColor(TextColor.parseColor(hexcode)))
                : Component.literal(" " + ranks + " ")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00FF00")));
    }
}



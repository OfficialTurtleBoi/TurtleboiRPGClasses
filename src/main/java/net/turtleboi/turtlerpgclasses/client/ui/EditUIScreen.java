package net.turtleboi.turtlerpgclasses.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.turtleboi.turtlerpgclasses.client.ui.cooldowns.CooldownOverlay;
import net.turtleboi.turtlerpgclasses.config.UIConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EditUIScreen extends Screen {
    private final List<MovableUIComponent> movableComponents;

    public EditUIScreen() {
        super(Component.literal("Edit UI"));
        this.movableComponents = new ArrayList<>();
    }

    //@Override
    //protected void init() {
    //    this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 30, 200, 20, Component.literal("Save"), button -> {
    //        savePositions();
    //        this.minecraft.setScreen(null);
    //    }, (Supplier<MutableComponent> def) -> Component.empty()));
//
    //    MovableUIComponent mainResourceComponent =
    //            new MovableUIComponent(UIConfig.resourceMainX.get(),
    //                    UIConfig.resourceMainY.get(),
    //                    80,
    //                    15,
    //                    Component.literal("Main Resource Bar"),
    //                    UIConfig.resourceMainAnchor.get());
    //    mainResourceComponent.updatePosition(UIConfig.resourceMainX.get(), UIConfig.resourceMainY.get());
    //    MovableUIComponent secondaryResourceComponent =
    //            new MovableUIComponent(UIConfig.resourceSecondaryX.get(),
    //                    UIConfig.resourceSecondaryY.get(),
    //                    80,
    //                    15,
    //                    Component.literal("Secondary Resource Bar"),
    //                    UIConfig.resourceSecondaryAnchor.get());
    //    secondaryResourceComponent.updatePosition(UIConfig.resourceSecondaryX.get(), UIConfig.resourceSecondaryY.get());
    //    MovableUIComponent cooldownComponent =
    //            new MovableUIComponent(UIConfig.cooldownX.get(),
    //                    UIConfig.cooldownY.get(),
    //                    104,
    //                    22,
    //                    Component.literal("Cooldown Slots"),
    //                    UIConfig.cooldownAnchor.get());
    //    cooldownComponent.updatePosition(UIConfig.cooldownX.get(), UIConfig.cooldownY.get());
//
    //    this.movableComponents.add(mainResourceComponent);
    //    this.movableComponents.add(secondaryResourceComponent);
    //    this.movableComponents.add(cooldownComponent);
//
    //    this.addRenderableWidget(mainResourceComponent);
    //    this.addRenderableWidget(secondaryResourceComponent);
    //    this.addRenderableWidget(cooldownComponent);
    //}

    //@Override
    //public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    //    this.renderBackground(poseStack);
    //    super.render(poseStack, mouseX, mouseY, partialTick);
    //}

    //private void savePositions() {
    //    for (MovableUIComponent component : movableComponents) {
    //        if ("Main Resource Bar".equals(component.getIdentifier())) {
    //            UIConfig.resourceMainX.set(component.x);
    //            UIConfig.resourceMainY.set(component.y);
    //            UIConfig.resourceMainAnchor.set(component.getAnchor());
    //        } else if ("Secondary Resource Bar".equals(component.getIdentifier())) {
    //            UIConfig.resourceSecondaryX.set(component.x);
    //            UIConfig.resourceSecondaryY.set(component.y);
    //            UIConfig.resourceSecondaryAnchor.set(component.getAnchor());
    //        } else if ("Cooldown Slots".equals(component.getIdentifier())) {
    //            UIConfig.cooldownX.set(component.x);
    //            UIConfig.cooldownY.set(component.y);
    //            UIConfig.cooldownAnchor.set(component.getAnchor());
    //        }
    //    }
    //    UIConfig.spec.save();
    //    CooldownOverlay.initializeSlots(getMinecraft().player);
    //}
}

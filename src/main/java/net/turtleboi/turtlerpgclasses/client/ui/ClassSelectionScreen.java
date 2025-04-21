package net.turtleboi.turtlerpgclasses.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.client.ui.cooldowns.CooldownOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.resources.ResourceOverlay;
import net.turtleboi.turtlerpgclasses.network.ModNetworking;
import net.turtleboi.turtlerpgclasses.network.packet.ClassSelectionC2SPacket;
import net.turtleboi.turtlerpgclasses.rpg.classes.Mage;
import net.turtleboi.turtlerpgclasses.rpg.classes.Ranger;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.ranges.Range;

import java.util.function.Supplier;

public class ClassSelectionScreen extends Screen {

    public ClassSelectionScreen() {
        super(Component.translatable("SelectClass"));
    }

    String warrior = Component.translatable("class.warrior.name").getString();
    String ranger = Component.translatable("class.ranger.name").getString();
    String mage = Component.translatable("class.mage.name").getString();

    @Override
    protected void init() {
        super.init();
        int buttonWidth = 128;
        int buttonHeight = 144;
        int buttonSpacing = 16;

        int startX = this.width / 2 - (3 * buttonWidth + 2 * buttonSpacing) / 2;

        this.addRenderableWidget(new ClassButton(
                startX,
                this.height / 2 - buttonHeight / 2,
                buttonWidth,
                buttonHeight,
                Component.translatable("class.warrior.name"),
                Component.translatable("class.warrior.description"),
                Component.translatable("class.warrior.features"),
                button ->
                        handleClassSelection(warrior),
                (Supplier<MutableComponent> def) -> Component.empty()
        ));

        startX += buttonWidth + buttonSpacing;

        this.addRenderableWidget(new ClassButton(
                startX,
                this.height / 2 - buttonHeight / 2,
                buttonWidth,
                buttonHeight,
                Component.translatable("class.ranger.name"),
                Component.translatable("class.ranger.description"),
                Component.translatable("class.ranger.features"),
                button ->
                        handleClassSelection(ranger),
                (Supplier<MutableComponent> def) -> Component.empty()
        ));

        startX += buttonWidth + buttonSpacing;

        this.addRenderableWidget(new ClassButton(
                startX,
                this.height / 2 - buttonHeight / 2,
                buttonWidth,
                buttonHeight,
                Component.translatable("class.mage.name"),
                Component.translatable("class.mage.description"),
                Component.translatable("class.mage.features"),
                button ->
                        handleClassSelection(mage),
                (Supplier<MutableComponent> def) -> Component.empty()
        ));
    }

    private void handleClassSelection(String className) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            TalentStates.resetAllTalents(player);
            TalentStates.setPurchasedTalentPoints(player, 0);
                ModNetworking.sendToServer(new ClassSelectionC2SPacket(className, null));
                if (warrior.equals(className)) {
                    new Warrior().setActive(player);
                } else if (ranger.equals(className)) {
                    new Ranger().setActive(player);
                } else if (mage.equals(className)) {
                    new Mage().setActive(player);
                }
            CooldownOverlay.initializeSlots(player);
            ResourceOverlay.initializeResourceBars(player);
                this.onClose();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawCenteredString(font, this.title, this.width / 2, 20, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}

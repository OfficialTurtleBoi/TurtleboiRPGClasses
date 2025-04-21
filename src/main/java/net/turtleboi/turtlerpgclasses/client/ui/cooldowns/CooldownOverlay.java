package net.turtleboi.turtlerpgclasses.client.ui.cooldowns;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.MovableUIComponent;
import net.turtleboi.turtlerpgclasses.config.UIConfig;
import net.turtleboi.turtlerpgclasses.rpg.talents.ActiveAbility;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheBarbarianSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheJuggernautSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfThePaladinSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.*;
import net.turtleboi.turtlerpgclasses.util.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class CooldownOverlay {
    private static final List<CooldownSlot> cooldownSlots = new ArrayList<>();

    private static final KeyMapping[] KEY_BINDINGS = {
            KeyBinding.ACTIVE1,
            KeyBinding.ACTIVE2,
            KeyBinding.ACTIVE3,
            KeyBinding.ACTIVE4
    };

    public static final IGuiOverlay HUD_COOLDOWNS = (gui, guiGraphics, partialTick, width, height) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            for (CooldownSlot slot : cooldownSlots) {
                slot.render(guiGraphics, player);
            }
        }
    };

    public static void initializeSlots(Player player) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int[] startCoords = calculateStartCoordinates(screenWidth, screenHeight, UIConfig.cooldownX.get(), UIConfig.cooldownY.get(), MovableUIComponent.Anchor.valueOf(UIConfig.cooldownAnchor.get()));
        int cooldownX = startCoords[0];
        int cooldownY = startCoords[1];

        int cooldownSlotSize = CooldownSlot.getSlotSize();
        int cooldownSlotSpacing = CooldownSlot.getSlotSpacing();

        boolean isHorizontal = "HORIZONTAL".equalsIgnoreCase(UIConfig.cooldownOrientation.get());

        cooldownSlots.clear();
        for (int i = 0; i < 4; i++) {
            if (isHorizontal) {
                cooldownSlots.add(new CooldownSlot(cooldownX + i * (cooldownSlotSize + cooldownSlotSpacing), cooldownY, KEY_BINDINGS[i], null));
            } else {
                cooldownSlots.add(new CooldownSlot(cooldownX, cooldownY + i * (cooldownSlotSize + cooldownSlotSpacing), KEY_BINDINGS[i], null));
            }
        }

        List<ActiveAbility> activeAbilities = getActiveAbilities(player);
        for (int i = 0; i < activeAbilities.size() && i < cooldownSlots.size(); i++) {
            cooldownSlots.get(i).setAbility(activeAbilities.get(i));
        }
    }

    private static List<ActiveAbility> getActiveAbilities(Player player) {
        List<ActiveAbility> activeAbilities = new ArrayList<>();
        if (new PathOfTheBarbarianSubclass().isActive(player)) {
            activeAbilities.add(new UnleashFuryTalent());
        } else if (new PathOfTheJuggernautSubclass().isActive(player)) {
            activeAbilities.add(new SteelBarbsTalent());
        } else if (new PathOfThePaladinSubclass().isActive(player)) {
            activeAbilities.add(new DivineSanctuaryTalent());
        }
        if (new ChargeTalent().isActive(player)){
            if (new StampedeTalent().isActive(player)){
                activeAbilities.add(new StampedeTalent());
            } else {
                activeAbilities.add(new ChargeTalent());
            }
        } else if (new TauntTalent().isActive(player)) {
            if (new IntimidatingPresenceTalent().isActive(player)){
                activeAbilities.add(new IntimidatingPresenceTalent());
            } else {
                activeAbilities.add(new TauntTalent());
            }
        }
        if (new ColossusTalent().isActive(player)) {
            activeAbilities.add(new ColossusTalent());
        } else if (new ExecuteTalent().isActive(player)){
            activeAbilities.add(new ExecuteTalent());
        }
        if (new WarlordsPresenceTalent().isActive(player)) {
            activeAbilities.add(new WarlordsPresenceTalent());
        } else if (new GuardiansOathTalent().isActive(player)){
            activeAbilities.add(new GuardiansOathTalent());
        }
        return activeAbilities;
    }

    private static int[] calculateStartCoordinates(int screenWidth, int screenHeight, int offsetX, int offsetY, MovableUIComponent.Anchor anchor) {
        int x = 0, y = 0;
        int cooldownSlotSize = CooldownSlot.getSlotSize();
        int cooldownSlotSpacing = CooldownSlot.getSlotSpacing();
        boolean isHorizontal = "HORIZONTAL".equalsIgnoreCase(UIConfig.cooldownOrientation.get());
        int totalWidth = isHorizontal ? (cooldownSlotSize * 4) + (cooldownSlotSpacing * 3) : cooldownSlotSize;
        int totalHeight = isHorizontal ? cooldownSlotSize : (cooldownSlotSize * 4) + (cooldownSlotSpacing * 3);
        switch (anchor) {
            case TOP_LEFT:
                x = offsetX;
                y = offsetY;
                break;
            case TOP_RIGHT:
                x = screenWidth - offsetX - totalWidth;
                y = offsetY;
                break;
            case BOTTOM_LEFT:
                x = offsetX;
                y = screenHeight - offsetY - totalHeight;
                break;
            case BOTTOM_RIGHT:
                x = screenWidth - offsetX - totalWidth;
                y = screenHeight - offsetY - totalHeight;
                break;
            case TOP_CENTER:
                x = (screenWidth / 2) + offsetX;
                y = offsetY;
                break;
            case BOTTOM_CENTER:
                x = (screenWidth / 2) + offsetX;
                y = screenHeight - offsetY - totalHeight;
                break;
            case CENTER:
                x = (screenWidth / 2) + offsetX;
                y = (screenHeight / 2) + offsetY;
                break;
        }
        return new int[]{x, y};
    }
}

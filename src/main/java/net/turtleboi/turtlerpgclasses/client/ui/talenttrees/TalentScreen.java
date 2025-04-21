package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlerpgclasses.capabilities.PlayerClassProvider;
import net.turtleboi.turtlerpgclasses.capabilities.talents.TalentStates;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.client.ui.cooldowns.CooldownOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.resources.ResourceOverlay;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;
import net.turtleboi.turtlerpgclasses.rpg.classes.Mage;
import net.turtleboi.turtlerpgclasses.rpg.classes.Ranger;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.classes.subclasses.Barbarian;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheBarbarianSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfTheJuggernautSubclass;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.PathOfThePaladinSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TalentScreen extends Screen {
    private final List<TalentTree> talentTrees = new ArrayList<>();
    private TalentPointAllocator talentPointAllocator;

    public TalentScreen() {
        super(Component.literal("Talent Trees"));
    }

    @Override
    protected void init() {
        super.init();
        //System.out.println("Initializing " + this.getClass().getSimpleName());  //debug code
        initializeTalentTrees();
        if (talentPointAllocator != null) {
            talentPointAllocator.requestExperienceSync();
        }
    }

    public void initializeTalentTrees() {
        Player player = getMinecraft().player;
        assert player != null;
        int screenWidth = this.width;
        int screenHeight = this.height;

        talentTrees.clear();

        if (new Warrior().isActive(player)) {
            initializeTalentTree(WarriorTalentTree.class, true);
            if (new PathOfTheBarbarianSubclass().isActive(player)) {
                initializeTalentTree(BarbarianTalentTree.class, false);
            } else if (new PathOfTheJuggernautSubclass().isActive(player)) {
                initializeTalentTree(JuggernautTalentTree.class, false);
            } else if (new PathOfThePaladinSubclass().isActive(player)) {
                initializeTalentTree(PaladinTalentTree.class, false);
            }
        } else if (new Ranger().isActive(player)) {
            initializeTalentTree(RangerTalentTree.class, true);
        } else if (new Mage().isActive(player)) {
            initializeTalentTree(MageTalentTree.class, true);
        } else {
            return;
        }
        setupTalentPointAllocator(screenWidth / 2, screenHeight - 30);
    }

    public void initializeTalentTree(Class<? extends TalentTree> treeClass, Boolean classTree) {
        int buttonSize = 26;
        int verticalSpacing = 36;
        int screenWidth = this.width;
        int screenHeight = this.height;
        int treeWidth = 200;
        int treeHeight = Math.min(((3 * screenHeight) / 4) - (buttonSize / 2) + 2, (verticalSpacing * 10) + 19);
        int classTreeXOffset = screenWidth / 4 - treeWidth / 2;
        int treeXOffset = classTree ? screenWidth / 4 - treeWidth / 2 : screenWidth - classTreeXOffset - treeWidth;
        int treeYOffset = screenHeight / 10;

        try {TalentTree talentTree = treeClass.getConstructor(int.class, int.class, int.class, int.class, TalentScreen.class)
                    .newInstance(treeXOffset, treeYOffset, treeWidth, treeHeight, this);
            //System.out.println(treeClass.getSimpleName() + " loading...");  //debug code
            talentTree.init();
            talentTrees.add(talentTree);
            //System.out.println(treeClass.getSimpleName() + " loaded!");  //debug code
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Failed to load " + treeClass.getSimpleName());  //debug code
        }
    }

    public void clearTalentTree(Class<? extends TalentTree> talentTreeClass) {
        //System.out.println("Clearing Talent Tree: " + talentTreeClass.getSimpleName());  //debug code
        Iterator<TalentTree> iterator = talentTrees.iterator();
        while (iterator.hasNext()) {
            TalentTree tree = iterator.next();
            if (tree.getClass().equals(talentTreeClass)) {
                tree.getTalentButtons().clear();
                iterator.remove();
                break;
            }
        }
    }

    private void setupTalentPointAllocator(int centerX, int bottomY) {
        List<TalentButton> allTalentButtons = new ArrayList<>();
        for (TalentTree tree : talentTrees) {
            allTalentButtons.addAll(tree.getTalentButtons());
        }
        talentPointAllocator = new TalentPointAllocator(centerX, bottomY, allTalentButtons);
    }

    public TalentPointAllocator getTalentPointAllocator() {
        return talentPointAllocator;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (talentPointAllocator != null && talentPointAllocator.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        for (TalentTree tree : talentTrees) {
            if (tree.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (talentPointAllocator != null && talentPointAllocator.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        for (TalentTree tree : talentTrees) {
            if (tree.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (TalentTree tree : talentTrees) {
            if (tree.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        for (TalentTree tree : talentTrees) {
            if (tree.mouseScrolled(mouseX, mouseY, delta)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        //System.out.println("Rendering TalentScreen..."); //debug code
        this.renderBackground(guiGraphics);
        for (TalentTree tree : talentTrees) {
            tree.render(guiGraphics, mouseX, mouseY, partialTicks);
            //System.out.println("Rendered a TalentTree: " + tree.getClass().getSimpleName()); //debug code
        }
        for (TalentTree tree : talentTrees) {
            tree.renderTooltips(guiGraphics.pose(), mouseX, mouseY);
        }

        if (talentPointAllocator != null) {
            talentPointAllocator.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        this.init(minecraft, width, height);
        initializeTalentTrees();
    }

    @Override
    public void onClose() {
        super.onClose();
        CooldownOverlay.initializeSlots(getMinecraft().player);
        ResourceOverlay.initializeResourceBars(getMinecraft().player);
    }
}

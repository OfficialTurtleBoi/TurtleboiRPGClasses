package net.turtleboi.turtlerpgclasses.client.ui.talenttrees;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ClientClassData;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.TalentButton;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.common.*;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior.*;
import net.turtleboi.turtlerpgclasses.rpg.classes.Warrior;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class WarriorTalentTree extends TalentTree {
    private static final ResourceLocation WARRIOR_TREE_CONNECTIONS = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/warrior_tree_connections.png");

    public WarriorTalentTree(int posX, int posY, int width, int height, TalentScreen talentScreen) {
        super(posX, posY, width, height, false, talentScreen);
    }

    @Override
    protected void drawConnectionsTexture(GuiGraphics guiGraphics) {
        guiGraphics.blit(WARRIOR_TREE_CONNECTIONS, this.connectionTextureX, this.connectionTextureY, 0, 0, 190, 350, 190, 350);
    }

    @Override
    protected void setupTalentButtons() {
        int centerX = posX + (width / 2) - 13;
        int startY = posY + 13;
        String className = ClientClassData.getPlayerClass();
        warriorClassNode warriorClassNode = new warriorClassNode(
                this,
                new Warrior(),
                centerX,
                startY,
                1,
                0,
                ("Warrior".equals(className)),
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorClassNode, TalentButton.TalentState.ACTIVE);

        // Tier 2
        VigorTalentNode warriorVigorTalent = new VigorTalentNode(
                this,
                new VigorTalent(),
                centerX - buttonSize,
                startY + verticalSpacing,
                4,
                0,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        MightyBlowsTalentNode warriorMightyBlowsTalent = new MightyBlowsTalentNode(
                this,
                new MightyBlowsTalent(),
                centerX + buttonSize,
                startY + verticalSpacing,
                4,
                0,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorVigorTalent, null, warriorClassNode);
        createTalentButton(warriorMightyBlowsTalent, null, warriorClassNode);

        // Tier 3
        BattleHardenedTalentNode warriorBattleHardenedTalent = new BattleHardenedTalentNode(
                this,
                new BattleHardenedTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        SteadyFootingTalentNode warriorSteadyFootingTalent = new SteadyFootingTalentNode(
                this,
                new SteadyFootingTalent(),
                centerX,
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        SwiftHandsTalentNode warriorSwiftHandsTalent = new SwiftHandsTalentNode(
                this,
                new SwiftHandsTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorBattleHardenedTalent, null, warriorVigorTalent, warriorMightyBlowsTalent);
        createTalentButton(warriorSteadyFootingTalent, null, warriorVigorTalent, warriorMightyBlowsTalent);
        createTalentButton(warriorSwiftHandsTalent, null, warriorVigorTalent, warriorMightyBlowsTalent);

        // Tier 4
        pathOfTheBarbarianSubclassNode pathOfTheBarbarianSubclass = new pathOfTheBarbarianSubclassNode(
                this,
                new PathOfTheBarbarianSubclass(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        pathOfTheJuggernautSubclassNode pathOfTheJuggernautSubclass = new pathOfTheJuggernautSubclassNode(
                this,
                new PathOfTheJuggernautSubclass(),
                centerX,
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        pathOfThePaladinSubclassNode pathOfThePaladinSubclass = new pathOfThePaladinSubclassNode(
                this,
                new PathOfThePaladinSubclass(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(pathOfTheBarbarianSubclass, null, warriorBattleHardenedTalent, warriorSteadyFootingTalent, warriorSwiftHandsTalent);
        createTalentButton(pathOfTheJuggernautSubclass, null, warriorBattleHardenedTalent, warriorSteadyFootingTalent, warriorSwiftHandsTalent);
        createTalentButton(pathOfThePaladinSubclass, null, warriorBattleHardenedTalent, warriorSteadyFootingTalent, warriorSwiftHandsTalent);

        pathOfTheBarbarianSubclass.setButtonsToLock(Arrays.asList(pathOfTheJuggernautSubclass, pathOfThePaladinSubclass));
        pathOfTheJuggernautSubclass.setButtonsToLock(Arrays.asList(pathOfTheBarbarianSubclass, pathOfThePaladinSubclass));
        pathOfThePaladinSubclass.setButtonsToLock(Arrays.asList(pathOfTheBarbarianSubclass, pathOfTheJuggernautSubclass));

        // Tier 5
        QuickRecoveryTalentNode warriorQuickRecoveryTalent = new QuickRecoveryTalentNode(
                this,
                new QuickRecoveryTalent(),
                centerX - (buttonSize * 3),
                startY + (verticalSpacing * 4),
                4,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        SecondWindTalentNode warriorSecondWindTalent = new SecondWindTalentNode(
                this,
                new SecondWindTalent(),
                centerX - buttonSize,
                startY + (verticalSpacing * 4),
                5,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        FocusedStrikesTalentNode warriorFocusedStrikesTalent = new FocusedStrikesTalentNode(
                this,
                new FocusedStrikesTalent(),
                centerX + buttonSize,
                startY + (verticalSpacing * 4),
                5,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        MarathonerTalentNode warriorMarathonerTalent = new MarathonerTalentNode(
                this,
                new MarathonerTalent(),
                centerX + (buttonSize * 3),
                startY + (verticalSpacing * 4),
                4,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorQuickRecoveryTalent, null, pathOfTheBarbarianSubclass, pathOfTheJuggernautSubclass, pathOfThePaladinSubclass);
        createTalentButton(warriorSecondWindTalent, null, pathOfTheBarbarianSubclass, pathOfTheJuggernautSubclass, pathOfThePaladinSubclass);
        createTalentButton(warriorFocusedStrikesTalent, null, pathOfTheBarbarianSubclass, pathOfTheJuggernautSubclass, pathOfThePaladinSubclass);
        createTalentButton(warriorMarathonerTalent, null, pathOfTheBarbarianSubclass, pathOfTheJuggernautSubclass, pathOfThePaladinSubclass);

        warriorSecondWindTalent.setButtonsToLock(List.of(warriorFocusedStrikesTalent));
        warriorFocusedStrikesTalent.setButtonsToLock(List.of(warriorSecondWindTalent));

        // Tier 6
        LifeLeechTalentNode warriorLifeLeechTalent = new LifeLeechTalentNode(
                this,
                new LifeLeechTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        StaminaMasteryTalentNode warriorStaminaMasteryTalent = new StaminaMasteryTalentNode(
                this,
                new StaminaMasteryTalent(),
                centerX,
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        BrawlersTenacityTalentNode warriorBrawlersTenacityTalent = new BrawlersTenacityTalentNode(
                this,
                new BrawlersTenacityTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorLifeLeechTalent, null, warriorQuickRecoveryTalent, warriorSecondWindTalent, warriorFocusedStrikesTalent, warriorMarathonerTalent);
        createTalentButton(warriorStaminaMasteryTalent, null, warriorQuickRecoveryTalent, warriorSecondWindTalent, warriorFocusedStrikesTalent, warriorMarathonerTalent);
        createTalentButton(warriorBrawlersTenacityTalent, null, warriorQuickRecoveryTalent, warriorSecondWindTalent, warriorFocusedStrikesTalent, warriorMarathonerTalent);

        warriorLifeLeechTalent.setButtonsToLock(List.of(warriorBrawlersTenacityTalent));
        warriorBrawlersTenacityTalent.setButtonsToLock(List.of(warriorLifeLeechTalent));

        // Tier 7
        ChargeTalentNode warriorChargeTalent = new ChargeTalentNode(
                this,
                new ChargeTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 6),
                1,
                21,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        TauntTalentNode warriorTauntTalent = new TauntTalentNode(
                this,
                new TauntTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 6),
                1,
                21,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorChargeTalent, null, warriorLifeLeechTalent, warriorStaminaMasteryTalent, warriorBrawlersTenacityTalent);
        createTalentButton(warriorTauntTalent, null, warriorLifeLeechTalent, warriorStaminaMasteryTalent, warriorBrawlersTenacityTalent);

        warriorChargeTalent.setButtonsToLock(List.of(warriorTauntTalent));
        warriorTauntTalent.setButtonsToLock(List.of(warriorChargeTalent));

        // Tier 8
        StampedeTalentNode warriorStampedeTalent = new StampedeTalentNode(
                this,
                new StampedeTalent(),
                centerX - (buttonSize * 3),
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        MomentumTalentNode warriorMomentumTalent = new MomentumTalentNode(
                this,
                new MomentumTalent(),
                centerX - buttonSize,
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        VictoriousCryTalentNode warriorVictoriousCryTalent = new VictoriousCryTalentNode(
                this,
                new VictoriousCryTalent(),
                centerX + buttonSize,
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        IntimidatingPresenceTalentNode warriorIntimidatingPresenceTalent = new IntimidatingPresenceTalentNode(
                this,
                new IntimidatingPresenceTalent(),
                centerX + (buttonSize * 3),
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(warriorStampedeTalent, null, warriorChargeTalent);
        createTalentButton(warriorMomentumTalent, null, warriorChargeTalent);
        createTalentButton(warriorVictoriousCryTalent, null, warriorTauntTalent);
        createTalentButton(warriorIntimidatingPresenceTalent, null, warriorTauntTalent);

        warriorStampedeTalent.setButtonsToLock(List.of(warriorMomentumTalent));
        warriorMomentumTalent.setButtonsToLock(List.of(warriorStampedeTalent));
        warriorVictoriousCryTalent.setButtonsToLock(List.of(warriorIntimidatingPresenceTalent));
        warriorIntimidatingPresenceTalent.setButtonsToLock(List.of(warriorVictoriousCryTalent));

        // Tier 9
        ColossusTalentNode warriorColossusTalent = new ColossusTalentNode(
                this,
                new ColossusTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 8),
                1,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        CombatVeteranTalentNode warriorCombatVeteranTalent = new CombatVeteranTalentNode(
                this,
                new CombatVeteranTalent(),
                centerX,
                startY + (verticalSpacing * 8),
                5,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        ExecuteTalentNode warriorExecuteTalent = new ExecuteTalentNode(
                this,
                new ExecuteTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 8),
                1,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        createTalentButton(warriorColossusTalent, null, warriorStampedeTalent, warriorMomentumTalent, warriorVictoriousCryTalent, warriorIntimidatingPresenceTalent);
        createTalentButton(warriorCombatVeteranTalent, null, warriorStampedeTalent, warriorMomentumTalent, warriorVictoriousCryTalent, warriorIntimidatingPresenceTalent);
        createTalentButton(warriorExecuteTalent, null, warriorStampedeTalent, warriorMomentumTalent, warriorVictoriousCryTalent, warriorIntimidatingPresenceTalent);

        warriorColossusTalent.setButtonsToLock(List.of(warriorExecuteTalent));
        warriorExecuteTalent.setButtonsToLock(List.of(warriorColossusTalent));

        // Tier 10
        WarlordsPresenceTalentNode warriorWarlordsPresenceTalent = new WarlordsPresenceTalentNode(
                this,
                new WarlordsPresenceTalent(),
                centerX - buttonSize,
                startY + (verticalSpacing * 9),
                1,
                40,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        GuardiansOathTalentNode warriorGuardiansOathTalent = new GuardiansOathTalentNode(
                this,
                new GuardiansOathTalent(),
                centerX + buttonSize,
                startY + (verticalSpacing * 9),
                1,
                40,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        createTalentButton(warriorWarlordsPresenceTalent, null, warriorColossusTalent, warriorCombatVeteranTalent, warriorExecuteTalent);
        createTalentButton(warriorGuardiansOathTalent, null, warriorColossusTalent, warriorCombatVeteranTalent, warriorExecuteTalent);

        warriorWarlordsPresenceTalent.setButtonsToLock(List.of(warriorGuardiansOathTalent));
        warriorGuardiansOathTalent.setButtonsToLock(List.of(warriorWarlordsPresenceTalent));
    }

    //private void drawDropShadowsForConnections(PoseStack poseStack, TalentButton button) {
    //    if (!isInsideDraggableArea(button.x, button.y + VERTICAL_SPACING)) {
    //        return; // Skip if the parent button is not in the visible area
    //    }

    //    for (TalentButton child : button.getChildren()) {
    //        if (isInsideDraggableArea(child.x, child.y)) {
    //            drawDropShadows(poseStack, button.x + BUTTON_SIZE / 2, button.y + BUTTON_SIZE, child.x + BUTTON_SIZE / 2, child.y);
    //            drawDropShadowsForConnections(poseStack, child); // Recursive call
    //        }
    //    }
    //}

    //private void drawWhiteLinesForConnections(PoseStack poseStack, TalentButton button) {
    //    if (!isInsideDraggableArea(button.x, button.y + VERTICAL_SPACING)) {
    //        return; // Skip if the parent button is not in the visible area
    //    }

    //    for (TalentButton child : button.getChildren()) {
    //        if (isInsideDraggableArea(child.x, child.y)) {
    //            drawConnectionLine(poseStack, button.x + BUTTON_SIZE / 2, button.y + BUTTON_SIZE, child.x + BUTTON_SIZE / 2, child.y);
    //            drawWhiteLinesForConnections(poseStack, child); // Recursive call
    //        }
    //    }
    //}

    //private void drawDropShadows(PoseStack poseStack, int parentX, int parentY, int childX, int childY) {
    //    int midY = parentY + (childY - parentY) / 2;
    //    this.hLine(poseStack, parentX, childX, midY - 1, 0xFF000000);
    //    this.hLine(poseStack, parentX, childX, midY + 1, 0xFF000000);
    //    this.hLine(poseStack, childX - 1, childX + 1, midY, 0xFF000000);
    //    this.hLine(poseStack, parentX - 1, parentX + 1, midY, 0xFF000000);
    //    this.vLine(poseStack, parentX - 1, parentY - 4, midY, 0xFF000000);
    //    this.vLine(poseStack, parentX + 1, parentY - 4, midY, 0xFF000000);
    //    this.vLine(poseStack, childX - 1, midY, childY + 4, 0xFF000000);
    //    this.vLine(poseStack, childX + 1, midY, childY + 4, 0xFF000000);
    //}

    //private void drawConnectionLine(PoseStack poseStack, int parentX, int parentY, int childX, int childY) {
    //    int midY = parentY + (childY - parentY) / 2;
    //    this.vLine(poseStack, parentX, parentY - 4, midY, 0xFFFFFFFF);
    //    this.vLine(poseStack, childX, midY, childY + 4, 0xFFFFFFFF);
    //    this.hLine(poseStack, parentX, childX, midY, 0xFFFFFFFF);
    //}
}

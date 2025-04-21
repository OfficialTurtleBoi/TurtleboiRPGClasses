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
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.ranger.*;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior.*;
import net.turtleboi.turtlerpgclasses.rpg.classes.Ranger;
import net.turtleboi.turtlerpgclasses.rpg.talents.commonTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.rangerTalents.active.*;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.GuardiansOathTalent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.WarlordsPresenceTalent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class RangerTalentTree extends TalentTree {
    private static final ResourceLocation RANGER_TREE_CONNECTIONS = new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/warrior_tree_connections.png");

    public RangerTalentTree(int posX, int posY, int width, int height, TalentScreen talentScreen) {
        super(posX, posY, width, height, true, talentScreen);
    }

    @Override
    protected void drawConnectionsTexture(GuiGraphics guiGraphics) {
        guiGraphics.blit(RANGER_TREE_CONNECTIONS, this.connectionTextureX, this.connectionTextureY, 0, 0, 190, 350, 190, 350);
    }

    @Override
    protected void setupTalentButtons() {
        int centerX = posX + (width / 2) - 13;
        int startY = posY + 13;
        String className = ClientClassData.getPlayerClass();
        rangerClassNode rangerClassNode = new rangerClassNode(
                this,
                new Ranger(),
                centerX,
                startY,
                1,
                0,
                ("Ranger".equals(className)),
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(rangerClassNode, TalentButton.TalentState.ACTIVE);

        // Tier 2
        WeakPointsTalentNode weakPointsTalentNode = new WeakPointsTalentNode(
                this,
                new WeakPointsTalent(),
                centerX - buttonSize,
                startY + verticalSpacing,
                4,
                0,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        SwiftHandsTalentNode swiftHandsTalentNode = new SwiftHandsTalentNode(
                this,
                new SwiftHandsTalent(),
                centerX + buttonSize,
                startY + verticalSpacing,
                4,
                0,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(weakPointsTalentNode, null, rangerClassNode);
        createTalentButton(swiftHandsTalentNode, null, rangerClassNode);

        // Tier 3
        LethalityTalentNode lethalityTalentNode = new LethalityTalentNode(
                this,
                new LethalityTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        QuickDrawTalentNode quickDrawTalentNode = new QuickDrawTalentNode(
                this,
                new QuickDrawTalent(),
                centerX,
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        VigorTalentNode vigorTalentNode = new VigorTalentNode(
                this,
                new VigorTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 2),
                3,
                1,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(lethalityTalentNode, null, weakPointsTalentNode, swiftHandsTalentNode);
        createTalentButton(quickDrawTalentNode, null, weakPointsTalentNode, swiftHandsTalentNode);
        createTalentButton(vigorTalentNode, null, weakPointsTalentNode, swiftHandsTalentNode);

        // Tier 4
        MarksmansAxiomSubclassNode marksmansAxiomSubclassNode = new MarksmansAxiomSubclassNode(
                this,
                new MarksmansAxiomSubclass(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        RoguesAxiomSubclassNode roguesAxiomSubclassNode = new RoguesAxiomSubclassNode(
                this,
                new RoguesAxiomSubclass(),
                centerX,
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        BeastmastersAxiomSubclassNode beastmastersAxiomSubclassNode = new BeastmastersAxiomSubclassNode(
                this,
                new BeastmastersAxiomSubclass(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 3),
                1,
                10,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(marksmansAxiomSubclassNode, null, lethalityTalentNode, quickDrawTalentNode, vigorTalentNode);
        createTalentButton(roguesAxiomSubclassNode, null, lethalityTalentNode, quickDrawTalentNode, vigorTalentNode);
        createTalentButton(beastmastersAxiomSubclassNode, null, lethalityTalentNode, quickDrawTalentNode, vigorTalentNode);

        marksmansAxiomSubclassNode.setButtonsToLock(Arrays.asList(roguesAxiomSubclassNode, beastmastersAxiomSubclassNode));
        roguesAxiomSubclassNode.setButtonsToLock(Arrays.asList(marksmansAxiomSubclassNode, beastmastersAxiomSubclassNode));
        beastmastersAxiomSubclassNode.setButtonsToLock(Arrays.asList(marksmansAxiomSubclassNode, roguesAxiomSubclassNode));

        // Tier 5
        QuickRecoveryTalentNode quickRecoveryTalentNode = new QuickRecoveryTalentNode(
                this,
                new QuickRecoveryTalent(),
                centerX - (buttonSize * 3),
                startY + (verticalSpacing * 4),
                4,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        EvasiveManeuversTalentNode evasiveManeuversTalentNode = new EvasiveManeuversTalentNode(
                this,
                new EvasiveManeuversTalent(),
                centerX - buttonSize,
                startY + (verticalSpacing * 4),
                5,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        VineWhipTalentNode vineWhipTalentNode = new VineWhipTalentNode(
                this,
                new VineWhipTalent(),
                centerX + buttonSize,
                startY + (verticalSpacing * 4),
                5,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        MarathonerTalentNode marathonerTalentNode = new MarathonerTalentNode(
                this,
                new MarathonerTalent(),
                centerX + (buttonSize * 3),
                startY + (verticalSpacing * 4),
                4,
                12,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(quickRecoveryTalentNode, null, marksmansAxiomSubclassNode, roguesAxiomSubclassNode, beastmastersAxiomSubclassNode);
        createTalentButton(evasiveManeuversTalentNode, null, marksmansAxiomSubclassNode, roguesAxiomSubclassNode, beastmastersAxiomSubclassNode);
        createTalentButton(vineWhipTalentNode, null, marksmansAxiomSubclassNode, roguesAxiomSubclassNode, beastmastersAxiomSubclassNode);
        createTalentButton(marathonerTalentNode, null, marksmansAxiomSubclassNode, roguesAxiomSubclassNode, beastmastersAxiomSubclassNode);

        evasiveManeuversTalentNode.setButtonsToLock(List.of(vineWhipTalentNode));
        vineWhipTalentNode.setButtonsToLock(List.of(evasiveManeuversTalentNode));

        // Tier 6
        SteadyBreathingTalentNode steadyBreathingTalentNode = new SteadyBreathingTalentNode(
                this,
                new SteadyBreathingTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        EffecientEnergyTalentNode effecientEnergyTalentNode = new EffecientEnergyTalentNode(
                this,
                new EfficientEnergyTalent(),
                centerX,
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        FocusedStrikesTalentNode focusedStrikesTalentNode = new FocusedStrikesTalentNode(
                this,
                new FocusedStrikesTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 5),
                5,
                16,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(steadyBreathingTalentNode, null, quickRecoveryTalentNode, evasiveManeuversTalentNode, vineWhipTalentNode, marathonerTalentNode);
        createTalentButton(effecientEnergyTalentNode, null, quickRecoveryTalentNode, evasiveManeuversTalentNode, vineWhipTalentNode, marathonerTalentNode);
        createTalentButton(focusedStrikesTalentNode, null, quickRecoveryTalentNode, evasiveManeuversTalentNode, vineWhipTalentNode, marathonerTalentNode);

        steadyBreathingTalentNode.setButtonsToLock(List.of(focusedStrikesTalentNode));
        focusedStrikesTalentNode.setButtonsToLock(List.of(steadyBreathingTalentNode));

        // Tier 7
        HuntersMarkTalentNode huntersMarkTalentNode = new HuntersMarkTalentNode(
                this,
                new HuntersMarkTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 6),
                1,
                21,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        EtherealArrowTalentNode etherealArrowTalentNode = new EtherealArrowTalentNode(
                this,
                new EtherealArrowTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 6),
                1,
                21,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(huntersMarkTalentNode, null, steadyBreathingTalentNode, effecientEnergyTalentNode, focusedStrikesTalentNode);
        createTalentButton(etherealArrowTalentNode, null, steadyBreathingTalentNode, effecientEnergyTalentNode, focusedStrikesTalentNode);

        huntersMarkTalentNode.setButtonsToLock(List.of(etherealArrowTalentNode));
        etherealArrowTalentNode.setButtonsToLock(List.of(huntersMarkTalentNode));

        // Tier 8
        FavoredEnemyTalentNode favoredEnemyTalentNode = new FavoredEnemyTalentNode(
                this,
                new FavoredEnemyTalent(),
                centerX - (buttonSize * 3),
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        KillStreakTalentNode killStreakTalentNode = new KillStreakTalentNode(
                this,
                new KillStreakTalent(),
                centerX - buttonSize,
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        EtherealVolleyTalentNode etherealVolleyTalentNode = new EtherealVolleyTalentNode(
                this,
                new EtherealVolleyTalent(),
                centerX + buttonSize,
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        HeatSeekingTalentNode heatSeekingTalentNode = new HeatSeekingTalentNode(
                this,
                new HeatSeekingTalent(),
                centerX + (buttonSize * 3),
                startY + (verticalSpacing * 7),
                3,
                25,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());
        createTalentButton(favoredEnemyTalentNode, null, huntersMarkTalentNode);
        createTalentButton(killStreakTalentNode, null, huntersMarkTalentNode);
        createTalentButton(etherealVolleyTalentNode, null, etherealArrowTalentNode);
        createTalentButton(heatSeekingTalentNode, null, etherealArrowTalentNode);

        favoredEnemyTalentNode.setButtonsToLock(List.of(killStreakTalentNode));
        killStreakTalentNode.setButtonsToLock(List.of(favoredEnemyTalentNode));
        etherealVolleyTalentNode.setButtonsToLock(List.of(heatSeekingTalentNode));
        heatSeekingTalentNode.setButtonsToLock(List.of(etherealVolleyTalentNode));

        // Tier 9
        GuerrillaWarfareTalentNode guerrillaWarfareTalentNode = new GuerrillaWarfareTalentNode(
                this,
                new GuerillaWarfareTalent(),
                centerX - (buttonSize * 2),
                startY + (verticalSpacing * 8),
                1,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        RenownedHunterTalentNode renownedHunterTalentNode = new RenownedHunterTalentNode(
                this,
                new RenownedHunterTalent(),
                centerX,
                startY + (verticalSpacing * 8),
                5,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        WindrunnerTalentNode windrunnerTalentNode = new WindrunnerTalentNode(
                this,
                new WindrunnerTalent(),
                centerX + (buttonSize * 2),
                startY + (verticalSpacing * 8),
                1,
                28,
                false,
                button -> {},
                (Supplier<MutableComponent> def) -> Component.empty());

        createTalentButton(guerrillaWarfareTalentNode, null, favoredEnemyTalentNode, killStreakTalentNode, etherealVolleyTalentNode, heatSeekingTalentNode);
        createTalentButton(renownedHunterTalentNode, null, favoredEnemyTalentNode, killStreakTalentNode, etherealVolleyTalentNode, heatSeekingTalentNode);
        createTalentButton(windrunnerTalentNode, null, favoredEnemyTalentNode, killStreakTalentNode, etherealVolleyTalentNode, heatSeekingTalentNode);

        guerrillaWarfareTalentNode.setButtonsToLock(List.of(windrunnerTalentNode));
        windrunnerTalentNode.setButtonsToLock(List.of(guerrillaWarfareTalentNode));

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

        createTalentButton(warriorWarlordsPresenceTalent, null, guerrillaWarfareTalentNode, renownedHunterTalentNode, windrunnerTalentNode);
        createTalentButton(warriorGuardiansOathTalent, null, guerrillaWarfareTalentNode, renownedHunterTalentNode, windrunnerTalentNode);

        warriorWarlordsPresenceTalent.setButtonsToLock(List.of(warriorGuardiansOathTalent));
        warriorGuardiansOathTalent.setButtonsToLock(List.of(warriorWarlordsPresenceTalent));
    }
}


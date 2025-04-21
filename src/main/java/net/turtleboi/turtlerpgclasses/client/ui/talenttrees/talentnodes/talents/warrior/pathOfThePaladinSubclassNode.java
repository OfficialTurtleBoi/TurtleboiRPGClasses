package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.*;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.ActiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.DivineSanctuaryTalent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class pathOfThePaladinSubclassNode extends ActiveTalentButton {
    public pathOfThePaladinSubclassNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/talent_icons/pathofthepaladinsubclass_icon.png");
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        Player player = Minecraft.getInstance().player;
        double currentPoints = getCurrentPoints();
        DivineSanctuaryTalent talent = new DivineSanctuaryTalent();

        double healValue = talent.getHeal();
        double durationValue = talent.getDurationSeconds();

        assert player != null;
        double cooldownReduction = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get());
        double baseCooldown = talent.getCooldownSeconds(); // Base cooldown value in seconds
        double adjustedCooldown = baseCooldown * (cooldownReduction / 100.0);

        double baseCost = 0.0;
        Map<String, Integer> costs = talent.getResourceCosts(player);
        if (costs.containsKey("mana")) {
            baseCost = costs.get("mana").doubleValue();
        }

        MutableComponent costComponent = Component.literal((baseCost) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY.withColor(TextColor.parseColor("#55FFFF")));
        MutableComponent cooldownComponent = Component.literal(String.format("%.1f", adjustedCooldown) + "s")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));

        MutableComponent healComponent = Component.literal((healValue) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY);
        MutableComponent durationComponent = Component.literal((durationValue) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY);

        tooltip.add(Component.translatable("subclass.paladin.talent")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.subclass")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        tooltip.add(Component.translatable("subclass.paladin.description"));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("talents.subclass.paladin.divine_sanctuary")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.active")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        tooltip.add(Component.translatable("talents.cost_mana")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#55FFFF")))
                .append(costComponent)
                .append(Component.translatable("talents.cooldown")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                        .append(cooldownComponent)
                ));
        tooltip.add(Component.translatable("talents.subclass.paladin.divine_sanctuary1")
                .append(durationComponent)
                .append(Component.translatable("talents.subclass.paladin.divine_sanctuary2"))
                .append(healComponent)
                .append(Component.translatable("talents.subclass.paladin.divine_sanctuary3")
                ));
        if (currentPoints == 0) {
            tooltip.add(Component.translatable("talents.not_learned")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        } else if (currentPoints == maxPoints){
            tooltip.add(Component.translatable("talents.max_rank")
                    .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#555555"))));
        }
        return tooltip;
    }

    @Override
    public void onLeftClick(double mouseX, double mouseY) {
        super.onLeftClick(mouseX, mouseY);
        Minecraft minecraft = Minecraft.getInstance();
            if (!(minecraft.screen instanceof TalentScreen talentScreen)) {
                return;
            }
        talentScreen.initializeTalentTree(PaladinTalentTree.class, false);
    }

    @Override
    public void onRightClick(double mouseX, double mouseY) {
        super.onRightClick(mouseX, mouseY);
        Minecraft minecraft = Minecraft.getInstance();
            if (!(minecraft.screen instanceof TalentScreen talentScreen)) {
                return;
            }
        talentScreen.clearTalentTree(PaladinTalentTree.class);
    }
}

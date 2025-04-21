package net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.talents.warrior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.turtleboi.turtlecore.init.CoreAttributes;
import net.turtleboi.turtlerpgclasses.TurtleRPGClasses;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.BarbarianTalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentScreen;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentTree;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.talentnodes.ActiveTalentButton;
import net.turtleboi.turtlerpgclasses.rpg.talents.Talent;
import net.turtleboi.turtlerpgclasses.rpg.talents.warriorTalents.active.UnleashFuryTalent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class pathOfTheBarbarianSubclassNode extends ActiveTalentButton {

    public pathOfTheBarbarianSubclassNode(TalentTree talentTree, Talent talentClass, int x, int y, int maxPoints, int requiredPoints, boolean alwaysActive, OnPress onPress, CreateNarration createNarration) {
        super(talentTree, talentClass, x, y, maxPoints, requiredPoints, alwaysActive, onPress, createNarration);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(TurtleRPGClasses.MOD_ID, "textures/gui/talents/talent_icons/pathofthebarbariansubclass_icon.png");
    }

    @Override
    public List<Component> generateDynamicTooltip() {
        List<Component> tooltip = new ArrayList<>();
        Player player = Minecraft.getInstance().player;
        double currentPoints = getCurrentPoints();
        UnleashFuryTalent talent = new UnleashFuryTalent();

        double damageValue = talent.getDamage();
        double bleedLength = talent.getBleedDurationSeconds();
        boolean isShiftPressed = Screen.hasShiftDown();

        assert player != null;
        double cooldownReduction = player.getAttributeValue(CoreAttributes.COOLDOWN_REDUCTION.get());
        double baseCooldown = talent.getCooldownSeconds(); // Base cooldown value in seconds
        double adjustedCooldown = baseCooldown * (cooldownReduction / 100.0);

        double baseCost = 0.0;
        Map<String, Integer> costs = talent.getResourceCosts(player);
        if (costs.containsKey("stamina")) {
            baseCost = costs.get("stamina").doubleValue();
        }

        MutableComponent costComponent = Component.literal((baseCost) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY.withColor(TextColor.parseColor("#AA0000")));
        MutableComponent cooldownComponent = Component.literal(String.format("%.1f", adjustedCooldown) + "s")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")));

        MutableComponent damageComponent = Component.literal((damageValue) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY);
        MutableComponent bleedDurationComponent = Component.literal((bleedLength) + " ")
                .withStyle(currentPoints == 0 ? Style.EMPTY.withColor(TextColor.parseColor("#00FF00")) :
                        Style.EMPTY);


        tooltip.add(Component.translatable("subclass.barbarian.talent")//Change this value for each talent
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.subclass")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        tooltip.add(Component.translatable("subclass.barbarian.description"));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("talents.subclass.barbarian.unleash_fury")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FFD52B")))
                .append(Component.literal(" "))
                .append(Component.translatable("talents.talent_type.active")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#808080")))
                ));
        tooltip.add(Component.translatable("talents.cost_stamina")
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#AA0000")))
                .append(costComponent)
                .append(Component.translatable("talents.cooldown")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#00AAAA")))
                        .append(cooldownComponent)
                ));
        tooltip.add(Component.translatable("talents.subclass.barbarian.unleash_fury1")
                .append(damageComponent)
                .append(Component.translatable("talents.subclass.barbarian.unleash_fury2"))
                .append(bleedDurationComponent)
                .append(Component.translatable("talents.subclass.barbarian.unleash_fury3"))
                .append(Component.translatable("talents.spell_effect.bleed")
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF5555")))
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
        talentScreen.initializeTalentTree(BarbarianTalentTree.class, false);
    }

    @Override
    public void onRightClick(double mouseX, double mouseY) {
        super.onRightClick(mouseX, mouseY);
        Minecraft minecraft = Minecraft.getInstance();
            if (!(minecraft.screen instanceof TalentScreen talentScreen)) {
                return;
            }
        talentScreen.clearTalentTree(BarbarianTalentTree.class);
    }
}

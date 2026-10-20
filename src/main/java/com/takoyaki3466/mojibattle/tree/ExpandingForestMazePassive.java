package com.takoyaki3466.mojibattle.tree;


import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.PassiveRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;

import java.util.List;

public final class ExpandingForestMazePassive extends AbstractRuntime<ExpandingForestMazePassive.Config> implements PassiveRuntime {
    private int treeUnits;

    public ExpandingForestMazePassive(Config config) {
        super(config);
    }


    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        treeUnits = Math.min(getConfig().treesPerUniverse(), treeUnits + 1);
    }

    @Override
    public float ultimateDamageMultiplier(CombatantView owner) {
        return getConfig().multiplier(WoodRank.fromTreeUnits(treeUnits, getConfig()));
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        WoodRank rank = WoodRank.fromTreeUnits(treeUnits, getConfig());
        return List.of(AbilityStateLine.of("state.expansion_mod.tree.rank." + rank.name().toLowerCase(), 100), AbilityStateLine.of("state.expansion_mod.tree.units", 90, treeUnits, getConfig().treesPerUniverse()));
    }

    public record Config(
            @ConfigInt(defaultValue = 2, min = 1) int treesPerGrove,
            @ConfigInt(defaultValue = 3, min = 1) int grovesPerForest,
            @ConfigInt(defaultValue = 2, min = 1) int forestsPerUniverse,
            @ConfigFloat(defaultValue = .75f) float unrankedMultiplier,
            @ConfigFloat(defaultValue = 1f) float treeMultiplier,
            @ConfigFloat(defaultValue = 1.35f) float groveMultiplier,
            @ConfigFloat(defaultValue = 1.75f) float forestMultiplier,
            @ConfigFloat(defaultValue = 2.4f) float universeMultiplier) implements WoodRank.Progression {

        public Config {
            try {
                Math.multiplyExact(Math.multiplyExact(treesPerGrove, grovesPerForest), forestsPerUniverse);
            } catch (ArithmeticException exception) {
                throw new IllegalArgumentException("Config progression product exceeds int range: "
                        + "treesPerGrove=" + treesPerGrove + ", grovesPerForest=" + grovesPerForest
                        + ", forestsPerUniverse=" + forestsPerUniverse, exception);
            }
        }

        public float multiplier(WoodRank rank) {
            return switch (rank) {
                case NONE -> unrankedMultiplier;
                case TREE -> treeMultiplier;
                case GROVE -> groveMultiplier;
                case FOREST -> forestMultiplier;
                case UNIVERSE -> universeMultiplier;
            };
        }
    }
}

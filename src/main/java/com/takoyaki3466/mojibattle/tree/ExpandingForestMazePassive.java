package com.takoyaki3466.mojibattle.tree;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.PassiveRuntime;
import jp.takoa.mojibattle.battle.CombatantView;

import java.util.List;

public final class ExpandingForestMazePassive implements PassiveRuntime {
    private final Config config;
    private int treeUnits;

    public ExpandingForestMazePassive(Config config) {
        this.config = config;
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        treeUnits = Math.min(config.treesPerUniverse(), treeUnits + 1);
    }

    @Override
    public float ultimateDamageMultiplier(CombatantView owner) {
        return config.multiplier(WoodRank.fromTreeUnits(treeUnits, config));
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        WoodRank rank = WoodRank.fromTreeUnits(treeUnits, config);
        return List.of(
                AbilityStateLine.of("state.expansion_mod.tree.rank." + rank.name().toLowerCase(), 100),
                AbilityStateLine.of("state.expansion_mod.tree.units", 90,
                        treeUnits, config.treesPerUniverse()));
    }

    public record Config(int treesPerGrove, int grovesPerForest, int forestsPerUniverse,
                         float unrankedMultiplier, float treeMultiplier, float groveMultiplier,
                         float forestMultiplier, float universeMultiplier) implements WoodRank.Progression {
        private static final int DEFAULT_TREES_PER_GROVE = 2;
        private static final int DEFAULT_GROVES_PER_FOREST = 3;
        private static final int DEFAULT_FORESTS_PER_UNIVERSE = 2;

        public Config {
            Math.multiplyExact(Math.multiplyExact(treesPerGrove, grovesPerForest), forestsPerUniverse);
            if (unrankedMultiplier < 0f || treeMultiplier < 0f || groveMultiplier < 0f
                    || forestMultiplier < 0f || universeMultiplier < 0f) {
                throw new IllegalArgumentException("Wood-rank multipliers must be non-negative");
            }
        }

        public static Config from(ConfigView parameters) {
            return new Config(
                    ConfigValues.positiveInt(parameters, "treesPerGrove", DEFAULT_TREES_PER_GROVE),
                    ConfigValues.positiveInt(parameters, "grovesPerForest", DEFAULT_GROVES_PER_FOREST),
                    ConfigValues.positiveInt(parameters, "forestsPerUniverse", DEFAULT_FORESTS_PER_UNIVERSE),
                    ConfigValues.nonNegativeFloat(parameters, "unrankedMultiplier", .75f),
                    ConfigValues.nonNegativeFloat(parameters, "treeMultiplier", 1f),
                    ConfigValues.nonNegativeFloat(parameters, "groveMultiplier", 1.35f),
                    ConfigValues.nonNegativeFloat(parameters, "forestMultiplier", 1.75f),
                    ConfigValues.nonNegativeFloat(parameters, "universeMultiplier", 2.4f));
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

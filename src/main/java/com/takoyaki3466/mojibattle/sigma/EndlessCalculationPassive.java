package com.takoyaki3466.mojibattle.sigma;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.PassiveRuntime;
import jp.takoa.mojibattle.battle.CombatantView;

import java.util.List;

public final class EndlessCalculationPassive implements PassiveRuntime {
    private static final ThreadLocal<Boolean> ULTIMATE_DAMAGE_IN_PROGRESS =
            ThreadLocal.withInitial(() -> false);

    private final Config config;
    private int stacks;

    public EndlessCalculationPassive(Config config) {
        this.config = config;
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        if (stacks < Integer.MAX_VALUE) {
            stacks++;
        }
    }

    @Override
    public void onUltimateActivated(AbilityActivationContext context) {
        stacks = 0;
    }

    @Override
    public float ultimateDamageMultiplier(CombatantView owner) {
        return stacks;
    }

    @Override
    public float criticalRateModifier(CombatantView owner) {
        return ultimateDamageInProgress() && stacks >= config.guaranteedCriticalMinimumStacks()
                ? 1f : 0f;
    }

    @Override
    public boolean ignoresDefense(CombatantView owner) {
        return ultimateDamageInProgress() && stacks >= config.defenseIgnoreMinimumStacks();
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return List.of(AbilityStateLine.of(
                "state.expansion_mod.sigma.summation", 100, stacks));
    }

    static void beginUltimateDamage() {
        ULTIMATE_DAMAGE_IN_PROGRESS.set(true);
    }

    static void endUltimateDamage() {
        ULTIMATE_DAMAGE_IN_PROGRESS.remove();
    }

    private static boolean ultimateDamageInProgress() {
        return ULTIMATE_DAMAGE_IN_PROGRESS.get();
    }

    public record Config(int defenseIgnoreMinimumStacks, int guaranteedCriticalMinimumStacks) {
        public Config {
            if (guaranteedCriticalMinimumStacks < defenseIgnoreMinimumStacks) {
                throw new IllegalArgumentException(
                        "guaranteedCriticalMinimumStacks must be at least defenseIgnoreMinimumStacks");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveInt(values, "defenseIgnoreMinimumStacks", 3),
                    ConfigValues.positiveInt(values, "guaranteedCriticalMinimumStacks", 5));
        }
    }
}

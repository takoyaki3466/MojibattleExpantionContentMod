package com.takoyaki3466.mojibattle.sigma;


import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.PassiveRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;

import java.util.List;

public final class EndlessCalculationPassive extends AbstractRuntime<EndlessCalculationPassive.Config> implements PassiveRuntime {
    private static final ThreadLocal<Boolean> ULTIMATE_DAMAGE_IN_PROGRESS = ThreadLocal.withInitial(() -> false);

    private int stacks;

    public EndlessCalculationPassive(Config config) {
        super(config);
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
        return ultimateDamageInProgress() && stacks >= getConfig().guaranteedCriticalMinimumStacks() ? 1f : 0f;
    }

    @Override
    public boolean ignoresDefense(CombatantView owner) {
        return ultimateDamageInProgress() && stacks >= getConfig().defenseIgnoreMinimumStacks();
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return List.of(AbilityStateLine.of("state.expansion_mod.sigma.summation", 100, stacks));
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

    public record Config(
            @ConfigInt(defaultValue = 3, min = 1) int defenseIgnoreMinimumStacks,
            @ConfigInt(defaultValue = 5, min = 1) int guaranteedCriticalMinimumStacks) {

        public Config {
            if (guaranteedCriticalMinimumStacks < defenseIgnoreMinimumStacks) {
                throw new IllegalArgumentException("Config 'guaranteedCriticalMinimumStacks' must be at least "
                        + "'defenseIgnoreMinimumStacks': " + guaranteedCriticalMinimumStacks
                        + " < " + defenseIgnoreMinimumStacks);
            }
        }
    }
}

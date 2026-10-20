package com.takoyaki3466.mojibattle.gold;


import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.context.PassiveUpdateContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.PassiveRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;

import java.util.List;

public final class TimeIsMoneyPassive extends AbstractRuntime<TimeIsMoneyPassive.Config> implements PassiveRuntime {
    private int fortune;
    private float nextTimedGain;

    public TimeIsMoneyPassive(Config config) {
        super(config);
        nextTimedGain = config.timedGainIntervalSeconds();
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        addFortune(1);
    }

    @Override
    public void onUltimateActivated(AbilityActivationContext context) {
        fortune = Math.max(0, fortune - getConfig().fortuneConsumedByUltimate());
    }

    @Override
    public void update(PassiveUpdateContext context) {
        while (context.battleElapsedSeconds() >= nextTimedGain) {
            addFortune(1);
            nextTimedGain += getConfig().timedGainIntervalSeconds();
        }
    }

    @Override
    public float criticalRateModifier(CombatantView owner) {
        return fortune * getConfig().criticalRatePerFortune();
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return List.of(AbilityStateLine.of("state.expansion_mod.gold.fortune", 100, fortune, getConfig().maximumFortune()));
    }

    private void addFortune(int amount) {
        fortune = Math.min(getConfig().maximumFortune(), fortune + amount);
    }

    public record Config(
            @ConfigInt(defaultValue = 10, min = 1) int maximumFortune,
            @ConfigInt(defaultValue = 4, min = 1) int fortuneConsumedByUltimate,
            @ConfigFloat(defaultValue = .05f) float criticalRatePerFortune,
            @ConfigFloat(defaultValue = 30f, exclusiveMin = true) float timedGainIntervalSeconds) {
    }
}

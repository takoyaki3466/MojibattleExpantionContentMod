package com.takoyaki3466.mojibattle.gold;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.PassiveRuntime;
import jp.takoa.mojibattle.ability.PassiveUpdateContext;
import jp.takoa.mojibattle.battle.CombatantView;

import java.util.List;

public final class TimeIsMoneyPassive implements PassiveRuntime {
    private final Config config;
    private int fortune;
    private float nextTimedGain;

    public TimeIsMoneyPassive(Config config) {
        this.config = config;
        nextTimedGain = config.timedGainIntervalSeconds();
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        addFortune(1);
    }

    @Override
    public void onUltimateActivated(AbilityActivationContext context) {
        fortune = Math.max(0, fortune - config.fortuneConsumedByUltimate());
    }

    @Override
    public void update(PassiveUpdateContext context) {
        while (context.battleElapsedSeconds() >= nextTimedGain) {
            addFortune(1);
            nextTimedGain += config.timedGainIntervalSeconds();
        }
    }

    @Override
    public float criticalRateModifier(CombatantView owner) {
        return fortune * config.criticalRatePerFortune();
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return List.of(AbilityStateLine.of("state.expansion_mod.gold.fortune", 100,
                fortune, config.maximumFortune()));
    }

    private void addFortune(int amount) {
        fortune = Math.min(config.maximumFortune(), fortune + amount);
    }

    public record Config(int maximumFortune, int fortuneConsumedByUltimate,
                         float criticalRatePerFortune, float timedGainIntervalSeconds) {
        public Config {
            if (fortuneConsumedByUltimate < 0) {
                throw new IllegalArgumentException("fortuneConsumedByUltimate must be non-negative");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveInt(values, "maximumFortune", 10),
                    ConfigValues.positiveInt(values, "fortuneConsumedByUltimate", 4),
                    ConfigValues.nonNegativeFloat(values, "criticalRatePerFortune", .05f),
                    ConfigValues.positiveFloat(values, "timedGainIntervalSeconds", 30f));
        }
    }
}

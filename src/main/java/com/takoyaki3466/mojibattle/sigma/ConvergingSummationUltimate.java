package com.takoyaki3466.mojibattle.sigma;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.UltimateRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageResult;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ConvergingSummationUltimate implements UltimateRuntime {
    private final Config config;

    public ConvergingSummationUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }

        DamageResult result;
        EndlessCalculationPassive.beginUltimateDamage();
        try {
            result = context.actions().damage(context.user(), context.target(),
                    new DamageRequest(config.damagePerStack(), DamageType.ULTIMATE, false, false));
        } finally {
            EndlessCalculationPassive.endUltimateDamage();
        }
        if (result.dealtDamage() <= 0f) {
            return false;
        }
        context.actions().emitEffect(context.target(), config.effectId(),
                config.effectScale(), config.effectDurationSeconds());
        return true;
    }

    public record Config(float damagePerStack, ResourceId effectId,
                         float effectScale, float effectDurationSeconds) {
        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.nonNegativeFloat(values, "damagePerStack", 60f),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:critical"),
                    ConfigValues.positiveFloat(values, "effectScale", 10f),
                    ConfigValues.positiveFloat(values, "effectDurationSeconds", 1f));
        }
    }
}

package com.takoyaki3466.mojibattle.sigma;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.UltimateRuntime;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageResult;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ConvergingSummationUltimate extends AbstractRuntime<ConvergingSummationUltimate.Config> implements UltimateRuntime {

    public ConvergingSummationUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }

        DamageResult result;
        EndlessCalculationPassive.beginUltimateDamage();
        try {
            result = context.actions().damage(context.user(), context.target(), new DamageRequest(getConfig().damagePerStack(), DamageType.ULTIMATE, false, false));
        } finally {
            EndlessCalculationPassive.endUltimateDamage();
        }
        if (result.dealtDamage() <= 0f) {
            return false;
        }
        context.actions().emitEffect(context.target(), getConfig().effectId(), getConfig().effectScale(), getConfig().effectDurationSeconds());
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 60f) float damagePerStack,
            @ConfigResource(defaultValue = "mojibattle:critical") ResourceId effectId,
            @ConfigFloat(defaultValue = 10f, exclusiveMin = true) float effectScale,
            @ConfigFloat(defaultValue = 1f, exclusiveMin = true) float effectDurationSeconds) {
    }
}

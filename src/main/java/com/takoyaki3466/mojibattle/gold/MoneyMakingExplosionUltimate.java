package com.takoyaki3466.mojibattle.gold;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.UltimateRuntime;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class MoneyMakingExplosionUltimate extends AbstractRuntime<MoneyMakingExplosionUltimate.Config> implements UltimateRuntime {

    public MoneyMakingExplosionUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().damage(context.user(), context.target(), new DamageRequest(getConfig().baseDamage(), DamageType.ULTIMATE, false, true));
        context.actions().emitEffect(context.target(), getConfig().effectId(), getConfig().effectScale(), getConfig().effectDurationSeconds());
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 165f) float baseDamage,
            @ConfigResource(defaultValue = "mojibattle:critical") ResourceId effectId,
            @ConfigFloat(defaultValue = 9f, exclusiveMin = true) float effectScale,
            @ConfigFloat(defaultValue = .9f, exclusiveMin = true) float effectDurationSeconds) {
    }
}

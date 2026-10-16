package com.takoyaki3466.mojibattle.gold;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.UltimateRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class MoneyMakingExplosionUltimate implements UltimateRuntime {
    private final Config config;

    public MoneyMakingExplosionUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().damage(context.user(), context.target(),
                new DamageRequest(config.baseDamage(), DamageType.ULTIMATE, false, true));
        context.actions().emitEffect(context.target(), config.effectId(),
                config.effectScale(), config.effectDurationSeconds());
        return true;
    }

    public record Config(float baseDamage, ResourceId effectId, float effectScale,
                         float effectDurationSeconds) {
        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.nonNegativeFloat(values, "baseDamage", 165f),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:critical"),
                    ConfigValues.positiveFloat(values, "effectScale", 9f),
                    ConfigValues.positiveFloat(values, "effectDurationSeconds", .9f));
        }
    }
}

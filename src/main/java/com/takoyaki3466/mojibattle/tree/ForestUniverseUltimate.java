package com.takoyaki3466.mojibattle.tree;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.UltimateRuntime;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ForestUniverseUltimate implements UltimateRuntime {
    private final Config config;

    public ForestUniverseUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        var enemies = context.actions().enemiesOf(context.user());
        if (enemies.isEmpty()) {
            return false;
        }

        context.actions().emitEffect(
                context.user(), config.activationEffect(), config.activationEffectScale(), config.effectDurationSeconds());
        for (var enemy : enemies) {
            context.actions().damage(context.user(), enemy, config.baseDamage(), DamageType.ULTIMATE);
            context.actions().emitEffect(
                    enemy, config.impactEffect(), config.impactEffectScale(), config.effectDurationSeconds());
        }
        return true;
    }

    public record Config(float baseDamage, ResourceId activationEffect, ResourceId impactEffect,
                         float activationEffectScale, float impactEffectScale, float effectDurationSeconds) {
        private static final float DEFAULT_BASE_DAMAGE = 100f;
        private static final float DEFAULT_ACTIVATION_EFFECT_SCALE = 8f;
        private static final float DEFAULT_IMPACT_EFFECT_SCALE = 6f;
        private static final float DEFAULT_EFFECT_DURATION_SECONDS = .9f;
        private static final String DEFAULT_ACTIVATION_EFFECT = "mojibattle:family";
        private static final String DEFAULT_IMPACT_EFFECT = "mojibattle:impact";

        public static Config from(ConfigView parameters) {
            return new Config(
                    ConfigValues.nonNegativeFloat(parameters, "baseDamage", DEFAULT_BASE_DAMAGE),
                    ConfigValues.resourceId(parameters, "activationEffectId", DEFAULT_ACTIVATION_EFFECT),
                    ConfigValues.resourceId(parameters, "impactEffectId", DEFAULT_IMPACT_EFFECT),
                    ConfigValues.positiveFloat(parameters, "activationEffectScale", DEFAULT_ACTIVATION_EFFECT_SCALE),
                    ConfigValues.positiveFloat(parameters, "impactEffectScale", DEFAULT_IMPACT_EFFECT_SCALE),
                    ConfigValues.positiveFloat(parameters, "effectDurationSeconds", DEFAULT_EFFECT_DURATION_SECONDS));
        }
    }
}

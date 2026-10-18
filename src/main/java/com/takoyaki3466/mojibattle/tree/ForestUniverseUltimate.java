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
        }

        // BattlefieldView returns unified views for mains, supports and summons. MojiBattle 0.8.1
        // can damage all of those views, but its emitEffect adapter still accepts only the legacy
        // main-character view supplied as AbilityActivationContext.target(). Using a unified enemy
        // view here therefore crashes as soon as this ultimate is activated. Keep the all-enemy
        // damage and anchor the representative impact effect to the safe primary target instead.
        var primaryTarget = context.target();
        if (primaryTarget != null && primaryTarget.alive()) {
            context.actions().emitEffect(primaryTarget, config.impactEffect(),
                    config.impactEffectScale(), config.effectDurationSeconds());
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

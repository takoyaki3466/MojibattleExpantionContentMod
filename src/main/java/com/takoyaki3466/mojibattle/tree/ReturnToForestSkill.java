package com.takoyaki3466.mojibattle.tree;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SkillRuntime;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ReturnToForestSkill implements SkillRuntime {
    private final Config config;

    public ReturnToForestSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        var target = context.target();
        if (target == null || !target.alive()) {
            return false;
        }

        context.actions().damage(context.user(), target, config.damage(), DamageType.SKILL);
        context.actions().knockbackAwayFrom(context.user(), target, config.knockbackDistance());
        context.actions().emitEffect(target, config.impactEffect(), config.effectScale(), config.effectDurationSeconds());
        return true;
    }

    public record Config(float damage, float knockbackDistance, ResourceId impactEffect,
                         float effectScale, float effectDurationSeconds) {
        private static final float DEFAULT_DAMAGE = 30f;
        private static final float DEFAULT_KNOCKBACK_DISTANCE = 1.4f;
        private static final float DEFAULT_EFFECT_SCALE = 4.5f;
        private static final float DEFAULT_EFFECT_DURATION_SECONDS = .45f;
        private static final String DEFAULT_IMPACT_EFFECT = "mojibattle:wind";

        public static Config from(ConfigView parameters) {
            return new Config(
                    ConfigValues.nonNegativeFloat(parameters, "damage", DEFAULT_DAMAGE),
                    ConfigValues.nonNegativeFloat(parameters, "knockbackDistance", DEFAULT_KNOCKBACK_DISTANCE),
                    ConfigValues.resourceId(parameters, "impactEffectId", DEFAULT_IMPACT_EFFECT),
                    ConfigValues.positiveFloat(parameters, "effectScale", DEFAULT_EFFECT_SCALE),
                    ConfigValues.positiveFloat(parameters, "effectDurationSeconds", DEFAULT_EFFECT_DURATION_SECONDS));
        }
    }
}

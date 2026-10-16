package com.takoyaki3466.mojibattle.tea;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SkillRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.battle.ProjectileSpec;
import jp.takoa.mojibattle.battle.StatusDuration;
import jp.takoa.mojibattle.battle.StatusEffectType;
import jp.takoa.mojibattle.battle.StatusSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class MakeTeaSkill implements SkillRuntime {
    private static final ResourceId SLOW_STATUS = new ResourceId("expansion_mod", "tea_slow");
    private final Config config;

    public MakeTeaSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().applyStatus(context.user(), context.target(), StatusSpec.multiplier(
                SLOW_STATUS, StatusEffectType.MOVE_SPEED_MULTIPLIER, config.slowMultiplier(),
                StatusDuration.seconds(config.slowDurationSeconds())));
        context.actions().spawnProjectile(context.user(), context.target(), new ProjectileSpec(
                DamageRequest.of(config.damage(), DamageType.SKILL), config.projectileSpeed(),
                config.projectileRadius(), config.projectileLifetimeSeconds(), 1, 0f, 1,
                config.projectileTexture(), config.effectId()));
        return true;
    }

    public record Config(float damage, float slowMultiplier, float slowDurationSeconds,
                         float projectileSpeed, float projectileRadius, float projectileLifetimeSeconds,
                         ResourceId projectileTexture, ResourceId effectId) {
        public Config {
            if (slowMultiplier > 1f) {
                throw new IllegalArgumentException("slowMultiplier must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.nonNegativeFloat(values, "damage", 34f),
                    ConfigValues.positiveFloat(values, "slowMultiplier", .62f),
                    ConfigValues.positiveFloat(values, "slowDurationSeconds", 4.5f),
                    ConfigValues.positiveFloat(values, "projectileSpeed", 12f),
                    ConfigValues.positiveFloat(values, "projectileRadius", .55f),
                    ConfigValues.positiveFloat(values, "projectileLifetimeSeconds", 2.5f),
                    ConfigValues.resourceId(values, "projectileTexture", "expansion_mod:textures/tea.png"),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:kana"));
        }
    }
}

package com.takoyaki3466.mojibattle.eyebrow;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportUltimateRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.battle.ProjectileSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class SkepticalVolleyUltimate implements SupportUltimateRuntime {
    private final Config config;

    public SkepticalVolleyUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        int level = EyebrowState.level(context.state(), config.maximumLevel());
        int projectiles = Math.clamp((level + config.levelsPerProjectile() - 1)
                / config.levelsPerProjectile(), 1, config.maximumProjectiles());
        context.actions().spawnProjectile(context.owner(), context.target(), new ProjectileSpec(
                DamageRequest.of(config.damagePerProjectile(), DamageType.ULTIMATE),
                config.projectileSpeed(), config.projectileRadius(), config.projectileLifetimeSeconds(),
                projectiles, config.spreadDegrees(), 1, config.projectileTexture(), config.effectId()));
        EyebrowState.halve(context.state(), config.maximumLevel());
        return true;
    }

    public record Config(int maximumLevel, int levelsPerProjectile, int maximumProjectiles,
                         float damagePerProjectile, float projectileSpeed, float projectileRadius,
                         float projectileLifetimeSeconds, float spreadDegrees,
                         ResourceId projectileTexture, ResourceId effectId) {
        public Config {
            if (maximumProjectiles > 64) {
                throw new IllegalArgumentException("maximumProjectiles must not exceed 64");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveInt(values, "maximumLevel", 200),
                    ConfigValues.positiveInt(values, "levelsPerProjectile", 10),
                    ConfigValues.positiveInt(values, "maximumProjectiles", 20),
                    ConfigValues.nonNegativeFloat(values, "damagePerProjectile", 22f),
                    ConfigValues.positiveFloat(values, "projectileSpeed", 16f),
                    ConfigValues.positiveFloat(values, "projectileRadius", .52f),
                    ConfigValues.positiveFloat(values, "projectileLifetimeSeconds", 3f),
                    ConfigValues.nonNegativeFloat(values, "spreadDegrees", 38f),
                    ConfigValues.resourceId(values, "projectileTexture", "expansion_mod:textures/skeptical.png"),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:critical"));
        }
    }
}

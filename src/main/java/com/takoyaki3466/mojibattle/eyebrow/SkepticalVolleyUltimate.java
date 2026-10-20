package com.takoyaki3466.mojibattle.eyebrow;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.action.spec.ProjectileSpec;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class SkepticalVolleyUltimate extends AbstractRuntime<SkepticalVolleyUltimate.Config> implements SupportAbilityRuntime {

    public SkepticalVolleyUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        int level = EyebrowState.level(context.state(), getConfig().maximumLevel());
        int projectiles = Math.clamp((level + getConfig().levelsPerProjectile() - 1) / getConfig().levelsPerProjectile(), 1, getConfig().maximumProjectiles());

        context.actions().spawnProjectile(context.owner(), context.target(), new ProjectileSpec(DamageRequest.of(getConfig().damagePerProjectile(), DamageType.ULTIMATE), getConfig().projectileSpeed(), getConfig().projectileRadius(), getConfig().projectileLifetimeSeconds(), projectiles, getConfig().spreadDegrees(), 1, getConfig().projectileTexture(), getConfig().effectId()));

        EyebrowState.halve(context.state(), getConfig().maximumLevel());
        return true;
    }

    public record Config(
            @ConfigInt(defaultValue = 200, min = 1) int maximumLevel,
            @ConfigInt(defaultValue = 10, min = 1) int levelsPerProjectile,
            @ConfigInt(defaultValue = 20, min = 1, max = 64) int maximumProjectiles,
            @ConfigFloat(defaultValue = 22f) float damagePerProjectile,
            @ConfigFloat(defaultValue = 16f, exclusiveMin = true) float projectileSpeed,
            @ConfigFloat(defaultValue = .52f, exclusiveMin = true) float projectileRadius,
            @ConfigFloat(defaultValue = 3f, exclusiveMin = true) float projectileLifetimeSeconds,
            @ConfigFloat(defaultValue = 38f) float spreadDegrees,
            @ConfigResource(defaultValue = "expansion_mod:textures/skeptical.png") ResourceId projectileTexture,
            @ConfigResource(defaultValue = "mojibattle:critical") ResourceId effectId) {
    }
}

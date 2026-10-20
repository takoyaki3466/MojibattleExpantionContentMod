package com.takoyaki3466.mojibattle.tea;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SkillRuntime;
import jp.takoa.mojibattle.battle.action.spec.ProjectileSpec;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.battle.status.StatusDuration;
import jp.takoa.mojibattle.battle.status.StatusEffectType;
import jp.takoa.mojibattle.battle.status.StatusSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class MakeTeaSkill extends AbstractRuntime<MakeTeaSkill.Config> implements SkillRuntime {
    private static final ResourceId SLOW_STATUS = ExpansionMod.ofId("tea_slow");

    public MakeTeaSkill(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().applyStatus(context.user(), context.target(), StatusSpec.multiplier(SLOW_STATUS, StatusEffectType.MOVE_SPEED_MULTIPLIER, getConfig().slowMultiplier(), StatusDuration.seconds(getConfig().slowDurationSeconds())));
        context.actions().spawnProjectile(context.user(), context.target(), new ProjectileSpec(DamageRequest.of(getConfig().damage(), DamageType.SKILL), getConfig().projectileSpeed(), getConfig().projectileRadius(), getConfig().projectileLifetimeSeconds(), 1, 0f, 1, getConfig().projectileTexture(), getConfig().effectId()));
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 34f) float damage,
            @ConfigFloat(defaultValue = .62f, exclusiveMin = true, max = 1f) float slowMultiplier,
            @ConfigFloat(defaultValue = 4.5f, exclusiveMin = true) float slowDurationSeconds,
            @ConfigFloat(defaultValue = 12f, exclusiveMin = true) float projectileSpeed,
            @ConfigFloat(defaultValue = .55f, exclusiveMin = true) float projectileRadius,
            @ConfigFloat(defaultValue = 2.5f, exclusiveMin = true) float projectileLifetimeSeconds,
            @ConfigResource(defaultValue = "expansion_mod:textures/tea.png") ResourceId projectileTexture,
            @ConfigResource(defaultValue = "mojibattle:kana") ResourceId effectId) {
    }
}

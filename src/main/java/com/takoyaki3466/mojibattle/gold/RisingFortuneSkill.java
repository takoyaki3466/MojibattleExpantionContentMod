package com.takoyaki3466.mojibattle.gold;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SkillRuntime;
import jp.takoa.mojibattle.battle.action.spec.DelayedStrikeSpec;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

public final class RisingFortuneSkill extends AbstractRuntime<RisingFortuneSkill.Config> implements SkillRuntime {

    public RisingFortuneSkill(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().spawnDelayedStrike(context.user(), context.target(), new DelayedStrikeSpec(getConfig().dropDelaySeconds(), DamageRequest.of(getConfig().damage(), DamageType.SKILL), getConfig().effectId(), getConfig().goldTexture(), getConfig().effectScale()));
        context.actions().addUltimatePoints(context.user(), getConfig().ultimatePoints());
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 38f) float damage,
            @ConfigFloat(defaultValue = 18f, exclusiveMin = true) float ultimatePoints,
            @ConfigFloat(defaultValue = .55f) float dropDelaySeconds,
            @ConfigResource(defaultValue = "mojibattle:stock") ResourceId effectId,
            @ConfigResource(defaultValue = "expansion_mod:textures/gold.png") ResourceId goldTexture,
            @ConfigFloat(defaultValue = 5.5f, exclusiveMin = true) float effectScale) {
    }
}

package com.takoyaki3466.mojibattle.tree;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.UltimateRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.List;

public final class ForestUniverseUltimate extends AbstractRuntime<ForestUniverseUltimate.Config> implements UltimateRuntime {
    public ForestUniverseUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        List<CombatantView> enemies = context.actions().enemiesOf(context.user());
        if (enemies.isEmpty()) {
            return false;
        }

        context.actions().emitEffect(context.user(), getConfig().activationEffect(), getConfig().activationEffectScale(), getConfig().effectDurationSeconds());
        for (CombatantView enemy : enemies) {
            context.actions().damage(context.user(), enemy, getConfig().baseDamage(), DamageType.ULTIMATE);
        }

        // BattlefieldView returns unified views for mains, supports and summons. MojiBattle 0.8.1
        // can damage all of those views, but its emitEffect adapter still accepts only the legacy
        // main-character view supplied as AbilityActivationContext.target(). Using a unified enemy
        // view here therefore crashes as soon as this ultimate is activated. Keep the all-enemy
        // damage and anchor the representative impact effect to the safe primary target instead.
        CombatantView primaryTarget = context.target();

        if (primaryTarget != null && primaryTarget.alive()) {
            context.actions().emitEffect(primaryTarget, getConfig().impactEffect(), getConfig().impactEffectScale(), getConfig().effectDurationSeconds());
        }
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 100f) float baseDamage,
            @ConfigResource(name = "activationEffectId", defaultValue = "mojibattle:family") ResourceId activationEffect,
            @ConfigResource(name = "impactEffectId", defaultValue = "mojibattle:impact") ResourceId impactEffect,
            @ConfigFloat(defaultValue = 32f, exclusiveMin = true) float activationEffectScale,
            @ConfigFloat(defaultValue = 24f, exclusiveMin = true) float impactEffectScale,
            @ConfigFloat(defaultValue = .9f, exclusiveMin = true) float effectDurationSeconds) {
    }
}

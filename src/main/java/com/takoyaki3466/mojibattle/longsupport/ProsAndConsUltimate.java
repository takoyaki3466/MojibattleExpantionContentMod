package com.takoyaki3466.mojibattle.longsupport;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.status.StatusDuration;
import jp.takoa.mojibattle.battle.status.StatusEffectType;
import jp.takoa.mojibattle.battle.status.StatusSpec;
import jp.takoa.mojibattle.battle.status.StatusStacking;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ProsAndConsUltimate extends AbstractRuntime<ProsAndConsUltimate.Config> implements SupportAbilityRuntime {
    private static final ResourceId ALLY_STATUS = ExpansionMod.ofId("pros_and_cons_ally");
    private static final ResourceId ENEMY_STATUS = ExpansionMod.ofId("pros_and_cons_enemy");

    public ProsAndConsUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        for (CombatantView ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally, permanentStack(ALLY_STATUS, getConfig().allyAttackIntervalMultiplier()));
        }
        for (CombatantView enemy : context.actions().enemiesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), enemy, permanentStack(ENEMY_STATUS, getConfig().enemyAttackIntervalMultiplier()));
        }
        return true;
    }

    private StatusSpec permanentStack(ResourceId id, float magnitude) {
        return new StatusSpec(id, StatusEffectType.NORMAL_ATTACK_INTERVAL_MULTIPLIER, magnitude, StatusDuration.permanent(), StatusStacking.ADD_STACK, getConfig().maximumStacks(), 1f);
    }

    public record Config(
            @ConfigFloat(defaultValue = .92f, exclusiveMin = true, max = 1f) float allyAttackIntervalMultiplier,
            @ConfigFloat(defaultValue = 1.08f, min = 1f) float enemyAttackIntervalMultiplier,
            @ConfigInt(defaultValue = 100, min = 1) int maximumStacks) {

    }
}

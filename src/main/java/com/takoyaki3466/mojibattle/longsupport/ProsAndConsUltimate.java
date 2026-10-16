package com.takoyaki3466.mojibattle.longsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportUltimateRuntime;
import jp.takoa.mojibattle.battle.StatusDuration;
import jp.takoa.mojibattle.battle.StatusEffectType;
import jp.takoa.mojibattle.battle.StatusSpec;
import jp.takoa.mojibattle.battle.StatusStacking;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ProsAndConsUltimate implements SupportUltimateRuntime {
    private static final ResourceId ALLY_STATUS = new ResourceId("expansion_mod", "pros_and_cons_ally");
    private static final ResourceId ENEMY_STATUS = new ResourceId("expansion_mod", "pros_and_cons_enemy");
    private final Config config;

    public ProsAndConsUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        for (var ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally,
                    permanentStack(ALLY_STATUS, config.allyAttackIntervalMultiplier()));
        }
        for (var enemy : context.actions().enemiesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), enemy,
                    permanentStack(ENEMY_STATUS, config.enemyAttackIntervalMultiplier()));
        }
        return true;
    }

    private StatusSpec permanentStack(ResourceId id, float magnitude) {
        return new StatusSpec(id, StatusEffectType.NORMAL_ATTACK_INTERVAL_MULTIPLIER, magnitude,
                StatusDuration.permanent(), StatusStacking.ADD_STACK, config.maximumStacks(), 1f);
    }

    public record Config(float allyAttackIntervalMultiplier, float enemyAttackIntervalMultiplier,
                         int maximumStacks) {
        public Config {
            if (allyAttackIntervalMultiplier > 1f || enemyAttackIntervalMultiplier < 1f) {
                throw new IllegalArgumentException("Pros and Cons interval multipliers are reversed");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "allyAttackIntervalMultiplier", .92f),
                    ConfigValues.positiveFloat(values, "enemyAttackIntervalMultiplier", 1.08f),
                    ConfigValues.positiveInt(values, "maximumStacks", 100));
        }
    }
}

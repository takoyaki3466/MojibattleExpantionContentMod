package com.takoyaki3466.mojibattle.dxsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportSkillRuntime;
import jp.takoa.mojibattle.battle.StatusConsumption;
import jp.takoa.mojibattle.battle.StatusDuration;
import jp.takoa.mojibattle.battle.StatusEffectType;
import jp.takoa.mojibattle.battle.StatusSpec;
import jp.takoa.mojibattle.battle.StatusStacking;
import jp.takoa.mojibattle.resource.ResourceId;

public final class PlaneDifferentiationSkill implements SupportSkillRuntime {
    private static final ResourceId SHIELD_ID = new ResourceId("expansion_mod", "plane_differentiation_shield");
    private final Config config;

    public PlaneDifferentiationSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        StatusSpec shield = new StatusSpec(SHIELD_ID,
                StatusEffectType.NEXT_INCOMING_DAMAGE_MULTIPLIER,
                config.damageMultiplier(), StatusDuration.permanent(),
                StatusStacking.REPLACE, 1, 1f, StatusConsumption.ON_DAMAGE_RECEIVED);
        for (var ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally, shield);
        }
        return true;
    }

    public record Config(float damageMultiplier) {
        public Config {
            if (damageMultiplier > 1f) {
                throw new IllegalArgumentException("damageMultiplier must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(ConfigValues.positiveFloat(values, "damageMultiplier", .5f));
        }
    }
}

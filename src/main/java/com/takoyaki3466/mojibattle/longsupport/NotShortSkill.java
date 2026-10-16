package com.takoyaki3466.mojibattle.longsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportSkillRuntime;
import jp.takoa.mojibattle.battle.StatusDuration;
import jp.takoa.mojibattle.battle.StatusEffectType;
import jp.takoa.mojibattle.battle.StatusSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class NotShortSkill implements SupportSkillRuntime {
    private static final ResourceId DEFENSE_STATUS = new ResourceId("expansion_mod", "not_short_defense");
    private final Config config;

    public NotShortSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        for (var ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally, StatusSpec.multiplier(
                    DEFENSE_STATUS, StatusEffectType.INCOMING_DAMAGE_MULTIPLIER,
                    config.incomingDamageMultiplier(), StatusDuration.seconds(config.durationSeconds())));
            context.actions().healRatio(ally, config.healRatio());
        }
        return true;
    }

    public record Config(float incomingDamageMultiplier, float durationSeconds, float healRatio) {
        public Config {
            if (incomingDamageMultiplier > 1f || healRatio > 1f) {
                throw new IllegalArgumentException("Not Short ratios must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "incomingDamageMultiplier", .7f),
                    ConfigValues.positiveFloat(values, "durationSeconds", 8f),
                    ConfigValues.nonNegativeFloat(values, "healRatio", .05f));
        }
    }
}

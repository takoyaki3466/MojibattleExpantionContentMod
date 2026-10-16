package com.takoyaki3466.mojibattle.gold;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SkillRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.battle.DelayedStrikeSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class RisingFortuneSkill implements SkillRuntime {
    private final Config config;

    public RisingFortuneSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        context.actions().spawnDelayedStrike(context.user(), context.target(), new DelayedStrikeSpec(
                config.dropDelaySeconds(), DamageRequest.of(config.damage(), DamageType.SKILL),
                config.effectId(), config.goldTexture(), config.effectScale()));
        return true;
    }

    public record Config(float damage, float dropDelaySeconds, ResourceId effectId,
                         ResourceId goldTexture, float effectScale) {
        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.nonNegativeFloat(values, "damage", 38f),
                    ConfigValues.nonNegativeFloat(values, "dropDelaySeconds", .55f),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:stock"),
                    ConfigValues.resourceId(values, "goldTexture", "expansion_mod:textures/gold.png"),
                    ConfigValues.positiveFloat(values, "effectScale", 5.5f));
        }
    }
}

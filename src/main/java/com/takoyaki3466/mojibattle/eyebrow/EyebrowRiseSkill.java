package com.takoyaki3466.mojibattle.eyebrow;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportSkillRuntime;
import jp.takoa.mojibattle.battle.DamageRequest;
import jp.takoa.mojibattle.battle.DamageType;
import jp.takoa.mojibattle.battle.ProjectileSpec;
import jp.takoa.mojibattle.battle.SupportNormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

public final class EyebrowRiseSkill implements SupportSkillRuntime {
    private static final ResourceId HASTE_ID = new ResourceId("expansion_mod", "eyebrow_rise_haste");
    private final Config config;

    public EyebrowRiseSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        EyebrowState.add(context.state(), config.levelGain(), config.maximumLevel());
        context.supportActions().addNormalAttackModifier(new SupportNormalAttackModifier(
                HASTE_ID, 200, SupportNormalAttackModifier.Stacking.MULTIPLY,
                1f, config.intervalMultiplier(), 1, 1, 1f, null, config.effectId()),
                config.durationSeconds());
        context.actions().spawnProjectile(context.owner(), context.target(), new ProjectileSpec(
                DamageRequest.of(config.damage(), DamageType.SKILL), config.projectileSpeed(),
                config.projectileRadius(), config.projectileLifetimeSeconds(), 1, 0f, 1,
                config.projectileTexture(), config.effectId()));
        return true;
    }

    public record Config(int levelGain, int maximumLevel, float intervalMultiplier,
                         float durationSeconds, float damage, float projectileSpeed,
                         float projectileRadius, float projectileLifetimeSeconds,
                         ResourceId projectileTexture, ResourceId effectId) {
        public Config {
            if (intervalMultiplier > 1f || levelGain > maximumLevel) {
                throw new IllegalArgumentException("Invalid eyebrow skill values");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveInt(values, "levelGain", 10),
                    ConfigValues.positiveInt(values, "maximumLevel", 200),
                    ConfigValues.positiveFloat(values, "intervalMultiplier", .65f),
                    ConfigValues.positiveFloat(values, "durationSeconds", 6f),
                    ConfigValues.nonNegativeFloat(values, "damage", 42f),
                    ConfigValues.positiveFloat(values, "projectileSpeed", 15f),
                    ConfigValues.positiveFloat(values, "projectileRadius", .5f),
                    ConfigValues.positiveFloat(values, "projectileLifetimeSeconds", 2.5f),
                    ConfigValues.resourceId(values, "projectileTexture", "expansion_mod:textures/eyebrow.png"),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:critical"));
        }
    }
}

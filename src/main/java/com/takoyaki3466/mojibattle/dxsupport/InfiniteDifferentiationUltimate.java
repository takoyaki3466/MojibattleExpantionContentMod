package com.takoyaki3466.mojibattle.dxsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportUltimateRuntime;
import jp.takoa.mojibattle.battle.SupportNormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

public final class InfiniteDifferentiationUltimate implements SupportUltimateRuntime {
    private static final ResourceId MODIFIER_ID = new ResourceId("expansion_mod", "infinite_differentiation");
    private final Config config;

    public InfiniteDifferentiationUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        context.supportActions().addNormalAttackModifier(new SupportNormalAttackModifier(
                MODIFIER_ID, 100, SupportNormalAttackModifier.Stacking.MULTIPLY,
                config.damageMultiplier(), config.intervalMultiplier(), 1,
                config.hitsPerAttack(), 1f, config.projectileTexture(), config.effectId()));
        return true;
    }

    public record Config(float damageMultiplier, float intervalMultiplier, int hitsPerAttack,
                         ResourceId projectileTexture, ResourceId effectId) {
        public Config {
            if (intervalMultiplier > 1f) {
                throw new IllegalArgumentException("intervalMultiplier must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "damageMultiplier", .55f),
                    ConfigValues.positiveFloat(values, "intervalMultiplier", .82f),
                    ConfigValues.positiveInt(values, "hitsPerAttack", 3),
                    ConfigValues.resourceId(values, "projectileTexture", "expansion_mod:textures/dx.png"),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:impact"));
        }
    }
}

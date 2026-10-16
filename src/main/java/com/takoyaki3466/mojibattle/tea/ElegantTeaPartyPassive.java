package com.takoyaki3466.mojibattle.tea;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.PassiveRuntime;
import jp.takoa.mojibattle.ability.PassiveUpdateContext;
import jp.takoa.mojibattle.battle.CombatantView;
import jp.takoa.mojibattle.battle.NormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.List;

public final class ElegantTeaPartyPassive implements PassiveRuntime {
    private static final ResourceId MODIFIER_ID = new ResourceId("expansion_mod", "tea_style_normal_attack");
    private final Config config;
    private float teaStyleRemaining;

    public ElegantTeaPartyPassive(Config config) {
        this.config = config;
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        teaStyleRemaining = Math.max(teaStyleRemaining, config.skillStateDurationSeconds());
    }

    @Override
    public void onUltimateActivated(AbilityActivationContext context) {
        teaStyleRemaining = Math.max(teaStyleRemaining, config.ultimateStateDurationSeconds());
        context.actions().refundUltimatePoints(context.user(),
                context.user().maximumUltimatePoints() * config.ultimateRefundRatio());
    }

    @Override
    public void update(PassiveUpdateContext context) {
        teaStyleRemaining = Math.max(0f, teaStyleRemaining - context.deltaSeconds());
    }

    @Override
    public List<NormalAttackModifier> normalAttackModifiers(CombatantView owner) {
        if (teaStyleRemaining <= 0f) {
            return List.of();
        }
        return List.of(new NormalAttackModifier(
                MODIFIER_ID, 100, config.attackIntervalMultiplier(), config.projectileCountDelta(),
                config.spreadDegrees(), 0, config.damageMultiplier(),
                config.projectileTexture(), config.effectId()));
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return teaStyleRemaining <= 0f ? List.of() : List.of(AbilityStateLine.of(
                "state.expansion_mod.tea.tea_style", 100, Math.ceil(teaStyleRemaining * 10f) / 10f));
    }

    public record Config(float skillStateDurationSeconds, float ultimateStateDurationSeconds,
                         float ultimateRefundRatio, float attackIntervalMultiplier,
                         int projectileCountDelta, float spreadDegrees, float damageMultiplier,
                         ResourceId projectileTexture, ResourceId effectId) {
        public Config {
            if (ultimateRefundRatio > 1f) {
                throw new IllegalArgumentException("ultimateRefundRatio must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "skillStateDurationSeconds", 3f),
                    ConfigValues.positiveFloat(values, "ultimateStateDurationSeconds", 12f),
                    ConfigValues.nonNegativeFloat(values, "ultimateRefundRatio", .5f),
                    ConfigValues.positiveFloat(values, "attackIntervalMultiplier", .58f),
                    ConfigValues.positiveInt(values, "projectileCountDelta", 2),
                    ConfigValues.nonNegativeFloat(values, "spreadDegrees", 42f),
                    ConfigValues.nonNegativeFloat(values, "damageMultiplier", .85f),
                    ConfigValues.resourceId(values, "projectileTexture", "expansion_mod:textures/tea.png"),
                    ConfigValues.resourceId(values, "effectId", "mojibattle:kana"));
        }
    }
}

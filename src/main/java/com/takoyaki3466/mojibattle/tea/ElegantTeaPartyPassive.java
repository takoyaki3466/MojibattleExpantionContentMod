package com.takoyaki3466.mojibattle.tea;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.context.PassiveUpdateContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.PassiveRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.modifier.NormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.List;

public final class ElegantTeaPartyPassive extends AbstractRuntime<ElegantTeaPartyPassive.Config> implements PassiveRuntime {
    private static final ResourceId MODIFIER_ID = ExpansionMod.ofId("tea_style_normal_attack");
    private float teaStyleRemaining;

    public ElegantTeaPartyPassive(Config config) {
        super(config);
    }

    @Override
    public void onSkillActivated(AbilityActivationContext context) {
        teaStyleRemaining = Math.max(teaStyleRemaining, getConfig().skillStateDurationSeconds());
    }

    @Override
    public void onUltimateActivated(AbilityActivationContext context) {
        teaStyleRemaining = Math.max(teaStyleRemaining, getConfig().ultimateStateDurationSeconds());
        context.actions().refundUltimatePoints(context.user(), context.user().maximumUltimatePoints() * getConfig().ultimateRefundRatio());
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
        return List.of(new NormalAttackModifier(MODIFIER_ID, 100, getConfig().attackIntervalMultiplier(), getConfig().projectileCountDelta(), getConfig().spreadDegrees(), 0, getConfig().damageMultiplier(), getConfig().projectileTexture(), getConfig().effectId()));
    }

    @Override
    public List<AbilityStateLine> stateLines(CombatantView owner) {
        return teaStyleRemaining <= 0f ? List.of() : List.of(AbilityStateLine.of("state.expansion_mod.tea.tea_style", 100, Math.ceil(teaStyleRemaining * 10f) / 10f));
    }

    public record Config(
            @ConfigFloat(defaultValue = 3f, exclusiveMin = true) float skillStateDurationSeconds,
            @ConfigFloat(defaultValue = 12f, exclusiveMin = true) float ultimateStateDurationSeconds,
            @ConfigFloat(defaultValue = .5f, max = 1f) float ultimateRefundRatio,
            @ConfigFloat(defaultValue = .58f, exclusiveMin = true) float attackIntervalMultiplier,
            @ConfigInt(defaultValue = 2, min = 1) int projectileCountDelta,
            @ConfigFloat(defaultValue = 42f) float spreadDegrees,
            @ConfigFloat(defaultValue = .85f) float damageMultiplier,
            @ConfigResource(defaultValue = "expansion_mod:textures/tea.png") ResourceId projectileTexture,
            @ConfigResource(defaultValue = "mojibattle:kana") ResourceId effectId) {
    }
}

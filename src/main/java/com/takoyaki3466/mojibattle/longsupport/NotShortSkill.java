package com.takoyaki3466.mojibattle.longsupport;


import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.status.StatusDuration;
import jp.takoa.mojibattle.battle.status.StatusEffectType;
import jp.takoa.mojibattle.battle.status.StatusSpec;
import jp.takoa.mojibattle.resource.ResourceId;

public final class NotShortSkill extends AbstractRuntime<NotShortSkill.Config> implements SupportAbilityRuntime {
    private static final ResourceId DEFENSE_STATUS = ExpansionMod.ofId("not_short_defense");

    public NotShortSkill(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        for (CombatantView ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally, StatusSpec.multiplier(DEFENSE_STATUS, StatusEffectType.INCOMING_DAMAGE_MULTIPLIER, getConfig().incomingDamageMultiplier(), StatusDuration.seconds(getConfig().durationSeconds())));
            context.actions().healRatio(ally, getConfig().healRatio());
        }
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = .7f, exclusiveMin = true, max = 1f) float incomingDamageMultiplier,
            @ConfigFloat(defaultValue = 8f, exclusiveMin = true) float durationSeconds,
            @ConfigFloat(defaultValue = .05f, max = 1f) float healRatio) {
    }
}

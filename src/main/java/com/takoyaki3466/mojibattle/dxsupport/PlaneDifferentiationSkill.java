package com.takoyaki3466.mojibattle.dxsupport;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.status.*;
import jp.takoa.mojibattle.resource.ResourceId;

public final class PlaneDifferentiationSkill extends AbstractRuntime<PlaneDifferentiationSkill.Config> implements SupportAbilityRuntime {
    private static final ResourceId SHIELD_ID = ExpansionMod.ofId("plane_differentiation_shield");

    public PlaneDifferentiationSkill(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        StatusSpec shield = new StatusSpec(SHIELD_ID, StatusEffectType.NEXT_INCOMING_DAMAGE_MULTIPLIER, getConfig().damageMultiplier(), StatusDuration.permanent(), StatusStacking.REPLACE, 1, 1f, StatusConsumption.ON_DAMAGE_RECEIVED);

        for (CombatantView ally : context.actions().alliesOf(context.owner())) {
            context.actions().applyStatus(context.owner(), ally, shield);
        }
        return true;
    }

    public record Config(@ConfigFloat(defaultValue = .5f, exclusiveMin = true, max = 1f) float damageMultiplier) {
    }
}

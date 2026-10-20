package com.takoyaki3466.mojibattle.groupsupport;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.SupportPassiveContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.SupportPassiveRuntime;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.List;

public final class LetterGroupPassive extends AbstractRuntime<LetterGroupPassive.Config> implements SupportPassiveRuntime {
    private static final ResourceId MODIFIER_ID = ExpansionMod.ofId("letter_group_battlefield_count");

    public LetterGroupPassive(Config config) {
        super(config);
    }

    @Override
    public void onBattleStart(SupportPassiveContext context) {
        context.supportActions().setBattlefieldCountAttackMultiplier(MODIFIER_ID, getConfig().priority(), getConfig().baseMultiplier(), getConfig().multiplierPerCombatant());
    }

    @Override
    public List<AbilityStateLine> stateLines(SupportPassiveContext context) {
        int combatants = context.actions().battlefield().livingCombatants().size();
        float multiplier = Math.max(0f, getConfig().baseMultiplier() + getConfig().multiplierPerCombatant() * combatants);
        float roundedMultiplier = Math.round(multiplier * 100f) / 100f;
        return List.of(AbilityStateLine.of("state.expansion_mod.group.battlefield_power", 100, combatants, roundedMultiplier));
    }

    public record Config(
            @ConfigInt(defaultValue = 100) int priority,
            @ConfigFloat(defaultValue = .9f) float baseMultiplier,
            @ConfigFloat(defaultValue = .05f) float multiplierPerCombatant) {
    }
}

package com.takoyaki3466.mojibattle.groupsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportPassiveContext;
import jp.takoa.mojibattle.ability.SupportPassiveRuntime;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.List;

public final class LetterGroupPassive implements SupportPassiveRuntime {
    private static final ResourceId MODIFIER_ID =
            new ResourceId("expansion_mod", "letter_group_battlefield_count");
    private final Config config;

    public LetterGroupPassive(Config config) {
        this.config = config;
    }

    @Override
    public void onBattleStart(SupportPassiveContext context) {
        context.supportActions().setBattlefieldCountAttackMultiplier(
                MODIFIER_ID, config.priority(), config.baseMultiplier(), config.multiplierPerCombatant());
    }

    @Override
    public List<AbilityStateLine> stateLines(SupportPassiveContext context) {
        int combatants = context.actions().battlefield().livingCombatants().size();
        float multiplier = Math.max(0f,
                config.baseMultiplier() + config.multiplierPerCombatant() * combatants);
        float roundedMultiplier = Math.round(multiplier * 100f) / 100f;
        return List.of(AbilityStateLine.of(
                "state.expansion_mod.group.battlefield_power", 100, combatants, roundedMultiplier));
    }

    public record Config(int priority, float baseMultiplier, float multiplierPerCombatant) {
        public static Config from(ConfigView values) {
            return new Config(
                    values.intValue("priority", 100),
                    ConfigValues.nonNegativeFloat(values, "baseMultiplier", .9f),
                    ConfigValues.nonNegativeFloat(values, "multiplierPerCombatant", .05f));
        }
    }
}

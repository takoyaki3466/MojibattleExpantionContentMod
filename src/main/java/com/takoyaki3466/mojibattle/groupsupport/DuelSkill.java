package com.takoyaki3466.mojibattle.groupsupport;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.combatant.BattlefieldView;
import jp.takoa.mojibattle.battle.combatant.CombatantKind;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.battle.rule.DuelSpec;

import java.util.Comparator;
import java.util.List;

public final class DuelSkill extends AbstractRuntime<DuelSkill.Config> implements SupportAbilityRuntime {

    public DuelSkill(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        BattlefieldView battlefield = context.actions().battlefield();
        CombatantView enemy = battlefield.enemiesOf(context.owner()).stream().filter(CombatantView::alive).max(Comparator.comparingDouble(CombatantView::hp).thenComparing(CombatantView::instanceId)).orElse(null);

        if (enemy == null) {
            return false;
        }

        String supportInstanceId = context.support().instanceId();
        String supportDefinitionId = context.support().id();

        List<CombatantView> participants = battlefield.alliesOf(context.owner()).stream().filter(CombatantView::alive).filter(value -> value.instanceId().equals(supportInstanceId) || value.kind() == CombatantKind.SUMMON && value.id().equals(supportDefinitionId) && value.instanceId().startsWith(supportInstanceId + ":split:")).toList();

        if (participants.isEmpty()) {
            return false;
        }

        context.actions().startDuel(new DuelSpec(participants, enemy, getConfig().durationSeconds(), true, getConfig().participantDamageTakenMultiplier(), getConfig().outsideAttackerDamageMultiplier()));
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = 8f, exclusiveMin = true) float durationSeconds,
            @ConfigFloat(defaultValue = 1.5f, exclusiveMin = true) float participantDamageTakenMultiplier,
            @ConfigFloat(defaultValue = .2f, max = 1f) float outsideAttackerDamageMultiplier) {
    }
}

package com.takoyaki3466.mojibattle.groupsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportSkillRuntime;
import jp.takoa.mojibattle.battle.CombatantKind;
import jp.takoa.mojibattle.battle.CombatantView;
import jp.takoa.mojibattle.battle.DuelSpec;

import java.util.Comparator;
import java.util.List;

public final class DuelSkill implements SupportSkillRuntime {
    private final Config config;

    public DuelSkill(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        var battlefield = context.actions().battlefield();
        CombatantView enemy = battlefield.enemiesOf(context.owner()).stream()
                .filter(CombatantView::alive)
                .max(Comparator.comparingDouble(CombatantView::hp)
                        .thenComparing(CombatantView::instanceId))
                .orElse(null);
        if (enemy == null) {
            return false;
        }

        String supportInstanceId = context.support().instanceId();
        String supportDefinitionId = context.support().id();
        List<CombatantView> participants = battlefield.alliesOf(context.owner()).stream()
                .filter(CombatantView::alive)
                .filter(value -> value.instanceId().equals(supportInstanceId)
                        || value.kind() == CombatantKind.SUMMON
                        && value.id().equals(supportDefinitionId)
                        && value.instanceId().startsWith(supportInstanceId + ":split:"))
                .toList();
        if (participants.isEmpty()) {
            return false;
        }

        context.actions().startDuel(new DuelSpec(
                participants, enemy, config.durationSeconds(), true,
                config.participantDamageTakenMultiplier(),
                config.outsideAttackerDamageMultiplier()));
        return true;
    }

    public record Config(float durationSeconds, float participantDamageTakenMultiplier,
                         float outsideAttackerDamageMultiplier) {
        public Config {
            if (outsideAttackerDamageMultiplier > 1f) {
                throw new IllegalArgumentException("outsideAttackerDamageMultiplier must not exceed 1");
            }
        }

        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "durationSeconds", 8f),
                    ConfigValues.positiveFloat(values, "participantDamageTakenMultiplier", 1.5f),
                    ConfigValues.nonNegativeFloat(values, "outsideAttackerDamageMultiplier", .2f));
        }
    }
}

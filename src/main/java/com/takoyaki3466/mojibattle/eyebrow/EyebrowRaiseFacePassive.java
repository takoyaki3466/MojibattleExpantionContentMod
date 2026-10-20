package com.takoyaki3466.mojibattle.eyebrow;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.context.SupportPassiveContext;
import jp.takoa.mojibattle.ability.presentation.AbilityStateLine;
import jp.takoa.mojibattle.ability.runtime.SupportPassiveRuntime;
import jp.takoa.mojibattle.battle.event.NormalAttackEvent;

import java.util.List;

public final class EyebrowRaiseFacePassive extends AbstractRuntime<EyebrowRaiseFacePassive.Config> implements SupportPassiveRuntime {

    public EyebrowRaiseFacePassive(Config config) {
        super(config);
    }

    @Override
    public void onNormalAttack(SupportPassiveContext context, NormalAttackEvent event) {
        boolean ownAttack = event.attackerType() == NormalAttackEvent.AttackerType.SUPPORT && event.attackerInstanceId().equals(context.support().instanceId());
        if (ownAttack || event.team() == NormalAttackEvent.Team.ENEMY) {
            EyebrowState.add(context.state(), 1, getConfig().maximumLevel());
        }
    }

    @Override
    public List<AbilityStateLine> stateLines(SupportPassiveContext context) {
        return List.of(AbilityStateLine.of("state.expansion_mod.eyebrow.level", 100, EyebrowState.level(context.state(), getConfig().maximumLevel()), getConfig().maximumLevel()));
    }

    public record Config(@ConfigInt(defaultValue = 200, min = 1) int maximumLevel) {
    }
}

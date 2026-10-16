package com.takoyaki3466.mojibattle.eyebrow;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityStateLine;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportPassiveContext;
import jp.takoa.mojibattle.ability.SupportPassiveRuntime;
import jp.takoa.mojibattle.battle.NormalAttackEvent;

import java.util.List;

public final class EyebrowRaiseFacePassive implements SupportPassiveRuntime {
    private final Config config;

    public EyebrowRaiseFacePassive(Config config) {
        this.config = config;
    }

    @Override
    public void onNormalAttack(SupportPassiveContext context, NormalAttackEvent event) {
        boolean ownAttack = event.attackerType() == NormalAttackEvent.AttackerType.SUPPORT
                && event.attackerInstanceId().equals(context.support().instanceId());
        if (ownAttack || event.team() == NormalAttackEvent.Team.ENEMY) {
            EyebrowState.add(context.state(), 1, config.maximumLevel());
        }
    }

    @Override
    public List<AbilityStateLine> stateLines(SupportPassiveContext context) {
        return List.of(AbilityStateLine.of("state.expansion_mod.eyebrow.level", 100,
                EyebrowState.level(context.state(), config.maximumLevel()), config.maximumLevel()));
    }

    public record Config(int maximumLevel) {
        public static Config from(ConfigView values) {
            return new Config(ConfigValues.positiveInt(values, "maximumLevel", 200));
        }
    }
}

package com.takoyaki3466.mojibattle.tea;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.AbilityActivationContext;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.UltimateRuntime;
import jp.takoa.mojibattle.resource.ResourceId;

public final class TeaStyleUltimate implements UltimateRuntime {
    private final Config config;

    public TeaStyleUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        context.actions().emitEffect(context.user(), config.effectId(),
                config.effectScale(), config.effectDurationSeconds());
        return true;
    }

    public record Config(ResourceId effectId, float effectScale, float effectDurationSeconds) {
        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.resourceId(values, "effectId", "mojibattle:kana"),
                    ConfigValues.positiveFloat(values, "effectScale", 10f),
                    ConfigValues.positiveFloat(values, "effectDurationSeconds", 1.2f));
        }
    }
}

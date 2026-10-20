package com.takoyaki3466.mojibattle.tea;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.AbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.UltimateRuntime;
import jp.takoa.mojibattle.resource.ResourceId;

public final class TeaStyleUltimate extends AbstractRuntime<TeaStyleUltimate.Config> implements UltimateRuntime {

    public TeaStyleUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean tryActivate(AbilityActivationContext context) {
        context.actions().emitEffect(context.user(), getConfig().effectId(), getConfig().effectScale(), getConfig().effectDurationSeconds());
        return true;
    }

    public record Config(
            @ConfigResource(defaultValue = "mojibattle:kana") ResourceId effectId,
            @ConfigFloat(defaultValue = 10f, exclusiveMin = true) float effectScale,
            @ConfigFloat(defaultValue = 1.2f, exclusiveMin = true) float effectDurationSeconds) {
    }
}

package com.takoyaki3466.mojibattle.dxsupport;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.modifier.SupportNormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

public final class InfiniteDifferentiationUltimate extends AbstractRuntime<InfiniteDifferentiationUltimate.Config> implements SupportAbilityRuntime {
    private static final ResourceId MODIFIER_ID = ExpansionMod.ofId("infinite_differentiation");

    public InfiniteDifferentiationUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        context.supportActions().addNormalAttackModifier(new SupportNormalAttackModifier(MODIFIER_ID, 100, SupportNormalAttackModifier.Stacking.MULTIPLY, getConfig().damageMultiplier(), getConfig().intervalMultiplier(), 1, getConfig().hitsPerAttack(), 1f, getConfig().projectileTexture(), getConfig().effectId()));
        return true;
    }

    public record Config(
            @ConfigFloat(defaultValue = .55f, exclusiveMin = true) float damageMultiplier,
            @ConfigFloat(defaultValue = .82f, exclusiveMin = true, max = 1f) float intervalMultiplier,
            @ConfigInt(defaultValue = 3, min = 1) int hitsPerAttack,
            @ConfigResource(defaultValue = "expansion_mod:textures/dx.png") ResourceId projectileTexture,
            @ConfigResource(defaultValue = "mojibattle:impact") ResourceId effectId) {
    }
}

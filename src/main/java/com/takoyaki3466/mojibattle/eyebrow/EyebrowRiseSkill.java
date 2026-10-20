package com.takoyaki3466.mojibattle.eyebrow;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigInt;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.battle.action.spec.ProjectileSpec;
import jp.takoa.mojibattle.battle.damage.DamageRequest;
import jp.takoa.mojibattle.battle.damage.DamageType;
import jp.takoa.mojibattle.battle.modifier.SupportNormalAttackModifier;
import jp.takoa.mojibattle.resource.ResourceId;

public final class EyebrowRiseSkill extends AbstractRuntime<EyebrowRiseSkill.Config> implements SupportAbilityRuntime {
    private static final ResourceId HASTE_ID = ExpansionMod.ofId("eyebrow_rise_haste");

    public EyebrowRiseSkill(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        if (context.target() == null || !context.target().alive()) {
            return false;
        }
        EyebrowState.add(context.state(), getConfig().levelGain(), getConfig().maximumLevel());
        context.supportActions().addNormalAttackModifier(new SupportNormalAttackModifier(HASTE_ID, 200, SupportNormalAttackModifier.Stacking.MULTIPLY, 1f, getConfig().intervalMultiplier(), 1, 1, 1f, null, getConfig().effectId()), getConfig().durationSeconds());
        context.actions().spawnProjectile(context.owner(), context.target(), new ProjectileSpec(DamageRequest.of(getConfig().damage(), DamageType.SKILL), getConfig().projectileSpeed(), getConfig().projectileRadius(), getConfig().projectileLifetimeSeconds(), 1, 0f, 1, getConfig().projectileTexture(), getConfig().effectId()));
        return true;
    }

    public record Config(
            @ConfigInt(defaultValue = 10, min = 1) int levelGain, @ConfigInt(defaultValue = 200, min = 1) int maximumLevel,
            @ConfigFloat(defaultValue = .65f, exclusiveMin = true, max = 1f) float intervalMultiplier,
            @ConfigFloat(defaultValue = 6f, exclusiveMin = true) float durationSeconds,
            @ConfigFloat(defaultValue = 42f) float damage,
            @ConfigFloat(defaultValue = 15f, exclusiveMin = true) float projectileSpeed,
            @ConfigFloat(defaultValue = .5f, exclusiveMin = true) float projectileRadius,
            @ConfigFloat(defaultValue = 2.5f, exclusiveMin = true) float projectileLifetimeSeconds,
            @ConfigResource(defaultValue = "expansion_mod:textures/eyebrow.png") ResourceId projectileTexture,
            @ConfigResource(defaultValue = "mojibattle:critical") ResourceId effectId) {

        public Config {
            if (levelGain > maximumLevel) {
                throw new IllegalArgumentException("Config 'levelGain' must not exceed 'maximumLevel': "
                        + levelGain + " > " + maximumLevel);
            }
        }
    }
}

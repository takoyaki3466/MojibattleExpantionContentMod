package com.takoyaki3466.mojibattle.groupsupport;

import jp.takoa.mojibattle.ability.config.AbstractRuntime;
import jp.takoa.mojibattle.ability.config.annotations.ConfigFloat;
import jp.takoa.mojibattle.ability.config.annotations.ConfigResource;
import jp.takoa.mojibattle.ability.context.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.ability.support.SupportSplitMemberSpec;
import jp.takoa.mojibattle.ability.support.SupportSplitSpec;
import jp.takoa.mojibattle.battle.combatant.BattlefieldView;
import jp.takoa.mojibattle.battle.combatant.CombatantView;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.Comparator;
import java.util.List;

public final class UnitedAsOneUltimate extends AbstractRuntime<UnitedAsOneUltimate.Config> implements SupportAbilityRuntime {
    private static final int MEMBER_COUNT = 5;

    public UnitedAsOneUltimate(Config config) {
        super(config);
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        BattlefieldView battlefield = context.actions().battlefield();
        CombatantView target = context.target();

        if (target == null || !target.alive()) {
            target = battlefield.enemiesOf(context.owner()).stream().filter(CombatantView::alive).max(Comparator.comparingDouble(CombatantView::hp).thenComparing(CombatantView::instanceId)).orElse(null);
        }
        if (target == null) {
            return false;
        }

        int projectedCombatants = Math.max(0, battlefield.livingCombatants().size() - 1) + MEMBER_COUNT;

        float countMultiplier = Math.max(0f, getConfig().countBaseMultiplier() + getConfig().multiplierPerCombatant() * projectedCombatants);
        float memberDamage = getConfig().memberBaseDamage() * countMultiplier;

        List<SupportSplitMemberSpec> members = List.of(member("サ", getConfig().saTexture(), memberDamage), member("ン", getConfig().nTexture(), memberDamage), member("チ", getConfig().chiTexture(), memberDamage), member("ー", getConfig().longMarkTexture(), memberDamage), member("ム", getConfig().muTexture(), memberDamage));

        context.supportActions().splitTemporarily(new SupportSplitSpec(members, getConfig().durationSeconds(), target, true, true));
        return true;
    }

    private SupportSplitMemberSpec member(String glyph, ResourceId texture, float damage) {
        return new SupportSplitMemberSpec(glyph, texture, damage, getConfig().memberAttackIntervalSeconds(), getConfig().memberMoveSpeed());
    }

    public record Config(
            @ConfigFloat(defaultValue = 12f, exclusiveMin = true) float durationSeconds,
            @ConfigFloat(defaultValue = 11f) float memberBaseDamage,
            @ConfigFloat(defaultValue = 1.1f, exclusiveMin = true) float memberAttackIntervalSeconds,
            @ConfigFloat(defaultValue = 9f) float memberMoveSpeed,
            @ConfigFloat(defaultValue = .9f) float countBaseMultiplier,
            @ConfigFloat(defaultValue = .05f) float multiplierPerCombatant,
            @ConfigResource(defaultValue = "expansion_mod:textures/group_sa.png") ResourceId saTexture,
            @ConfigResource(defaultValue = "expansion_mod:textures/group_n.png") ResourceId nTexture,
            @ConfigResource(defaultValue = "expansion_mod:textures/group_chi.png") ResourceId chiTexture,
            @ConfigResource(defaultValue = "expansion_mod:textures/group_long_mark.png") ResourceId longMarkTexture,
            @ConfigResource(defaultValue = "expansion_mod:textures/group_mu.png") ResourceId muTexture) {
    }
}

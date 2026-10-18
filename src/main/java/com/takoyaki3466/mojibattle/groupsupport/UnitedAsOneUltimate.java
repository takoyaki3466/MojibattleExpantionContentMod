package com.takoyaki3466.mojibattle.groupsupport;

import com.takoyaki3466.mojibattle.config.ConfigValues;
import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.ability.SupportAbilityActivationContext;
import jp.takoa.mojibattle.ability.SupportSplitMemberSpec;
import jp.takoa.mojibattle.ability.SupportSplitSpec;
import jp.takoa.mojibattle.ability.SupportUltimateRuntime;
import jp.takoa.mojibattle.battle.CombatantView;
import jp.takoa.mojibattle.resource.ResourceId;

import java.util.Comparator;
import java.util.List;

public final class UnitedAsOneUltimate implements SupportUltimateRuntime {
    private static final int MEMBER_COUNT = 5;
    private final Config config;

    public UnitedAsOneUltimate(Config config) {
        this.config = config;
    }

    @Override
    public boolean activate(SupportAbilityActivationContext context) {
        var battlefield = context.actions().battlefield();
        CombatantView target = context.target();
        if (target == null || !target.alive()) {
            target = battlefield.enemiesOf(context.owner()).stream()
                    .filter(CombatantView::alive)
                    .max(Comparator.comparingDouble(CombatantView::hp)
                            .thenComparing(CombatantView::instanceId))
                    .orElse(null);
        }
        if (target == null) {
            return false;
        }

        int projectedCombatants = Math.max(0, battlefield.livingCombatants().size() - 1) + MEMBER_COUNT;
        float countMultiplier = Math.max(0f,
                config.countBaseMultiplier() + config.multiplierPerCombatant() * projectedCombatants);
        float memberDamage = config.memberBaseDamage() * countMultiplier;
        List<SupportSplitMemberSpec> members = List.of(
                member("サ", config.saTexture(), memberDamage),
                member("ン", config.nTexture(), memberDamage),
                member("チ", config.chiTexture(), memberDamage),
                member("ー", config.longMarkTexture(), memberDamage),
                member("ム", config.muTexture(), memberDamage));
        context.supportActions().splitTemporarily(new SupportSplitSpec(
                members, config.durationSeconds(), target, true, true));
        return true;
    }

    private SupportSplitMemberSpec member(String glyph, ResourceId texture, float damage) {
        return new SupportSplitMemberSpec(
                glyph, texture, damage, config.memberAttackIntervalSeconds(), config.memberMoveSpeed());
    }

    public record Config(float durationSeconds, float memberBaseDamage,
                         float memberAttackIntervalSeconds, float memberMoveSpeed,
                         float countBaseMultiplier, float multiplierPerCombatant,
                         ResourceId saTexture, ResourceId nTexture, ResourceId chiTexture,
                         ResourceId longMarkTexture, ResourceId muTexture) {
        public static Config from(ConfigView values) {
            return new Config(
                    ConfigValues.positiveFloat(values, "durationSeconds", 12f),
                    ConfigValues.nonNegativeFloat(values, "memberBaseDamage", 11f),
                    ConfigValues.positiveFloat(values, "memberAttackIntervalSeconds", 1.1f),
                    ConfigValues.nonNegativeFloat(values, "memberMoveSpeed", 9f),
                    ConfigValues.nonNegativeFloat(values, "countBaseMultiplier", .9f),
                    ConfigValues.nonNegativeFloat(values, "multiplierPerCombatant", .05f),
                    ConfigValues.resourceId(values, "saTexture", "expansion_mod:textures/group_sa.png"),
                    ConfigValues.resourceId(values, "nTexture", "expansion_mod:textures/group_n.png"),
                    ConfigValues.resourceId(values, "chiTexture", "expansion_mod:textures/group_chi.png"),
                    ConfigValues.resourceId(values, "longMarkTexture", "expansion_mod:textures/group_long_mark.png"),
                    ConfigValues.resourceId(values, "muTexture", "expansion_mod:textures/group_mu.png"));
        }
    }
}

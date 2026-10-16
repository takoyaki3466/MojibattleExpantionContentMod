package com.takoyaki3466.mojibattle;

import com.takoyaki3466.mojibattle.tree.ExpandingForestMazePassive;
import com.takoyaki3466.mojibattle.tree.ForestUniverseUltimate;
import com.takoyaki3466.mojibattle.tree.ReturnToForestSkill;
import com.takoyaki3466.mojibattle.gold.MoneyMakingExplosionUltimate;
import com.takoyaki3466.mojibattle.gold.RisingFortuneSkill;
import com.takoyaki3466.mojibattle.gold.TimeIsMoneyPassive;
import com.takoyaki3466.mojibattle.longsupport.NotShortSkill;
import com.takoyaki3466.mojibattle.longsupport.ProsAndConsUltimate;
import com.takoyaki3466.mojibattle.tea.ElegantTeaPartyPassive;
import com.takoyaki3466.mojibattle.tea.MakeTeaSkill;
import com.takoyaki3466.mojibattle.tea.TeaStyleUltimate;
import com.takoyaki3466.mojibattle.dxsupport.InfiniteDifferentiationUltimate;
import com.takoyaki3466.mojibattle.dxsupport.PlaneDifferentiationSkill;
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRaiseFacePassive;
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRiseSkill;
import com.takoyaki3466.mojibattle.eyebrow.SkepticalVolleyUltimate;
import jp.takoa.mojibattle.content.CharacterDataRegistration;
import jp.takoa.mojibattle.content.FieldEffectDataRegistration;
import jp.takoa.mojibattle.content.SupportCharacterDataRegistration;
import jp.takoa.mojibattle.mod.GameMod;
import jp.takoa.mojibattle.mod.ModContext;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ExpansionMod implements GameMod {
    public static final String MOD_ID = "expansion_mod";

    private static final String TREE_SKILL_IMPLEMENTATION = MOD_ID + ":return_to_forest";
    private static final String TREE_ULTIMATE_IMPLEMENTATION = MOD_ID + ":forest_universe";
    private static final String TREE_PASSIVE_IMPLEMENTATION = MOD_ID + ":expanding_forest_maze";
    private static final String GOLD_SKILL_IMPLEMENTATION = MOD_ID + ":rising_fortune";
    private static final String GOLD_ULTIMATE_IMPLEMENTATION = MOD_ID + ":money_making_explosion";
    private static final String GOLD_PASSIVE_IMPLEMENTATION = MOD_ID + ":time_is_money";
    private static final String TEA_SKILL_IMPLEMENTATION = MOD_ID + ":make_tea";
    private static final String TEA_ULTIMATE_IMPLEMENTATION = MOD_ID + ":tea_style";
    private static final String TEA_PASSIVE_IMPLEMENTATION = MOD_ID + ":elegant_tea_party";
    private static final String LONG_SKILL_IMPLEMENTATION = MOD_ID + ":not_short";
    private static final String LONG_ULTIMATE_IMPLEMENTATION = MOD_ID + ":pros_and_cons";
    private static final String DX_SKILL_IMPLEMENTATION = MOD_ID + ":plane_differentiation";
    private static final String DX_ULTIMATE_IMPLEMENTATION = MOD_ID + ":infinite_differentiation";
    private static final String EYEBROW_SKILL_IMPLEMENTATION = MOD_ID + ":eyebrow_rise";
    private static final String EYEBROW_ULTIMATE_IMPLEMENTATION = MOD_ID + ":skeptical_volley";
    private static final String EYEBROW_PASSIVE_IMPLEMENTATION = MOD_ID + ":eyebrow_raise_face";

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void register(ModContext context) {
        registerTreeAbilities(context);
        registerGoldAbilities(context);
        registerTeaAbilities(context);
        registerLongAbilities(context);
        registerDxAbilities(context);
        registerEyebrowAbilities(context);
        registerMainCharacter(context, "tree");
        registerMainCharacter(context, "gold");
        registerMainCharacter(context, "tea");
        context.content().registerFieldEffect(new FieldEffectDataRegistration(resource("field_effects/long_long_ago.json")));
        registerFieldEffect(context, "differential_move_slow");
        registerFieldEffect(context, "differential_attack_down");
        registerFieldEffect(context, "differential_defense_down");
        registerFieldEffect(context, "differential_integral_haste");
        registerFieldEffect(context, "eyebrow_passive_marker");
        context.content().registerSupportCharacter(new SupportCharacterDataRegistration(resource("support/long.json"), "i18n"));
        context.content().registerSupportCharacter(new SupportCharacterDataRegistration(resource("support/dx.json"), "i18n"));
        context.content().registerSupportCharacter(new SupportCharacterDataRegistration(resource("support/eyebrow.json"), "i18n"));
    }

    private static void registerTreeAbilities(ModContext context) {
        context.abilities().registerSkillImplementation(TREE_SKILL_IMPLEMENTATION, parameters -> new ReturnToForestSkill(ReturnToForestSkill.Config.from(parameters)));
        context.abilities().registerUltimateImplementation(TREE_ULTIMATE_IMPLEMENTATION, parameters -> new ForestUniverseUltimate(ForestUniverseUltimate.Config.from(parameters)));
        context.abilities().registerPassiveImplementation(TREE_PASSIVE_IMPLEMENTATION, parameters -> new ExpandingForestMazePassive(ExpandingForestMazePassive.Config.from(parameters)));
    }

    private static void registerGoldAbilities(ModContext context) {
        context.abilities().registerSkillImplementation(GOLD_SKILL_IMPLEMENTATION, parameters -> new RisingFortuneSkill(RisingFortuneSkill.Config.from(parameters)));
        context.abilities().registerUltimateImplementation(GOLD_ULTIMATE_IMPLEMENTATION, parameters -> new MoneyMakingExplosionUltimate(MoneyMakingExplosionUltimate.Config.from(parameters)));
        context.abilities().registerPassiveImplementation(GOLD_PASSIVE_IMPLEMENTATION, parameters -> new TimeIsMoneyPassive(TimeIsMoneyPassive.Config.from(parameters)));
    }

    private static void registerTeaAbilities(ModContext context) {
        context.abilities().registerSkillImplementation(TEA_SKILL_IMPLEMENTATION, parameters -> new MakeTeaSkill(MakeTeaSkill.Config.from(parameters)));
        context.abilities().registerUltimateImplementation(TEA_ULTIMATE_IMPLEMENTATION, parameters -> new TeaStyleUltimate(TeaStyleUltimate.Config.from(parameters)));
        context.abilities().registerPassiveImplementation(TEA_PASSIVE_IMPLEMENTATION, parameters -> new ElegantTeaPartyPassive(ElegantTeaPartyPassive.Config.from(parameters)));
    }

    private static void registerLongAbilities(ModContext context) {
        context.abilities().registerSupportSkillImplementation(LONG_SKILL_IMPLEMENTATION, parameters -> new NotShortSkill(NotShortSkill.Config.from(parameters)));
        context.abilities().registerSupportUltimateImplementation(LONG_ULTIMATE_IMPLEMENTATION, parameters -> new ProsAndConsUltimate(ProsAndConsUltimate.Config.from(parameters)));
    }

    private static void registerDxAbilities(ModContext context) {
        context.abilities().registerSupportSkillImplementation(DX_SKILL_IMPLEMENTATION,
                parameters -> new PlaneDifferentiationSkill(PlaneDifferentiationSkill.Config.from(parameters)));
        context.abilities().registerSupportUltimateImplementation(DX_ULTIMATE_IMPLEMENTATION,
                parameters -> new InfiniteDifferentiationUltimate(InfiniteDifferentiationUltimate.Config.from(parameters)));
    }

    private static void registerEyebrowAbilities(ModContext context) {
        context.abilities().registerSupportSkillImplementation(EYEBROW_SKILL_IMPLEMENTATION,
                parameters -> new EyebrowRiseSkill(EyebrowRiseSkill.Config.from(parameters)));
        context.abilities().registerSupportUltimateImplementation(EYEBROW_ULTIMATE_IMPLEMENTATION,
                parameters -> new SkepticalVolleyUltimate(SkepticalVolleyUltimate.Config.from(parameters)));
        context.abilities().registerSupportPassiveImplementation(EYEBROW_PASSIVE_IMPLEMENTATION,
                parameters -> new EyebrowRaiseFacePassive(EyebrowRaiseFacePassive.Config.from(parameters)));
    }

    private static void registerFieldEffect(ModContext context, String id) {
        context.content().registerFieldEffect(new FieldEffectDataRegistration(resource("field_effects/" + id + ".json")));
    }

    private static void registerMainCharacter(ModContext context, String id) {
        context.content().registerCharacter(new CharacterDataRegistration(resource("characters/" + id + ".json"), resource("abilities/" + id + "_skill.json"), resource("abilities/" + id + "_ultimate.json"), resource("abilities/" + id + "_passive.json"), "i18n"));
    }

    private static ResourceId resource(String path) {
        return new ResourceId(MOD_ID, path);
    }
}

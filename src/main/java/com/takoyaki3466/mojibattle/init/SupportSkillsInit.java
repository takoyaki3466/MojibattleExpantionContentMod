package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.dxsupport.PlaneDifferentiationSkill;
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRiseSkill;
import com.takoyaki3466.mojibattle.groupsupport.DuelSkill;
import com.takoyaki3466.mojibattle.longsupport.NotShortSkill;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.SupportAbilityImpl;

public class SupportSkillsInit {
    public static final MojiBattleRegister.SupportAbility SUPPORT_SKILLS = MojiBattleRegister.createSupportAbility(ExpansionMod.MOD_ID);

    public static final RegistryHandle<SupportAbilityImpl> LONG = ConfigRegistration.supportAbility(SUPPORT_SKILLS, ExpansionMod.MOD_ID, "not_short", NotShortSkill.Config.class, NotShortSkill::new);
    public static final RegistryHandle<SupportAbilityImpl> DX = ConfigRegistration.supportAbility(SUPPORT_SKILLS, ExpansionMod.MOD_ID, "plane_differentiation", PlaneDifferentiationSkill.Config.class, PlaneDifferentiationSkill::new);
    public static final RegistryHandle<SupportAbilityImpl> EYEBROW = ConfigRegistration.supportAbility(SUPPORT_SKILLS, ExpansionMod.MOD_ID, "eyebrow_rise", EyebrowRiseSkill.Config.class, EyebrowRiseSkill::new);
    public static final RegistryHandle<SupportAbilityImpl> GROUPS = ConfigRegistration.supportAbility(SUPPORT_SKILLS, ExpansionMod.MOD_ID, "duel", DuelSkill.Config.class, DuelSkill::new);
}

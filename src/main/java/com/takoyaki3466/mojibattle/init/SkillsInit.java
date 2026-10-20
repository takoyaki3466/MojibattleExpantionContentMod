package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.gold.RisingFortuneSkill;
import com.takoyaki3466.mojibattle.sigma.AccumulationSkill;
import com.takoyaki3466.mojibattle.tea.MakeTeaSkill;
import com.takoyaki3466.mojibattle.tree.ReturnToForestSkill;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.SkillImpl;


public class SkillsInit {
    public static final MojiBattleRegister.Skill SKILLS = MojiBattleRegister.createSkill(ExpansionMod.MOD_ID);

    public static final RegistryHandle<SkillImpl> TREE = ConfigRegistration.skill(SKILLS, ExpansionMod.MOD_ID, "return_to_forest", ReturnToForestSkill.Config.class, ReturnToForestSkill::new);
    public static final RegistryHandle<SkillImpl> GOLD = ConfigRegistration.skill(SKILLS, ExpansionMod.MOD_ID, "rising_fortune", RisingFortuneSkill.Config.class, RisingFortuneSkill::new);
    public static final RegistryHandle<SkillImpl> TEA = ConfigRegistration.skill(SKILLS, ExpansionMod.MOD_ID, "make_tea", MakeTeaSkill.Config.class, MakeTeaSkill::new);
    public static final RegistryHandle<SkillImpl> SIGMA = ConfigRegistration.skill(SKILLS, ExpansionMod.MOD_ID, "accumulation", AccumulationSkill.Config.class, AccumulationSkill::new);

}

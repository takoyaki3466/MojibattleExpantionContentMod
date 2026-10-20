package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.dxsupport.InfiniteDifferentiationUltimate;
import com.takoyaki3466.mojibattle.eyebrow.SkepticalVolleyUltimate;
import com.takoyaki3466.mojibattle.groupsupport.UnitedAsOneUltimate;
import com.takoyaki3466.mojibattle.longsupport.ProsAndConsUltimate;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.SupportAbilityImpl;

public class SupportUltimatesInit {
    public static final MojiBattleRegister.SupportAbility SUPPORT_ULTIMATES = MojiBattleRegister.createSupportAbility(ExpansionMod.MOD_ID);

    public static final RegistryHandle<SupportAbilityImpl> LONG = ConfigRegistration.supportAbility(SUPPORT_ULTIMATES, ExpansionMod.MOD_ID, "pros_and_cons", ProsAndConsUltimate.Config.class, ProsAndConsUltimate::new);
    public static final RegistryHandle<SupportAbilityImpl> DX = ConfigRegistration.supportAbility(SUPPORT_ULTIMATES, ExpansionMod.MOD_ID, "infinite_differentiation", InfiniteDifferentiationUltimate.Config.class, InfiniteDifferentiationUltimate::new);
    public static final RegistryHandle<SupportAbilityImpl> EYEBROW = ConfigRegistration.supportAbility(SUPPORT_ULTIMATES, ExpansionMod.MOD_ID, "skeptical_volley", SkepticalVolleyUltimate.Config.class, SkepticalVolleyUltimate::new);
    public static final RegistryHandle<SupportAbilityImpl> GROUPS = ConfigRegistration.supportAbility(SUPPORT_ULTIMATES, ExpansionMod.MOD_ID, "united_as_one", UnitedAsOneUltimate.Config.class, UnitedAsOneUltimate::new);
}

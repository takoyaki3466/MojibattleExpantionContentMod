package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRaiseFacePassive;
import com.takoyaki3466.mojibattle.groupsupport.LetterGroupPassive;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.SupportPassiveImpl;

public class SupportPassivesInit {
    public static final MojiBattleRegister.SupportPassive SUPPORT_PASSIVES = MojiBattleRegister.createSupportPassive(ExpansionMod.MOD_ID);

    public static final RegistryHandle<SupportPassiveImpl> EYEBROW = ConfigRegistration.supportPassive(SUPPORT_PASSIVES, ExpansionMod.MOD_ID, "eyebrow_raise_face", EyebrowRaiseFacePassive.Config.class, EyebrowRaiseFacePassive::new);
    public static final RegistryHandle<SupportPassiveImpl> GROUPS = ConfigRegistration.supportPassive(SUPPORT_PASSIVES, ExpansionMod.MOD_ID, "letter_group", LetterGroupPassive.Config.class, LetterGroupPassive::new);

}

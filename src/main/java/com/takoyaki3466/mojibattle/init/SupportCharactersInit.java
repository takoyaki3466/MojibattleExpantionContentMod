package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.content.SupportCharacterDataRegistration;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.MojiBattleRegistries;
import jp.takoa.mojibattle.registry.RegistryHandle;

public class SupportCharactersInit {
    public static final MojiBattleRegister<SupportCharacterDataRegistration> SUPPORT_CHARACTERS = MojiBattleRegister.create(MojiBattleRegistries.SUPPORT_CHARACTERS, ExpansionMod.MOD_ID);

    public static final RegistryHandle<SupportCharacterDataRegistration> LONG = register("long");
    public static final RegistryHandle<SupportCharacterDataRegistration> DX = register("dx");
    public static final RegistryHandle<SupportCharacterDataRegistration> EYEBROW = register("eyebrow");
    public static final RegistryHandle<SupportCharacterDataRegistration> GROUPS = register("group");

    public static RegistryHandle<SupportCharacterDataRegistration> register(String name) {
        return SUPPORT_CHARACTERS.register(name, () -> new SupportCharacterDataRegistration(ExpansionMod.ofId("support/" + name + ".json"), "i18n"));
    }
}

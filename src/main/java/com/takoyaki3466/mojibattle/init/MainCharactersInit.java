package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.content.CharacterDataRegistration;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.MojiBattleRegistries;
import jp.takoa.mojibattle.registry.RegistryHandle;

public class MainCharactersInit {
    public static final MojiBattleRegister<CharacterDataRegistration> MAIN_CHARACTERS = MojiBattleRegister.create(MojiBattleRegistries.CHARACTERS, ExpansionMod.MOD_ID);

    public static final RegistryHandle<CharacterDataRegistration> TREE = register("tree");
    public static final RegistryHandle<CharacterDataRegistration> GOLD = register("gold");
    public static final RegistryHandle<CharacterDataRegistration> TEA = register("tea");
    public static final RegistryHandle<CharacterDataRegistration> SIGMA = register("sigma");

    public static RegistryHandle<CharacterDataRegistration> register(String name) {
        return MAIN_CHARACTERS.register(name,() -> new CharacterDataRegistration(ExpansionMod.ofId("characters/" + name + ".json"), ExpansionMod.ofId("abilities/" + name + "_skill.json"), ExpansionMod.ofId("abilities/" + name + "_ultimate.json"), ExpansionMod.ofId("abilities/" + name + "_passive.json"), "i18n"));
    }

}

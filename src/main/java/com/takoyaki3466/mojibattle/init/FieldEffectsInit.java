package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import jp.takoa.mojibattle.content.FieldEffectDataRegistration;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.MojiBattleRegistries;
import jp.takoa.mojibattle.registry.RegistryHandle;

public class FieldEffectsInit {
    public static final MojiBattleRegister<FieldEffectDataRegistration> FIELD_EFFECTS = MojiBattleRegister.create(MojiBattleRegistries.FIELD_EFFECTS, ExpansionMod.MOD_ID);


    public static final RegistryHandle<FieldEffectDataRegistration> MOVE_SLOW = register("differential_move_slow");
    public static final RegistryHandle<FieldEffectDataRegistration> ATTACK_DOWN = register("differential_attack_down");
    public static final RegistryHandle<FieldEffectDataRegistration> DEFENSE_DOWN = register("differential_defense_down");

    public static final RegistryHandle<FieldEffectDataRegistration> LONG_LONG_AGO = register("long_long_ago");
    public static final RegistryHandle<FieldEffectDataRegistration> INTEGRAL_HASTE = register("differential_integral_haste");
    public static final RegistryHandle<FieldEffectDataRegistration> EYEBROW = register("eyebrow_passive_marker");
    public static final RegistryHandle<FieldEffectDataRegistration> GROUPS = register("letter_group_marker");



    private static RegistryHandle<FieldEffectDataRegistration> register(String name) {
        return FIELD_EFFECTS.register(name, () -> new FieldEffectDataRegistration(ExpansionMod.ofId("field_effects/" + name + ".json")));
    }
}

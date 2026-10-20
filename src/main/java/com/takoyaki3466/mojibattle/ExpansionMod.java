package com.takoyaki3466.mojibattle;

import com.takoyaki3466.mojibattle.init.*;
import jp.takoa.mojibattle.event.RegistrationBus;
import jp.takoa.mojibattle.mod.GameMod;
import jp.takoa.mojibattle.mod.ModContext;
import jp.takoa.mojibattle.mod.ModEntry;
import jp.takoa.mojibattle.resource.ResourceId;


@ModEntry(ExpansionMod.MOD_ID)
public final class ExpansionMod implements GameMod {
    public static final String MOD_ID = "expansion_mod";

    public static ResourceId ofId(String path) {
        return new ResourceId(MOD_ID, path);
    }

    @Override
    public void register(ModContext context) {
        RegistrationBus bus = context.registrationBus();

        SkillsInit.SKILLS.attach(bus);
        UltimatesInit.ULTIMATES.attach(bus);
        PassivesInit.PASSIVES.attach(bus);

        SupportSkillsInit.SUPPORT_SKILLS.attach(bus);
        SupportUltimatesInit.SUPPORT_ULTIMATES.attach(bus);
        SupportPassivesInit.SUPPORT_PASSIVES.attach(bus);

        FieldEffectsInit.FIELD_EFFECTS.attach(bus);

        MainCharactersInit.MAIN_CHARACTERS.attach(bus);
        SupportCharactersInit.SUPPORT_CHARACTERS.attach(bus);
    }

}

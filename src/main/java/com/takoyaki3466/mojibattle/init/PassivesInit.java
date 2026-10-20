package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.gold.TimeIsMoneyPassive;
import com.takoyaki3466.mojibattle.sigma.EndlessCalculationPassive;
import com.takoyaki3466.mojibattle.tea.ElegantTeaPartyPassive;
import com.takoyaki3466.mojibattle.tree.ExpandingForestMazePassive;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.PassiveImpl;

public class PassivesInit {
    public static final MojiBattleRegister.Passive PASSIVES = MojiBattleRegister.createPassive(ExpansionMod.MOD_ID);

    public static final RegistryHandle<PassiveImpl> TREE = ConfigRegistration.passive(PASSIVES, ExpansionMod.MOD_ID, "expanding_forest_maze", ExpandingForestMazePassive.Config.class, ExpandingForestMazePassive::new);
    public static final RegistryHandle<PassiveImpl> GOLD = ConfigRegistration.passive(PASSIVES, ExpansionMod.MOD_ID, "time_is_money", TimeIsMoneyPassive.Config.class, TimeIsMoneyPassive::new);
    public static final RegistryHandle<PassiveImpl> TEA = ConfigRegistration.passive(PASSIVES, ExpansionMod.MOD_ID, "elegant_tea_party", ElegantTeaPartyPassive.Config.class, ElegantTeaPartyPassive::new);
    public static final RegistryHandle<PassiveImpl> SIGMA = ConfigRegistration.passive(PASSIVES, ExpansionMod.MOD_ID, "endless_calculation", EndlessCalculationPassive.Config.class, EndlessCalculationPassive::new);
}

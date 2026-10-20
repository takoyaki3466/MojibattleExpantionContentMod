package com.takoyaki3466.mojibattle.init;

import com.takoyaki3466.mojibattle.ExpansionMod;
import com.takoyaki3466.mojibattle.config.ConfigRegistration;
import com.takoyaki3466.mojibattle.gold.MoneyMakingExplosionUltimate;
import com.takoyaki3466.mojibattle.sigma.ConvergingSummationUltimate;
import com.takoyaki3466.mojibattle.tea.TeaStyleUltimate;
import com.takoyaki3466.mojibattle.tree.ForestUniverseUltimate;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.UltimateImpl;

public class UltimatesInit {
    public static final MojiBattleRegister.Ultimate ULTIMATES = MojiBattleRegister.createUltimate(ExpansionMod.MOD_ID);

    public static final RegistryHandle<UltimateImpl> TREE = ConfigRegistration.ultimate(ULTIMATES, ExpansionMod.MOD_ID, "forest_universe", ForestUniverseUltimate.Config.class, ForestUniverseUltimate::new);
    public static final RegistryHandle<UltimateImpl> GOLD = ConfigRegistration.ultimate(ULTIMATES, ExpansionMod.MOD_ID, "money_making_explosion", MoneyMakingExplosionUltimate.Config.class, MoneyMakingExplosionUltimate::new);
    public static final RegistryHandle<UltimateImpl> TEA = ConfigRegistration.ultimate(ULTIMATES, ExpansionMod.MOD_ID, "tea_style", TeaStyleUltimate.Config.class, TeaStyleUltimate::new);
    public static final RegistryHandle<UltimateImpl> SIGMA = ConfigRegistration.ultimate(ULTIMATES, ExpansionMod.MOD_ID, "converging_summation", ConvergingSummationUltimate.Config.class, ConvergingSummationUltimate::new);
}

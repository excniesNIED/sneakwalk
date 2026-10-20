package org.hxcn.excnies.sneakwalk;

import net.minecraftforge.fml.common.Mod;

@Mod(SneakWalkForge.MOD_ID)
public final class SneakWalkForge {
    public static final String MOD_ID = "sneakwalk";

    public SneakWalkForge() {
        SneakWalkConfig.ensureLoaded();
    }
}

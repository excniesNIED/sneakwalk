package org.hxcn.excnies.sneakwalk;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(SneakWalkNeoForge.MOD_ID)
public final class SneakWalkNeoForge {
    public static final String MOD_ID = "sneakwalk";

    public SneakWalkNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        SneakWalkConfig.ensureLoaded();
    }
}

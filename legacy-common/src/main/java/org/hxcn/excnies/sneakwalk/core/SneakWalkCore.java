package org.hxcn.excnies.sneakwalk.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

/** Legacy Mixin bootstrap for 1.8.9 / 1.12.2 LaunchWrapper. */
@IFMLLoadingPlugin.Name("SneakWalk")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions({"org.hxcn.excnies.sneakwalk"})
public class SneakWalkCore implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.sneakwalk.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

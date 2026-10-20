package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Legacy crouch/stand camera for modern versions. */
@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private float eyeHeight;
    @Shadow private float eyeHeightOld;

    @Inject(method = "setup", at = @At("HEAD"))
    private void sneakwalk$legacySneakCamera(BlockGetter level, Entity entity, boolean detached,
                                             boolean mirrored, float partialTick, CallbackInfo ci) {
        if (!SneakWalkConfig.shouldUseLegacySneakCamera()) {
            return;
        }
        if (entity != Minecraft.getInstance().player) {
            return;
        }
        if (SneakWalkConfig.sneakCamera == SneakWalkConfig.SneakCamera.LEGACY18) {
            float target = entity.isCrouching() ? 1.54F : 1.62F;
            this.eyeHeight = target;
            this.eyeHeightOld = target;
        } else if (SneakWalkConfig.sneakCamera == SneakWalkConfig.SneakCamera.SNAP) {
            float target = entity.getEyeHeight();
            this.eyeHeight = target;
            this.eyeHeightOld = target;
        }
    }
}

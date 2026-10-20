package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.hxcn.excnies.sneakwalk.SneakWalkPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Render-state based HumanoidModel versions: 1.21.4 and 26.x.
 * Targets HumanoidModel so it also works when PlayerModel inherits setupAnim.
 */
@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModelState {
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void sneakwalk$apply(HumanoidRenderState state, CallbackInfo ci) {
        if (!(this instanceof PlayerModel)) {
            return;
        }
        PlayerModel<?> model = (PlayerModel<?>) (Object) this;
        boolean crouching = state.isCrouching;

        if (SneakWalkConfig.shouldApplyLegacySneakPose(crouching)) {
            SneakWalkPose.applyLegacySneakPose(model);
        }
        if (SneakWalkConfig.shouldApply(crouching)) {
            SneakWalkPose.applyCrouchWalk(model, state.walkAnimationPos, state.walkAnimationSpeed);
        }
    }
}

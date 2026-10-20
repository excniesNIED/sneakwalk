package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.hxcn.excnies.sneakwalk.SneakWalkPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Entity-based PlayerModel versions: 1.18.2, 1.20.1, 1.20.4, 1.21.1.
 */
@Mixin(PlayerModel.class)
public abstract class MixinPlayerModelEntity {
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void sneakwalk$apply(LivingEntity entity, float limbSwing, float limbSwingAmount,
                                 float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!(entity instanceof Player)) {
            return;
        }
        PlayerModel<?> model = (PlayerModel<?>) (Object) this;
        boolean crouching = entity.isCrouching();

        if (SneakWalkConfig.shouldApplyLegacySneakPose(crouching)) {
            SneakWalkPose.applyLegacySneakPose(model);
        }
        if (SneakWalkConfig.shouldApply(crouching)) {
            SneakWalkPose.applyCrouchWalk(model, limbSwing, limbSwingAmount);
        }
    }
}

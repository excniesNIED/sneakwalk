package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.hxcn.excnies.sneakwalk.SneakWalkMath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.8.9 / 1.12.2 ModelBiped crouch-walk override. */
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped {
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightArm;

    @Inject(method = "setRotationAngles", at = @At("TAIL"))
    private void sneakwalk$apply(float limbSwing, float limbSwingAmount, float ageInTicks,
                                 float netHeadYaw, float headPitch, float scaleFactor,
                                 Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        boolean crouching = ((EntityPlayer) entity).isSneaking();
        if (!SneakWalkConfig.shouldApply(crouching)) {
            return;
        }
        float[] pose = SneakWalkMath.pose(SneakWalkConfig.mode == SneakWalkConfig.Mode.BEDROCK,
                limbSwing, limbSwingAmount, SneakWalkConfig.swapSides);
        if (SneakWalkConfig.affectLegs) {
            this.bipedLeftLeg.rotateAngleX = pose[0];
            this.bipedRightLeg.rotateAngleX = pose[1];
        }
        if (SneakWalkConfig.affectArms) {
            this.bipedLeftArm.rotateAngleX = pose[2];
            this.bipedRightArm.rotateAngleX = pose[3];
        }
    }
}

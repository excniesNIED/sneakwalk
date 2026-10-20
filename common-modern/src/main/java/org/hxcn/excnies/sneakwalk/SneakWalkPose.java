package org.hxcn.excnies.sneakwalk;

import net.minecraft.client.model.PlayerModel;

/** Modern (1.18.2+) model mutations shared by entity- and state-based mixins. */
public final class SneakWalkPose {
    private SneakWalkPose() {
    }

    public static void applyCrouchWalk(PlayerModel<?> model, float limbSwing, float amount) {
        float[] pose = SneakWalkMath.pose(SneakWalkConfig.mode == SneakWalkConfig.Mode.BEDROCK,
                limbSwing, amount, SneakWalkConfig.swapSides);
        if (SneakWalkConfig.affectLegs) {
            model.leftLeg.xRot = pose[0];
            model.rightLeg.xRot = pose[1];
        }
        if (SneakWalkConfig.affectArms) {
            model.leftArm.xRot = pose[2];
            model.rightArm.xRot = pose[3];
        }
    }

    /**
     * 1.8 ModelBiped sneak pose. Modern model pivots differ; these are the
     * absolute 1.8 rotation-point values. Config-gated so users can compare
     * without being forced into the old body pose.
     */
    public static void applyLegacySneakPose(PlayerModel<?> model) {
        model.body.xRot = 0.5F;
        model.body.y = 0.0F;
        model.head.y = 1.0F;
        model.leftArm.y = 12.0F;
        model.rightArm.y = 12.0F;
        model.leftArm.z = 0.0F;
        model.rightArm.z = 0.0F;
        model.leftLeg.y = 9.0F;
        model.rightLeg.y = 9.0F;
        model.leftLeg.z = 4.0F;
        model.rightLeg.z = 4.0F;
    }
}

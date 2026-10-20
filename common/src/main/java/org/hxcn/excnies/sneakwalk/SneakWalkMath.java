package org.hxcn.excnies.sneakwalk;

/**
 * Pure math for the two observed crouch-walk standards.
 *
 * <p>GermMod (HuaYuTing 1.8.9) custom bone formulas, recovered from
 * {@code com.germmc!.0O00OOO0O00O0OOO}:
 * <pre>
 *   people_left_leg  x = cos(limbSwing * 0.6662)          * 1.05 * limbSwingAmount
 *   people_right_leg x = cos(limbSwing * 0.6662 + PI)     * 1.05 * limbSwingAmount
 *   people_right_arm x = cos(limbSwing * 0.6662)          * 0.75 * limbSwingAmount
 *   people_left_arm  x = cos(limbSwing * 0.6662 + PI)     * 0.75 * limbSwingAmount
 * </pre>
 *
 * <p>The custom model names are mirrored relative to the vanilla entity's
 * left/right, so the vanilla limbs below keep the vanilla phase:
 * right leg / left arm use cos(0), left leg / right arm use cos(PI).
 */
public final class SneakWalkMath {
    public static final float PI = (float) Math.PI;
    public static final float DEG = PI / 180.0F;

    /** GermMod custom-bone swing amplitudes. */
    public static final float GERM_LEG = 1.05F;
    public static final float GERM_ARM = 0.75F;

    /** Standard Bedrock player.animation.json sneak base angles. */
    public static final float BEDROCK_SNEAK_LEG = -28.0F * DEG;
    public static final float BEDROCK_SNEAK_ARM = -5.7F * DEG;
    /** movement.legs / movement.arms swing amplitudes. */
    public static final float BEDROCK_LEG_SWING = 1.4F;
    public static final float BEDROCK_ARM_SWING = 1.0F;

    private SneakWalkMath() {
    }

    public static float cosSwing(float limbSwing, float limbSwingAmount) {
        return (float) Math.cos(limbSwing * 0.6662F) * limbSwingAmount;
    }

    /** Vanilla-phase right leg for the GermMod preset. */
    public static float germRightLeg(float limbSwing, float amount) {
        return cosSwing(limbSwing, amount) * GERM_LEG;
    }

    /** Vanilla-phase left leg for the GermMod preset. */
    public static float germLeftLeg(float limbSwing, float amount) {
        return -cosSwing(limbSwing, amount) * GERM_LEG;
    }

    /** Vanilla-phase left arm for the GermMod preset. */
    public static float germLeftArm(float limbSwing, float amount) {
        return cosSwing(limbSwing, amount) * GERM_ARM;
    }

    /** Vanilla-phase right arm for the GermMod preset. */
    public static float germRightArm(float limbSwing, float amount) {
        return -cosSwing(limbSwing, amount) * GERM_ARM;
    }

    public static float bedrockRightLeg(float limbSwing, float amount) {
        return BEDROCK_SNEAK_LEG + cosSwing(limbSwing, amount) * BEDROCK_LEG_SWING;
    }

    public static float bedrockLeftLeg(float limbSwing, float amount) {
        return BEDROCK_SNEAK_LEG - cosSwing(limbSwing, amount) * BEDROCK_LEG_SWING;
    }

    public static float bedrockLeftArm(float limbSwing, float amount) {
        return BEDROCK_SNEAK_ARM + cosSwing(limbSwing, amount) * BEDROCK_ARM_SWING;
    }

    public static float bedrockRightArm(float limbSwing, float amount) {
        return BEDROCK_SNEAK_ARM - cosSwing(limbSwing, amount) * BEDROCK_ARM_SWING;
    }

    /**
     * Returns {leftLeg, rightLeg, leftArm, rightArm} in vanilla model naming.
     * {@code swap} mirrors the assignments for pack authors who find the
     * custom model's left/right convention reversed on their model.
     */
    public static float[] pose(boolean bedrock, float limbSwing, float amount, boolean swap) {
        float leftLeg;
        float rightLeg;
        float leftArm;
        float rightArm;
        if (bedrock) {
            leftLeg = bedrockLeftLeg(limbSwing, amount);
            rightLeg = bedrockRightLeg(limbSwing, amount);
            leftArm = bedrockLeftArm(limbSwing, amount);
            rightArm = bedrockRightArm(limbSwing, amount);
        } else {
            leftLeg = germLeftLeg(limbSwing, amount);
            rightLeg = germRightLeg(limbSwing, amount);
            leftArm = germLeftArm(limbSwing, amount);
            rightArm = germRightArm(limbSwing, amount);
        }
        if (swap) {
            float t = leftLeg;
            leftLeg = rightLeg;
            rightLeg = t;
            t = leftArm;
            leftArm = rightArm;
            rightArm = t;
        }
        return new float[]{leftLeg, rightLeg, leftArm, rightArm};
    }
}

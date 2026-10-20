package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.8 arm swing duration (6 ticks, modified by haste/mining fatigue). */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntitySwing {
    @Inject(method = "getCurrentSwingDuration", at = @At("HEAD"), cancellable = true)
    private void sneakwalk$legacySwing(CallbackInfoReturnable<Integer> cir) {
        if (!SneakWalkConfig.shouldUseLegacySwing()) {
            return;
        }
        if ((Object) this != Minecraft.getInstance().player) {
            return;
        }
        LivingEntity self = (LivingEntity) (Object) this;
        int duration = 6;
        MobEffectInstance haste = self.getEffect(MobEffects.DIG_SPEED);
        if (haste != null) {
            duration -= 1 + haste.getAmplifier();
        } else {
            MobEffectInstance fatigue = self.getEffect(MobEffects.DIG_SLOWDOWN);
            if (fatigue != null) {
                duration += 1 + 2 * fatigue.getAmplifier();
            }
        }
        cir.setReturnValue(duration);
    }
}

package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.8 sword blocking for 1.18.2-1.20.4 (UseAnim era). */
@Mixin(Item.class)
public abstract class MixinItemSwordBlockOld {
    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void sneakwalk$blockAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (!SneakWalkConfig.shouldUseLegacySwordBlock()) {
            return;
        }
        if ((Object) this instanceof SwordItem) {
            cir.setReturnValue(UseAnim.BLOCK);
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void sneakwalk$blockDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!SneakWalkConfig.shouldUseLegacySwordBlock()) {
            return;
        }
        if ((Object) this instanceof SwordItem) {
            cir.setReturnValue(72000);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void sneakwalk$blockUse(Level level, Player player, InteractionHand hand,
                                    CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!SneakWalkConfig.shouldUseLegacySwordBlock()) {
            return;
        }
        if ((Object) this instanceof SwordItem) {
            cir.setReturnValue(InteractionResultHolder.consume(player.getItemInHand(hand)));
        }
    }
}

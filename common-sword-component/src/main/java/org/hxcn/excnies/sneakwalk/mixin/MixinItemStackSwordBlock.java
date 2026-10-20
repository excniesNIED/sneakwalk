package org.hxcn.excnies.sneakwalk.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.Consumable;
import org.hxcn.excnies.sneakwalk.SneakWalkConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.8 sword blocking for 1.20.5+ (DataComponents / Consumable era). */
@Mixin(ItemStack.class)
public abstract class MixinItemStackSwordBlock {
    private static final Consumable SNEAKWALK_BLOCK = Consumable.builder()
            .consumeSeconds(1000000.0F)
            .animation(ItemUseAnimation.BLOCK)
            .hasConsumeParticles(false)
            .build();

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private <T> void sneakwalk$blockConsumable(DataComponentType<T> type, CallbackInfoReturnable<T> cir) {
        if (!SneakWalkConfig.shouldUseLegacySwordBlock()) {
            return;
        }
        if (type != DataComponents.CONSUMABLE) {
            return;
        }
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof SwordItem) {
            cir.setReturnValue((T) SNEAKWALK_BLOCK);
        }
    }
}

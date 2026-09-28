package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import vplus.combat.Grip;

@Mixin(Item.class)
public abstract class ItemOffhandMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void vplus$use(net.minecraft.world.level.Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (hand == InteractionHand.MAIN_HAND && vplus.affix.Techniques.tryUse(player, player.getItemInHand(hand))) {
			cir.setReturnValue(InteractionResult.CONSUME);
			return;
		}
		if (hand == InteractionHand.OFF_HAND && Grip.of(player).suppressOffhand) {
			cir.setReturnValue(InteractionResult.FAIL);
		}
	}

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void vplus$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (context.getHand() == InteractionHand.OFF_HAND && context.getPlayer() != null && Grip.of(context.getPlayer()).suppressOffhand) {
			cir.setReturnValue(InteractionResult.FAIL);
		}
	}
}

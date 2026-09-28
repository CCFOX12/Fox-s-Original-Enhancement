package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import vplus.loadout.StrapLight;

@Mixin(EntityRenderer.class)
public abstract class EntityLightMixin {
	@Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
	private void vplus$light(Entity entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!(entity instanceof Player player)) {
			return;
		}
		int extra = StrapLight.level(player);
		if (extra > cir.getReturnValue()) {
			cir.setReturnValue(extra);
		}
	}
}

package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import vplus.cosmetic.StandLoadout;

@Mixin(Entity.class)
public abstract class ArmorStandMixin {
	@Inject(method = "remove", at = @At("HEAD"))
	private void vplus$dropCosmetics(Entity.RemovalReason reason, CallbackInfo ci) {
		if ((Object) this instanceof ArmorStand stand && stand.level() instanceof ServerLevel level) {
			StandLoadout.drop(stand, level);
		}
	}
}

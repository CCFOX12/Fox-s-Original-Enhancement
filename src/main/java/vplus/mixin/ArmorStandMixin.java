package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import vplus.cosmetic.StandLoadout;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin {
	@Inject(method = "kill", at = @At("HEAD"))
	private void vplus$dropCosmetics(ServerLevel level, CallbackInfo ci) {
		StandLoadout.drop((ArmorStand) (Object) this, level);
	}
}

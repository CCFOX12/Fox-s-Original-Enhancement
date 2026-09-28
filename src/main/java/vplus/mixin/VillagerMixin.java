package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.npc.villager.Villager;
import vplus.game.NitwitBarter;

@Mixin(Villager.class)
public abstract class VillagerMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void vplus$barter(CallbackInfo ci) {
		NitwitBarter.tick((Villager) (Object) this);
	}
}

package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import vplus.loadout.Loadout;
import vplus.loadout.LoadoutRules;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLoadoutMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void vplus$tick(CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayer player) {
			LoadoutRules.tick(player);
			vplus.game.LoomJobs.tick(player);
		} else if ((Object) this instanceof net.minecraft.world.entity.npc.villager.Villager villager) {
			vplus.game.NitwitBarter.tick(villager);
		}
	}

	@Inject(method = "dropEquipment", at = @At("HEAD"))
	private void vplus$death(ServerLevel level, CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayer player) {
			Loadout.get(player).dropAll(player);
		}
	}
}

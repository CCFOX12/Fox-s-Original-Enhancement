package vplus.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import vplus.loadout.Loadout;
import vplus.loadout.LoadoutRules;
import vplus.loadout.LoadoutSlots;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponMixin {
	@Inject(method = "getHeldProjectile", at = @At("HEAD"), cancellable = true)
	private static void vplus$quiver(LivingEntity entity, Predicate<ItemStack> predicate, CallbackInfoReturnable<ItemStack> cir) {
		if (!(entity instanceof Player player) || !LoadoutRules.active(player, LoadoutSlots.QUIVER_START)) {
			return;
		}
		boolean bow = player.getMainHandItem().is(Items.BOW);
		Loadout loadout = Loadout.get(player);
		for (int slot = LoadoutSlots.QUIVER_START; slot < LoadoutSlots.POTION_START; slot++) {
			ItemStack stack = loadout.get(slot);
			if (stack.isEmpty()) {
				continue;
			}
			if (bow && stack.is(Items.FIREWORK_ROCKET)) {
				continue;
			}
			if (predicate.test(stack)) {
				cir.setReturnValue(stack);
				return;
			}
		}
	}
}

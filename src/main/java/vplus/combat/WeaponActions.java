package vplus.combat;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vplus.entity.ThrownGear;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public final class WeaponActions {
	private WeaponActions() {
	}

	public static InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Profile profile = Profiles.of(stack.getItem());
		if (profile == null || !profile.has(Profile.Trait.THROWN) || stack.isEmpty()) {
			return null;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		ThrownGear gear = new ThrownGear(level, player, stack.copyWithCount(1));
		gear.shootFrom(player);
		level.addFreshEntity(gear);
		if ("throwing_knife".equals(profile.id)) {
			stack.shrink(1);
		} else {
			player.setItemInHand(hand, ItemStack.EMPTY);
		}
		return InteractionResult.CONSUME;
	}
}

package vplus.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import vplus.combat.WeaponActions;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public class VPlusWeaponItem extends Item {
	public VPlusWeaponItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		InteractionResult result = WeaponActions.use(level, player, hand);
		if (result != null) {
			return result;
		}
		Profile profile = Profiles.of(this);
		if (profile != null && profile.shield != null) {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
		return super.use(level, player, hand);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		Profile profile = Profiles.of(stack.getItem());
		if (profile != null && profile.shield != null) {
			return 72000;
		}
		return super.getUseDuration(stack, entity);
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		Profile profile = Profiles.of(stack.getItem());
		if (profile != null && profile.shield != null) {
			return ItemUseAnimation.BLOCK;
		}
		return super.getUseAnimation(stack);
	}
}

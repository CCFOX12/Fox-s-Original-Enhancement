package vplus.cosmetic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public final class CosmeticDispense extends DefaultDispenseItemBehavior {
	public static final CosmeticDispense BEHAVIOR = new CosmeticDispense();

	private CosmeticDispense() {
	}

	public static void register() {
		BEHAVIOR.toString();
	}

	public static void register(Item item) {
		DispenserBlock.registerBehavior(item, BEHAVIOR);
	}

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		BlockPos front = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		AABB box = new AABB(front.getX(), front.getY(), front.getZ(), front.getX() + 1.0, front.getY() + 1.0, front.getZ() + 1.0);
		LivingEntity target = null;
		for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity instanceof Player || entity instanceof ArmorStand)) {
			target = entity;
			break;
		}
		if (target instanceof Player player && CosmeticWear.equip(player, stack)) {
			return stack;
		}
		if (target instanceof ArmorStand stand && CosmeticWear.equip(stand, stack)) {
			return stack;
		}
		return super.execute(source, stack);
	}
}

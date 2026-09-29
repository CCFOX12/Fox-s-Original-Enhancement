package vplus.cosmetic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vplus.item.ModComponents;
import vplus.loadout.Loadout;
import vplus.loadout.LoadoutSlots;

public final class CosmeticFollow {
	private CosmeticFollow() {
	}

	public static double factor(Mob mob) {
		Player subject = mob.getTarget() instanceof Player targeted ? targeted : mob.level().getNearestPlayer(mob, 48.0);
		if (subject == null) {
			return 1.0;
		}
		String mobId = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).getPath();
		int pieces = 0;
		Loadout loadout = Loadout.get(subject);
		for (int slot = LoadoutSlots.DISPLAY_HEAD; slot <= LoadoutSlots.UTILITY_FEET; slot++) {
			pieces += piecesOf(loadout.get(slot), mobId);
		}
		for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
			pieces += piecesOf(subject.getItemBySlot(slot), mobId);
		}
		if (pieces <= 0) {
			return 1.0;
		}
		return Math.pow(0.85, pieces);
	}

	private static int piecesOf(ItemStack stack, String mobId) {
		if (!mobId.equals(stack.get(ModComponents.GEMS))) {
			return 0;
		}
		String count = stack.get(ModComponents.AFFIX);
		if (count == null) {
			return 1;
		}
		try {
			return Integer.parseInt(count);
		} catch (NumberFormatException exception) {
			return 1;
		}
	}
}

package vplus.loadout;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vplus.item.ModGear;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;
import vplus.weapon.WeaponSize;

public final class LoadoutRules {
	private LoadoutRules() {
	}

	public static boolean active(Player player, int slot) {
		Loadout loadout = Loadout.get(player);
		if (slot >= LoadoutSlots.SHOULDER_LEFT && slot <= LoadoutSlots.SHOULDER_RIGHT) {
			return ModGear.kind(loadout.get(LoadoutSlots.SHOULDER_STRAP), "gear_shoulder");
		}
		if (slot == LoadoutSlots.BELT_LEFT || slot == LoadoutSlots.BELT_RIGHT) {
			return ModGear.kind(loadout.get(LoadoutSlots.BELT_STRAP), "gear_belt");
		}
		if (slot == LoadoutSlots.BACK) {
			return ModGear.kind(loadout.get(LoadoutSlots.BACK_STRAP), "gear_back");
		}
		if (LoadoutSlots.isQuiver(slot)) {
			return carried(player, ModGear.QUIVER);
		}
		if (LoadoutSlots.isPotion(slot)) {
			return carried(player, ModGear.POTION_BAG);
		}
		return true;
	}

	public static boolean mayPlace(int slot, ItemStack stack) {
		if (stack.isEmpty()) {
			return true;
		}
		String kind = stack.get(vplus.item.ModComponents.SLOT_KIND);
		if (slot >= LoadoutSlots.DISPLAY_HEAD && slot <= LoadoutSlots.UTILITY_FEET) {
			return expectedKind(slot).equals(kind);
		}
		if (slot == LoadoutSlots.PAULDRON) {
			return "gear_pauldron".equals(kind);
		}
		if (slot == LoadoutSlots.LINING) {
			return "gear_lining".equals(kind);
		}
		if (slot == LoadoutSlots.SHOULDER_STRAP) {
			return "gear_shoulder".equals(kind);
		}
		if (slot == LoadoutSlots.BELT_STRAP) {
			return "gear_belt".equals(kind);
		}
		if (slot == LoadoutSlots.BACK_STRAP) {
			return "gear_back".equals(kind);
		}
		if (LoadoutSlots.isQuiver(slot)) {
			return arrowKind(slot, stack);
		}
		if (LoadoutSlots.isPotion(slot)) {
			return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
		}
		return fitsExtension(slot, stack);
	}

	public static void tick(Player player) {
		if (player.level().isClientSide()) {
			return;
		}
		Loadout loadout = Loadout.get(player);
		if (!ModGear.kind(loadout.get(LoadoutSlots.SHOULDER_STRAP), "gear_shoulder")) {
			loadout = dump(player, loadout, LoadoutSlots.SHOULDER_LEFT, LoadoutSlots.SHOULDER_RIGHT);
		}
		if (!ModGear.kind(loadout.get(LoadoutSlots.BELT_STRAP), "gear_belt")) {
			loadout = dump(player, loadout, LoadoutSlots.BELT_LEFT, LoadoutSlots.BELT_RIGHT);
		}
		if (!ModGear.kind(loadout.get(LoadoutSlots.BACK_STRAP), "gear_back")) {
			loadout = dump(player, loadout, LoadoutSlots.BACK, LoadoutSlots.BACK);
		}
		if (!carried(player, ModGear.QUIVER)) {
			loadout = dump(player, loadout, LoadoutSlots.QUIVER_START, LoadoutSlots.POTION_START - 1);
		}
		if (!carried(player, ModGear.POTION_BAG)) {
			loadout = dump(player, loadout, LoadoutSlots.POTION_START, Loadout.SIZE - 1);
		}
		Loadout.set(player, loadout);
	}

	public static void swap(Player player, int slot) {
		if (!active(player, slot) || !LoadoutSlots.isExtension(slot)) {
			return;
		}
		player.stopUsingItem();
		ItemStack hand = player.getMainHandItem().copy();
		ItemStack stored = Loadout.get(player).get(slot).copy();
		if (!hand.isEmpty() && !mayPlace(slot, hand)) {
			return;
		}
		Loadout.set(player, Loadout.get(player).with(slot, hand));
		player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, stored);
	}

	public static int light(Player player) {
		Loadout loadout = Loadout.get(player);
		int light = 0;
		for (int slot = LoadoutSlots.SHOULDER_LEFT; slot <= LoadoutSlots.BACK; slot++) {
			if (!active(player, slot)) {
				continue;
			}
			light = Math.max(light, lantern(loadout.get(slot)));
		}
		light = Math.max(light, lantern(player.getMainHandItem()));
		if (!vplus.combat.Grip.of(player).suppressOffhand) {
			light = Math.max(light, lantern(player.getOffhandItem()));
		}
		return light;
	}

	private static int lantern(ItemStack stack) {
		String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		if (!path.contains("lantern")) {
			return 0;
		}
		return path.contains("soul") ? 10 : 15;
	}

	private static Loadout dump(Player player, Loadout loadout, int from, int to) {
		for (int slot = from; slot <= to; slot++) {
			ItemStack stack = loadout.get(slot);
			if (stack.isEmpty()) {
				continue;
			}
			ItemStack copy = stack.copy();
			loadout = loadout.with(slot, ItemStack.EMPTY);
			if (!player.getInventory().add(copy)) {
				player.spawnAtLocation((net.minecraft.server.level.ServerLevel) player.level(), copy);
			}
		}
		return loadout;
	}

	private static boolean carried(Player player, net.minecraft.world.item.Item item) {
		if (item == null) {
			return false;
		}
		int size = Math.min(36, player.getInventory().getContainerSize());
		for (int i = 0; i < size; i++) {
			if (player.getInventory().getItem(i).is(item)) {
				return true;
			}
		}
		return false;
	}

	private static boolean fitsExtension(int slot, ItemStack stack) {
		if (lantern(stack) > 0) {
			return true;
		}
		Profile profile = Profiles.of(stack.getItem());
		if (profile == null) {
			return false;
		}
		WeaponSize size = profile.size;
		if (slot == LoadoutSlots.SHOULDER_LEFT || slot == LoadoutSlots.SHOULDER_RIGHT) {
			return size == WeaponSize.SMALL;
		}
		if (slot == LoadoutSlots.BELT_LEFT || slot == LoadoutSlots.BELT_RIGHT) {
			return size == WeaponSize.MEDIUM;
		}
		return size == WeaponSize.HEAVY;
	}

	private static boolean arrowKind(int slot, ItemStack stack) {
		int index = slot - LoadoutSlots.QUIVER_START;
		return switch (index) {
			case 0 -> stack.is(Items.ARROW);
			case 1 -> stack.is(Items.TIPPED_ARROW);
			case 2 -> stack.is(Items.SPECTRAL_ARROW);
			case 3 -> stack.is(Items.FIREWORK_ROCKET);
			default -> false;
		};
	}

	private static String expectedKind(int slot) {
		return switch (slot) {
			case 0 -> "display_head";
			case 1 -> "display_coat";
			case 2 -> "display_legs";
			case 3 -> "display_feet";
			case 4 -> "utility_head";
			case 5 -> "utility_coat";
			case 6 -> "utility_legs";
			case 7 -> "utility_feet";
			default -> "";
		};
	}
}

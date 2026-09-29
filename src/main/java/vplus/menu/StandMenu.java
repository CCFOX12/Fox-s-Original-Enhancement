package vplus.menu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import vplus.cosmetic.CosmeticWear;
import vplus.cosmetic.StandLoadout;
import vplus.item.ModComponents;
import vplus.loadout.Loadout;

public class StandMenu extends AbstractContainerMenu {
	private static final EquipmentSlot[] ARMOR = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
	private static final String[] DISPLAY = { "display_head", "display_coat", "display_legs", "display_feet" };
	private static final String[] UTILITY = { "utility_head", "utility_coat", "utility_legs", "utility_feet" };

	private final ArmorStand stand;

	public StandMenu(int id, Inventory inventory, ArmorStand stand) {
		super(ModMenus.STAND, id);
		this.stand = stand;
		Container cosmetics = stand == null ? new SimpleContainer(8) : new CosmeticContainer(stand);
		Container armor = stand == null ? new SimpleContainer(4) : new ArmorContainer(stand);
		for (int row = 0; row < 4; row++) {
			this.addSlot(new KindSlot(cosmetics, row, 44, 28 + row * 18, DISPLAY[row]));
			this.addSlot(new KindSlot(cosmetics, 4 + row, 80, 28 + row * 18, UTILITY[row]));
			this.addSlot(new ArmorSlot(armor, row, 116, 28 + row * 18, ARMOR[row]));
		}
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 112 + row * 18));
			}
		}
		for (int column = 0; column < 9; column++) {
			this.addSlot(new Slot(inventory, column, 8 + column * 18, 170));
		}
	}

	public static StandMenu fromNetwork(int id, Inventory inventory, StandData data) {
		var entity = inventory.player.level().getEntity(data.entityId());
		return new StandMenu(id, inventory, entity instanceof ArmorStand stand ? stand : null);
	}

	@Override
	public boolean stillValid(Player player) {
		return this.stand != null && this.stand.isAlive() && player.distanceToSqr(this.stand) <= 64.0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}
		if (index < 12) {
			ItemStack stack = slot.getItem().copy();
			this.moveItemStackTo(stack, 12, this.slots.size(), true);
			if (!stack.isEmpty() && !player.level().isClientSide()) {
				player.drop(stack.copy(), false);
				stack = ItemStack.EMPTY;
			}
			slot.set(stack);
			slot.setChanged();
			return ItemStack.EMPTY;
		}
		if (this.stand != null) {
			CosmeticWear.equip(this.stand, slot.getItem(), player);
		}
		return ItemStack.EMPTY;
	}

	private static final class KindSlot extends Slot {
		private final String kind;

		private KindSlot(Container container, int slot, int x, int y, String kind) {
			super(container, slot, x, y);
			this.kind = kind;
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return this.kind.equals(stack.get(ModComponents.SLOT_KIND));
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	private static final class ArmorSlot extends Slot {
		private final EquipmentSlot armor;

		private ArmorSlot(Container container, int slot, int x, int y, EquipmentSlot armor) {
			super(container, slot, x, y);
			this.armor = armor;
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
			return equippable != null && equippable.slot() == this.armor;
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	private static final class CosmeticContainer implements Container {
		private final ArmorStand stand;

		private CosmeticContainer(ArmorStand stand) {
			this.stand = stand;
		}

		@Override
		public int getContainerSize() {
			return 8;
		}

		@Override
		public boolean isEmpty() {
			Loadout loadout = StandLoadout.get(this.stand);
			for (int slot = 0; slot < 8; slot++) {
				if (!loadout.get(slot).isEmpty()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ItemStack getItem(int slot) {
			return StandLoadout.get(this.stand).get(slot);
		}

		@Override
		public ItemStack removeItem(int slot, int amount) {
			ItemStack current = this.getItem(slot).copy();
			if (current.isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack removed = current.split(amount);
			this.setItem(slot, current);
			return removed;
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			ItemStack current = this.getItem(slot).copy();
			this.setItem(slot, ItemStack.EMPTY);
			return current;
		}

		@Override
		public void setItem(int slot, ItemStack stack) {
			StandLoadout.set(this.stand, StandLoadout.get(this.stand).with(slot, stack));
		}

		@Override
		public void setChanged() {
		}

		@Override
		public boolean stillValid(Player player) {
			return true;
		}

		@Override
		public void clearContent() {
			for (int slot = 0; slot < 8; slot++) {
				this.setItem(slot, ItemStack.EMPTY);
			}
		}
	}

	private static final class ArmorContainer implements Container {
		private final ArmorStand stand;

		private ArmorContainer(ArmorStand stand) {
			this.stand = stand;
		}

		@Override
		public int getContainerSize() {
			return 4;
		}

		@Override
		public boolean isEmpty() {
			for (EquipmentSlot slot : ARMOR) {
				if (!this.stand.getItemBySlot(slot).isEmpty()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ItemStack getItem(int slot) {
			return this.stand.getItemBySlot(ARMOR[slot]);
		}

		@Override
		public ItemStack removeItem(int slot, int amount) {
			ItemStack current = this.getItem(slot).copy();
			if (current.isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack removed = current.split(amount);
			this.setItem(slot, current);
			return removed;
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			ItemStack current = this.getItem(slot).copy();
			this.setItem(slot, ItemStack.EMPTY);
			return current;
		}

		@Override
		public void setItem(int slot, ItemStack stack) {
			this.stand.setItemSlot(ARMOR[slot], stack);
		}

		@Override
		public void setChanged() {
		}

		@Override
		public boolean stillValid(Player player) {
			return true;
		}

		@Override
		public void clearContent() {
			for (int slot = 0; slot < 4; slot++) {
				this.setItem(slot, ItemStack.EMPTY);
			}
		}
	}
}

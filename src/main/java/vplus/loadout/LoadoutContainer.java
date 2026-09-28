package vplus.loadout;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class LoadoutContainer implements Container {
	private final Player player;

	public LoadoutContainer(Player player) {
		this.player = player;
	}

	public Player player() {
		return this.player;
	}

	@Override
	public int getContainerSize() {
		return Loadout.SIZE;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < Loadout.SIZE; i++) {
			if (!this.getItem(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return Loadout.get(this.player).get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack current = this.getItem(slot);
		if (current.isEmpty() || amount <= 0) {
			return ItemStack.EMPTY;
		}
		ItemStack split = current.split(amount);
		this.setItem(slot, current);
		return split;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack current = this.getItem(slot);
		this.setItem(slot, ItemStack.EMPTY);
		return current;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		Loadout.set(this.player, Loadout.get(this.player).with(slot, stack));
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return player == this.player;
	}

	@Override
	public void clearContent() {
		Loadout.set(this.player, Loadout.empty());
	}
}

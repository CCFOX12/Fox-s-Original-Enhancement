package vplus.loadout;

import net.minecraft.world.item.ItemStack;

public class LoadoutSlot extends net.minecraft.world.inventory.Slot {
	private final LoadoutContainer loadout;

	public LoadoutSlot(LoadoutContainer container, int index, int x, int y) {
		super(container, index, x, y);
		this.loadout = container;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return LoadoutRules.mayPlace(this.getContainerSlot(), stack);
	}

	@Override
	public boolean isActive() {
		return LoadoutRules.active(this.loadout.player(), this.getContainerSlot());
	}
}

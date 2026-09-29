package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vplus.loadout.LoadoutContainer;
import vplus.loadout.LoadoutSlot;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void vplus$slots(net.minecraft.world.entity.player.Inventory inventory, boolean active, Player player, CallbackInfo ci) {
		MenuSlotInvoker menu = (MenuSlotInvoker) (Object) this;
		LoadoutContainer container = new LoadoutContainer(player);
		int x = 184;
		for (int i = 0; i < 8; i++) {
			menu.vplus$addSlot(new LoadoutSlot(container, i, x, 8 + i * 18));
		}
		menu.vplus$addSlot(new LoadoutSlot(container, 8, 206, 8));
		menu.vplus$addSlot(new LoadoutSlot(container, 9, 206, 26));
		menu.vplus$addSlot(new LoadoutSlot(container, 10, 206, 44));
		menu.vplus$addSlot(new LoadoutSlot(container, 13, 224, 44));
		menu.vplus$addSlot(new LoadoutSlot(container, 14, 242, 44));
		menu.vplus$addSlot(new LoadoutSlot(container, 11, 206, 62));
		menu.vplus$addSlot(new LoadoutSlot(container, 15, 224, 62));
		menu.vplus$addSlot(new LoadoutSlot(container, 16, 242, 62));
		menu.vplus$addSlot(new LoadoutSlot(container, 12, 206, 80));
		menu.vplus$addSlot(new LoadoutSlot(container, 17, 224, 80));
		for (int i = 0; i < 4; i++) {
			menu.vplus$addSlot(new LoadoutSlot(container, 18 + i, 184 + i * 18, 116));
		}
		for (int i = 0; i < 4; i++) {
			menu.vplus$addSlot(new LoadoutSlot(container, 22 + i, 184 + i * 18, 134));
		}
	}

	@Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
	private void vplus$quick(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = ((InventoryMenu) (Object) this).slots.get(index);
		if (index >= 9 && index <= 44 && vplus.cosmetic.CosmeticWear.equip(player, slot.getItem())) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		if (!(slot.container instanceof LoadoutContainer) || slot.getItem().isEmpty()) {
			return;
		}
		ItemStack stack = slot.getItem().copy();
		slot.set(ItemStack.EMPTY);
		if (!player.getInventory().add(stack)) {
			player.drop(stack, false);
		}
		cir.setReturnValue(ItemStack.EMPTY);
	}
}

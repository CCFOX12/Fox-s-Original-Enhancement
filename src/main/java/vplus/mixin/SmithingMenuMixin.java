package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vplus.affix.SmithingCraft;
import vplus.item.ModComponents;
import vplus.item.ModTech;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin {
	@Inject(method = "createResult", at = @At("RETURN"))
	private void vplus$create(CallbackInfo ci) {
		SmithingCraft.apply((SmithingMenu) (Object) this);
	}

	@Inject(method = "onTake", at = @At("HEAD"))
	private void vplus$take(Player player, ItemStack stack, CallbackInfo ci) {
		if (stack.get(ModComponents.TECHNIQUE) == null && stack.get(ModComponents.SOCKETS) == null && stack.get(ModComponents.GEMS) == null) {
			return;
		}
		SmithingMenu menu = (SmithingMenu) (Object) this;
		menu.getSlot(SmithingMenu.TEMPLATE_SLOT).remove(1);
		menu.getSlot(SmithingMenu.BASE_SLOT).remove(1);
		ItemStack addition = menu.getSlot(SmithingMenu.ADDITIONAL_SLOT).getItem();
		if (ModTech.isGem(addition) || addition.is(Items.COPPER_INGOT)) {
			menu.getSlot(SmithingMenu.ADDITIONAL_SLOT).remove(1);
		}
	}
}

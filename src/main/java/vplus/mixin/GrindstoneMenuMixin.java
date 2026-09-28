package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import vplus.affix.SmithingCraft;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {
	@Inject(method = "createResult", at = @At("RETURN"))
	private void vplus$clear(CallbackInfo ci) {
		ItemStack result = ((GrindstoneMenu) (Object) this).getSlot(GrindstoneMenu.RESULT_SLOT).getItem();
		SmithingCraft.clearGrindstone(result);
	}
}

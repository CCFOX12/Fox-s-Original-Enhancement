package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import vplus.client.CosmeticLayerButtons;
import vplus.cosmetic.CosmeticLayer;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryLayerClickMixin {
	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void vplus$layer(MouseButtonEvent event, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
		if (!((Object) this instanceof InventoryScreen) || event.button() != 0) {
			return;
		}
		ContainerScreenAccess access = (ContainerScreenAccess) (Object) this;
		int hit = CosmeticLayerButtons.hit(event.x(), event.y(), access.vplus$leftPos(), access.vplus$topPos());
		if (hit < 0) {
			return;
		}
		var player = Minecraft.getInstance().player;
		if (player != null) {
			boolean armor = hit == 1;
			CosmeticLayer.set(player, armor);
			ClientPlayNetworking.send(new CosmeticLayer.LayerPayload(armor));
		}
		cir.setReturnValue(true);
	}
}

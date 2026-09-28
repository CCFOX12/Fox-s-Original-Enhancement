package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import vplus.loadout.LoadoutContainer;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
	private static final Identifier SLOT = Identifier.withDefaultNamespace("container/slot");

	@Inject(method = "renderBg", at = @At("RETURN"))
	private void vplus$slots(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
		ContainerScreenAccess access = (ContainerScreenAccess) (Object) this;
		InventoryScreen screen = (InventoryScreen) (Object) this;
		int left = access.vplus$leftPos();
		int top = access.vplus$topPos();
		for (Slot slot : screen.getMenu().slots) {
			if (slot.container instanceof LoadoutContainer && slot.isActive()) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, left + slot.x - 1, top + slot.y - 1, 18, 18);
			}
		}
	}
}

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
		var player = net.minecraft.client.Minecraft.getInstance().player;
		boolean armor = player != null && vplus.cosmetic.CosmeticLayer.armor(player);
		drawButton(graphics, left, top, false, !armor);
		drawButton(graphics, left, top, true, armor);
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void vplus$tips(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		ContainerScreenAccess access = (ContainerScreenAccess) (Object) this;
		int hit = vplus.client.CosmeticLayerButtons.hit(mouseX, mouseY, access.vplus$leftPos(), access.vplus$topPos());
		if (hit < 0) {
			return;
		}
		String key = hit == 0 ? "gui.vplus.layer.skin" : "gui.vplus.layer.armor";
		graphics.setTooltipForNextFrame(net.minecraft.client.Minecraft.getInstance().font, net.minecraft.network.chat.Component.translatable(key), mouseX, mouseY);
	}

	private static void drawButton(GuiGraphics graphics, int left, int top, boolean armor, boolean active) {
		int x = left + vplus.client.CosmeticLayerButtons.SKIN_X;
		int y = top + vplus.client.CosmeticLayerButtons.Y + (armor ? vplus.client.CosmeticLayerButtons.GAP : 0);
		int size = vplus.client.CosmeticLayerButtons.SIZE;
		graphics.fill(x, y, x + size, y + size, active ? 0xFFFFFFFF : 0xFF555555);
		graphics.fill(x + 1, y + 1, x + size - 1, y + size - 1, armor ? 0xFF8A8A8A : 0xFF6E4B2A);
	}
}

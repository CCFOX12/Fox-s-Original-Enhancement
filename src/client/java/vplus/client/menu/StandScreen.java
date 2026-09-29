package vplus.client.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import vplus.menu.StandMenu;

public class StandScreen extends AbstractContainerScreen<StandMenu> {
	private static final Identifier SLOT = Identifier.withDefaultNamespace("container/slot");

	public StandScreen(StandMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 194;
		this.inventoryLabelY = 100;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		graphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xFFC6C6C6);
		for (Slot slot : this.menu.slots) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, this.leftPos + slot.x - 1, this.topPos + slot.y - 1, 18, 18);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
		graphics.drawString(this.font, Component.translatable("container.vplus.column.display"), 44, 16, 0x404040, false);
		graphics.drawString(this.font, Component.translatable("container.vplus.column.utility"), 80, 16, 0x404040, false);
		graphics.drawString(this.font, Component.translatable("container.vplus.column.armor"), 116, 16, 0x404040, false);
		graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
	}
}

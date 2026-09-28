package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

@Mixin(AbstractContainerScreen.class)
public interface ContainerScreenAccess {
	@Accessor("leftPos")
	int vplus$leftPos();

	@Accessor("topPos")
	int vplus$topPos();
}

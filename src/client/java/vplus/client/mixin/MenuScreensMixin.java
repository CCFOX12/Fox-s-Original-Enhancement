package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@Mixin(MenuScreens.class)
public interface MenuScreensMixin {
	@Invoker("register")
	static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void vplus$register(MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> constructor) {
		throw new AssertionError();
	}
}

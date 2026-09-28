package vplus.client;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import vplus.net.SwapPayload;

public final class ModKeys {
	public static final KeyMapping SHOULDER = new KeyMapping("key.vplus.shoulder", GLFW.GLFW_KEY_X, KeyMapping.Category.GAMEPLAY);
	public static final KeyMapping BELT = new KeyMapping("key.vplus.belt", GLFW.GLFW_KEY_Z, KeyMapping.Category.GAMEPLAY);
	public static final KeyMapping BACK = new KeyMapping("key.vplus.back", GLFW.GLFW_KEY_C, KeyMapping.Category.GAMEPLAY);

	private ModKeys() {
	}

	public static void register() {
		KeyBindingHelper.registerKeyBinding(SHOULDER);
		KeyBindingHelper.registerKeyBinding(BELT);
		KeyBindingHelper.registerKeyBinding(BACK);
		ClientTickEvents.END_CLIENT_TICK.register(ModKeys::tick);
	}

	private static void tick(Minecraft client) {
		if (client.player == null || client.screen != null) {
			return;
		}
		boolean sneak = client.player.isShiftKeyDown();
		if (SHOULDER.consumeClick()) {
			send(sneak ? 14 : 13);
		}
		if (BELT.consumeClick()) {
			send(sneak ? 16 : 15);
		}
		if (BACK.consumeClick()) {
			send(17);
		}
	}

	private static void send(int slot) {
		ClientPlayNetworking.send(new SwapPayload(slot));
	}
}

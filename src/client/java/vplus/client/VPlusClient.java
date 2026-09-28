package vplus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import vplus.client.ModKeys;
import vplus.VPlusMod;
import vplus.entity.ModEntities;

public final class VPlusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModKeys.register();
		EntityRendererRegistry.register(ModEntities.THROWN, ThrownItemRenderer::new);
		VPlusMod.LOGGER.info("原版增强客户端已加载");
	}
}

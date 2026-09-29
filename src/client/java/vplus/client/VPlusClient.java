package vplus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import vplus.VPlusMod;
import vplus.client.cosmetic.MobSkinFeatureRenderer;
import vplus.client.menu.StandScreen;
import vplus.client.mixin.MenuScreensMixin;
import vplus.entity.ModEntities;
import vplus.menu.ModMenus;

public final class VPlusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModKeys.register();
		EntityRendererRegistry.register(ModEntities.THROWN, ThrownItemRenderer::new);
		MenuScreensMixin.vplus$register(ModMenus.STAND, StandScreen::new);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, helper, context) -> {
			if (entityRenderer instanceof ArmorStandRenderer stand) {
				helper.register(new MobSkinFeatureRenderer<>(stand));
			}
		});
		VPlusMod.LOGGER.info("原版增强客户端已加载");
	}
}

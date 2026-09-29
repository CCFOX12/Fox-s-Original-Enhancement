package vplus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import vplus.VPlusMod;
import vplus.client.cosmetic.MobSkinFeatureRenderer;
import vplus.client.cosmetic.PlayerArmorLayerFeature;
import vplus.client.cosmetic.SkinComposite;
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
			if (entityRenderer instanceof AvatarRenderer<?> avatar) {
				helper.register(new PlayerArmorLayerFeature(avatar));
			} else if (entityRenderer instanceof ArmorStandRenderer stand) {
				helper.register(new MobSkinFeatureRenderer<>(stand));
			}
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> SkinComposite.forget(entity.getId()));
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "skins");
			}

			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				SkinComposite.reload();
			}
		});
		VPlusMod.LOGGER.info("原版增强客户端已加载");
	}
}

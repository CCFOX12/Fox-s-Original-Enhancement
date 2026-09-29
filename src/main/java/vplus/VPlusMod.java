package vplus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vplus.entity.ModEntities;
import vplus.cosmetic.CosmeticItems;
import vplus.game.LoomJobs;
import vplus.game.ModLoot;
import vplus.item.ModComponents;
import vplus.item.ModTech;
import vplus.item.ModGear;
import vplus.item.ModItems;
import vplus.item.ModTabs;
import vplus.item.VanillaRetune;
import vplus.cosmetic.CosmeticWear;
import vplus.cosmetic.StandLoadout;
import vplus.loadout.LoadoutAttachments;
import vplus.menu.ModMenus;
import vplus.net.SwapPayload;
import vplus.weapon.Bench;

public final class VPlusMod implements ModInitializer {
	public static final String MOD_ID = "vplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModComponents.register();
		LoadoutAttachments.register();
		StandLoadout.register();
		ModMenus.register();
		ModEntities.register();
		ModItems.register();
		ModGear.register();
		ModTech.register();
		CosmeticItems.register();
		VanillaRetune.register();
		ModTabs.register();
		SwapPayload.register();
		LoomJobs.register();
		ModLoot.register();
		CosmeticWear.register();
		Bench.log();
		LOGGER.info("原版增强已加载");
	}
}

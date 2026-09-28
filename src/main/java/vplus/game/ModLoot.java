package vplus.game;

import java.util.Optional;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vplus.cosmetic.CosmeticItems;
import vplus.item.ModGear;
import vplus.item.ModTech;

public final class ModLoot {
	private ModLoot() {
	}

	public static void register() {
		LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
			String path = key.identifier().getPath();
			if (path.contains("abandoned_mineshaft")) {
				pool(builder, CosmeticItems.MINER_HAT, 0.15f);
			} else if (path.contains("buried_treasure")) {
				pool(builder, item("pirate_bandana"), 0.30f);
				pool(builder, ModTech.FAN, 0.20f);
			} else if (path.contains("desert_pyramid")) {
				pool(builder, item("desert_scarf"), 0.40f);
			} else if (path.contains("jungle_temple")) {
				pool(builder, item("jungle_mask"), 0.40f);
			} else if (path.contains("pillager_outpost")) {
				pool(builder, item("outpost_cape"), 0.25f);
				pool(builder, ModTech.BASH, 0.15f);
			} else if (path.contains("woodland_mansion")) {
				pool(builder, item("captain_coat"), 0.30f);
				pool(builder, ModGear.PAULDRON, 0.15f);
			} else if (path.contains("igloo")) {
				pool(builder, item("snow_cape"), 0.20f);
			} else if (path.contains("ruined_portal")) {
				pool(builder, item("obsidian_crown"), 0.20f);
			} else if (path.contains("nether_bridge")) {
				pool(builder, item("fortress_banner"), 0.25f);
				pool(builder, ModTech.STOMP, 0.12f);
			} else if (path.contains("bastion")) {
				pool(builder, item("piglin_headband"), 0.30f);
				pool(builder, ModGear.PAULDRON, 0.15f);
				pool(builder, ModTech.LUNGE, 0.10f);
			} else if (path.contains("end_city")) {
				pool(builder, item("chorus_shawl"), 0.30f);
				pool(builder, ModTech.LUNGE, 0.12f);
			} else if (path.contains("ancient_city")) {
				pool(builder, item("sculk_cape"), 0.15f);
				pool(builder, ModGear.BACK, 0.10f);
				pool(builder, ModTech.BURST, 0.10f);
			} else if (path.contains("trial_chambers/reward")) {
				pool(builder, item("trial_cape"), path.contains("ominous") ? 0.40f : 0.10f);
				if (path.contains("ominous")) {
					pool(builder, ModTech.BURST, 0.20f);
				} else {
					pool(builder, ModTech.STOMP, 0.08f);
				}
			} else if (path.contains("shipwreck")) {
				pool(builder, item("sailor_coat"), 0.20f);
			} else if (path.contains("underwater_ruin")) {
				pool(builder, item("sea_veil"), 0.25f);
			} else if (path.contains("underwater_ruin") || path.contains("monument")) {
				pool(builder, item("prismarine_crown"), 0.35f);
			} else if (path.contains("witch")) {
				pool(builder, item("witch_hat"), 0.25f);
			}
		});
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> CosmeticDrops.roll(entity, source));
		trade(VillagerProfession.WEAPONSMITH, 5, ModTech.BASH, 18, 0);
		trade(VillagerProfession.WEAPONSMITH, 3, ModTech.FAN, 14, 0);
		trade(VillagerProfession.WEAPONSMITH, 4, ModGear.SHOULDER, 10, 0);
		trade(VillagerProfession.WEAPONSMITH, 5, ModGear.BACK, 16, 1);
		trade(VillagerProfession.ARMORER, 5, ModGear.PAULDRON, 16, 0);
		trade(VillagerProfession.ARMORER, 4, ModGear.LINING, 12, 0);
		trade(VillagerProfession.LEATHERWORKER, 3, ModGear.BELT, 8, 0);
		trade(VillagerProfession.FLETCHER, 3, ModGear.QUIVER, 6, 0);
		trade(VillagerProfession.CLERIC, 3, ModGear.POTION_BAG, 6, 0);
		trade(VillagerProfession.TOOLSMITH, 2, ModTech.MOLD_LIGHT, 8, 0);
		trade(VillagerProfession.WEAPONSMITH, 3, ModTech.MOLD_ONE, 12, 0);
		trade(VillagerProfession.WEAPONSMITH, 4, ModTech.MOLD_TWO, 16, 1);
		trade(VillagerProfession.WEAPONSMITH, 3, ModTech.MOLD_SHIELD, 10, 0);
		trade(VillagerProfession.WEAPONSMITH, 5, ModTech.MOLD_EXTRA, 24, 0);
		trade(VillagerProfession.SHEPHERD, 1, item("shepherd_pattern"), 1, 0);
		trade(VillagerProfession.FARMER, 2, item("farmer_hat_pattern"), 1, 0);
		trade(VillagerProfession.FARMER, 3, item("farmer_apron_pattern"), 1, 0);
		trade(VillagerProfession.FISHERMAN, 2, item("fisherman_pattern"), 1, 0);
		trade(VillagerProfession.FLETCHER, 2, item("fletcher_pattern"), 1, 0);
		trade(VillagerProfession.LIBRARIAN, 2, item("librarian_pattern"), 1, 0);
		trade(VillagerProfession.CARTOGRAPHER, 2, item("cartographer_pattern"), 1, 0);
		trade(VillagerProfession.BUTCHER, 2, item("butcher_pattern"), 1, 0);
		trade(VillagerProfession.LEATHERWORKER, 2, item("leatherworker_pattern"), 1, 0);
		trade(VillagerProfession.MASON, 2, item("mason_pattern"), 1, 0);
		trade(VillagerProfession.CLERIC, 3, item("cleric_pattern"), 1, 0);
		trade(VillagerProfession.ARMORER, 3, item("armorer_pattern"), 1, 0);
		trade(VillagerProfession.WEAPONSMITH, 3, item("weaponsmith_pattern"), 1, 0);
		trade(VillagerProfession.TOOLSMITH, 3, item("toolsmith_pattern"), 1, 0);
	}

	private static void pool(net.minecraft.world.level.storage.loot.LootTable.Builder builder, net.minecraft.world.item.Item item, float chance) {
		if (item == null || item == Items.AIR) {
			return;
		}
		builder.pool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))).when(net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance(chance)).build());
	}

	private static net.minecraft.world.item.Item item(String path) {
		return net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.Identifier.fromNamespaceAndPath("vplus", path)).map(net.minecraft.core.Holder::value).orElse(Items.AIR);
	}

	private static void trade(net.minecraft.resources.ResourceKey<VillagerProfession> profession, int level, net.minecraft.world.item.Item item, int emeralds, int diamonds) {
		if (item == null || item == Items.AIR) {
			return;
		}
		TradeOfferHelper.registerVillagerOffers(profession, level, factories -> factories.add((levelWorld, trader, random) -> {
			ItemCost cost = new ItemCost(Items.EMERALD, emeralds);
			ItemStack result = new ItemStack(item);
			Optional<ItemCost> extra = diamonds > 0 ? Optional.of(new ItemCost(Items.DIAMOND, diamonds)) : Optional.empty();
			return new MerchantOffer(cost, extra, result, 8, 10, 0.05f);
		}));
	}
}

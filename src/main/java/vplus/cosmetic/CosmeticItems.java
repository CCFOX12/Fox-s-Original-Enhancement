package vplus.cosmetic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import vplus.VPlusMod;
import vplus.item.ItemStats;
import vplus.item.ModComponents;
import vplus.item.ModItems;

public final class CosmeticItems {
	public static Item MINER_HAT;

	private CosmeticItems() {
	}

	public static void register() {
		piece("zombie_coat", "僵尸外套", "display_coat", "zombie", 1);
		piece("zombie_legs", "僵尸裤装", "display_legs", "zombie", 1);
		piece("husk_head", "尸壳头饰", "display_head", "husk", 1);
		piece("husk_coat", "尸壳外套", "display_coat", "husk", 1);
		piece("husk_legs", "尸壳裤装", "display_legs", "husk", 1);
		piece("drowned_head", "溺尸头饰", "display_head", "drowned", 1);
		piece("drowned_coat", "溺尸外套", "display_coat", "drowned", 1);
		piece("drowned_legs", "溺尸裤装", "display_legs", "drowned", 1);
		piece("skeleton_coat", "骷髅骨架", "display_coat", "skeleton", 1);
		piece("stray_coat", "流浪者披肩", "display_coat", "stray", 1);
		piece("bogged_coat", "沼骸披肩", "display_coat", "bogged", 1);
		piece("wither_skeleton_coat", "凋零骷髅骨架", "display_coat", "wither_skeleton", 1);
		piece("witch_head", "女巫头饰", "display_head", "witch", 1);
		piece("witch_coat", "女巫外套", "display_coat", "witch", 1);
		piece("pillager_coat", "掠夺者长袍", "display_set", "pillager", 4);
		piece("vindicator_coat", "卫道士长袍", "display_set", "vindicator", 4);
		piece("evoker_coat", "唤魔者长袍", "display_set", "evoker", 4);
		piece("piglin_coat", "猪灵外套", "display_coat", "piglin", 1);
		piece("piglin_legs", "猪灵裤装", "display_legs", "piglin", 1);
		piece("zombified_piglin_coat", "僵尸猪灵外套", "display_coat", "zombified_piglin", 1);
		piece("zombified_piglin_legs", "僵尸猪灵裤装", "display_legs", "zombified_piglin", 1);
		piece("piglin_brute_head", "腐败头骨", "display_head", "piglin_brute", 1);
		piece("piglin_brute_coat", "腐败躯干", "display_coat", "piglin_brute", 1);
		whole("creeper", "苦力怕");
		whole("spider", "蜘蛛");
		whole("cave_spider", "洞穴蜘蛛");
		whole("slime", "史莱姆");
		whole("magma_cube", "岩浆怪");
		whole("blaze", "烈焰人");
		whole("ghast", "恶魂");
		whole("shulker", "潜影贝");
		whole("phantom", "幻翼");
		whole("guardian", "守卫者");
		whole("elder_guardian", "远古守卫者");
		whole("breeze", "旋风人");
		whole("creaking", "嘎枝");
		whole("silverfish", "蠹虫");
		whole("endermite", "末影螨");
		whole("vex", "恼鬼");
		whole("ravager", "劫掠兽");
		whole("hoglin", "疣猪兽");
		whole("zoglin", "僵尸疣猪兽");
		whole("zombie_horse", "僵尸马");
		whole("camel_husk", "骆驼尸壳");
		whole("zombie_nautilus", "僵尸鹦鹉螺");
		piece("enderman_head", "末影人头饰", "display_head", "enderman", 1);
		piece("enderman_coat", "末影人外套", "display_coat", "enderman", 1);
		piece("warden_head", "远古触角", "display_head", "warden", 1);
		piece("warden_coat", "监守者外套", "display_coat", "warden", 1);
		piece("wither_coat", "凋灵外套", "display_coat", "wither", 1);
		MINER_HAT = piece("miner_hat", "矿工头灯帽", "utility_head", "", 0);
		piece("pirate_bandana", "海盗头巾", "display_head", "", 0);
		piece("desert_scarf", "沙漠头巾", "display_head", "", 0);
		piece("jungle_mask", "丛林面具", "display_head", "", 0);
		piece("witch_hat", "女巫学徒帽", "display_head", "", 0);
		piece("sailor_coat", "水手外套", "display_coat", "", 0);
		piece("sea_veil", "海底头纱", "display_head", "", 0);
		piece("prismarine_crown", "海晶石头饰", "display_head", "", 0);
		piece("outpost_cape", "前哨斗篷", "display_coat", "", 0);
		piece("captain_coat", "队长外套", "display_coat", "", 0);
		piece("snow_cape", "雪地斗篷", "display_coat", "", 0);
		piece("sniffer_goggles", "嗅探兽护目镜", "display_head", "", 0);
		piece("obsidian_crown", "黑曜石头饰", "display_head", "", 0);
		piece("fortress_banner", "下界要塞旗帜", "display_coat", "", 0);
		piece("piglin_headband", "猪灵头带", "display_head", "", 0);
		piece("chorus_shawl", "紫颂披肩", "display_coat", "", 0);
		piece("sculk_cape", "幽匿斗篷", "display_coat", "", 0);
		piece("trial_cape", "试炼披风", "display_coat", "", 0);
		cloth("shepherd", "牧羊人", "display_coat", "牧羊人披肩");
		cloth("farmer_hat", "农民", "display_head", "草帽");
		cloth("farmer_apron", "农民", "display_coat", "耕围裙");
		cloth("fisherman", "渔夫", "display_head", "斗笠");
		cloth("fletcher", "制箭师", "display_coat", "护臂");
		cloth("librarian", "图书管理员", "display_coat", "图书长袍");
		cloth("cartographer", "制图师", "display_coat", "旅行斗篷");
		cloth("butcher", "屠夫", "display_coat", "屠夫围裙");
		cloth("leatherworker", "皮匠", "display_coat", "皮背心");
		cloth("mason", "石匠", "display_head", "石尘头巾");
		cloth("cleric", "牧师", "display_coat", "牧师长袍");
		cloth("armorer", "盔甲匠", "display_coat", "锻甲围裙");
		cloth("weaponsmith", "武器匠", "display_coat", "锻工护臂");
		cloth("toolsmith", "工具匠", "display_coat", "工具匠围裙");
	}

	private static void whole(String mob, String zh) {
		piece(mob + "_hide", zh + "整件外观", "display_coat", mob, 4);
	}

	private static void cloth(String id, String job, String slot, String name) {
		piece(id + "_pattern", job + "样布", "pattern", "", 0);
		piece(id + "_cloth", name, slot, "", 0);
	}

	private static Item piece(String path, String name, String slot, String mob, int pieces) {
		var key = ItemStats.key(path);
		var properties = new Item.Properties().setId(key).stacksTo(slot.equals("pattern") ? 16 : 1);
		var model = net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder");
		if (VanillaMobSkins.get(mob) != null && slot.startsWith("display_")) {
			model = net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, path);
		}
		properties.component(DataComponents.ITEM_MODEL, model);
		properties.component(ModComponents.SLOT_KIND, slot);
		equip(properties, slot);
		if (!mob.isEmpty()) {
			properties.component(ModComponents.COSMETIC_MOB, mob);
			properties.component(ModComponents.COSMETIC_PIECES, pieces);
		}
		Item item = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, key, new Item(properties));
		ModItems.CREATIVE.add(item);
		if (vplus.cosmetic.CosmeticWear.armorSlot(slot) != null) {
			vplus.cosmetic.CosmeticDispense.register(item);
		}
		return item;
	}

	private static void equip(Item.Properties properties, String slot) {
		EquipmentSlot equipment = switch (slot) {
			case "display_head", "utility_head" -> EquipmentSlot.HEAD;
			case "display_coat", "utility_coat", "display_set" -> EquipmentSlot.CHEST;
			case "display_legs", "utility_legs" -> EquipmentSlot.LEGS;
			case "display_feet", "utility_feet" -> EquipmentSlot.FEET;
			default -> null;
		};
		if (equipment == null) {
			return;
		}
		properties.component(DataComponents.EQUIPPABLE, Equippable.builder(equipment)
				.setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
				.setEquipOnInteract(true)
				.setSwappable(true)
				.setDispensable(true)
				.setDamageOnHurt(false)
				.setAllowedEntities(EntityType.PLAYER, EntityType.ARMOR_STAND)
				.build());
	}
}

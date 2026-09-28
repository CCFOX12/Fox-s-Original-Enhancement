package vplus.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vplus.VPlusMod;

public final class ModGear {
	public static Item PAULDRON;
	public static Item LINING;
	public static Item BELT;
	public static Item SHOULDER;
	public static Item BACK;
	public static Item QUIVER;
	public static Item POTION_BAG;
	public static Item TASTY_BREAD;
	public static Item ENCHANT_COPPER;
	public static Item ENCHANT_IRON;
	public static Item ENCHANT_GOLD;
	public static Item ENCHANT_NETHERITE;

	private ModGear() {
	}

	public static void register() {
		PAULDRON = piece("pauldron", "肩甲", "gear_pauldron", 1, 0);
		LINING = piece("lining", "锁链内衬", "gear_lining", 1, 120);
		BELT = piece("belt", "武器腰带", "gear_belt", 1, 0);
		SHOULDER = piece("shoulder_strap", "武器肩带", "gear_shoulder", 1, 0);
		BACK = piece("back_strap", "武器背带", "gear_back", 1, 0);
		QUIVER = piece("quiver", "箭袋", "bag_quiver", 1, 0);
		POTION_BAG = piece("potion_bag", "药水袋", "bag_potion", 1, 0);
		TASTY_BREAD = food("tasty_bread", "美味面包");
		ENCHANT_COPPER = piece("enchanted_copper", "附魔铜锭", "enchant_ingot", 64, 0);
		ENCHANT_IRON = piece("enchanted_iron", "附魔铁锭", "enchant_ingot", 64, 0);
		ENCHANT_GOLD = piece("enchanted_gold", "附魔金锭", "enchant_ingot", 64, 0);
		ENCHANT_NETHERITE = piece("enchanted_netherite", "附魔下界合金锭", "enchant_ingot", 64, 0);
	}

	public static Item piece(String path, String name, String kind, int stack, int durability) {
		var key = ItemStats.key(path);
		Item.Properties properties = new Item.Properties().setId(key).stacksTo(stack);
		if (durability > 0) {
			properties.durability(durability);
		}
		properties.component(DataComponents.ITEM_MODEL, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder"));
		properties.component(ModComponents.SLOT_KIND, kind);
		Item item = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, key, new Item(properties));
		ModItems.CREATIVE.add(item);
		ModItems.ALL_MOD.add(item);
		return item;
	}

	private static Item food(String path, String name) {
		var key = ItemStats.key(path);
		Item.Properties properties = new Item.Properties().setId(key).stacksTo(64);
		properties.component(DataComponents.ITEM_MODEL, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder"));
		properties.food(new net.minecraft.world.food.FoodProperties(5, 0.7f, false));
		Item item = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, key, new Item(properties));
		ModItems.CREATIVE.add(item);
		return item;
	}

	public static boolean kind(ItemStack stack, String kind) {
		return kind.equals(stack.get(ModComponents.SLOT_KIND));
	}
}

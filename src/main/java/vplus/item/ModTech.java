package vplus.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vplus.VPlusMod;
import vplus.affix.Techniques;

public final class ModTech {
	public static Item BASH;
	public static Item BURST;
	public static Item FAN;
	public static Item LUNGE;
	public static Item STOMP;
	public static Item MOLD_LIGHT;
	public static Item MOLD_ONE;
	public static Item MOLD_TWO;
	public static Item MOLD_SHIELD;
	public static Item MOLD_EXTRA;

	private ModTech() {
	}

	public static void register() {
		BASH = technique("technique_bash", "技巧模板：冲撞", Techniques.BASH);
		BURST = technique("technique_burst", "技巧模板：爆风", Techniques.BURST);
		FAN = technique("technique_fan", "技巧模板：扇形斩", Techniques.FAN);
		LUNGE = technique("technique_lunge", "技巧模板：突刺", Techniques.LUNGE);
		STOMP = technique("technique_stomp", "技巧模板：跺地", Techniques.STOMP);
		MOLD_LIGHT = mold("mold_light", "轻刃钻孔模", 1);
		MOLD_ONE = mold("mold_one", "单手钻孔模", 1);
		MOLD_TWO = mold("mold_two", "双手钻孔模", 1);
		MOLD_SHIELD = mold("mold_shield", "盾面钻孔模", 1);
		MOLD_EXTRA = mold("mold_extra", "复槽模具", 2);
	}

	public static boolean isTechnique(ItemStack stack) {
		return idOf(stack) != null;
	}

	public static String idOf(ItemStack stack) {
		return stack.get(ModComponents.TECHNIQUE);
	}

	public static boolean isMold(ItemStack stack) {
		Integer sockets = stack.get(ModComponents.SOCKETS);
		return sockets != null && "mold".equals(stack.get(ModComponents.SLOT_KIND));
	}

	public static int socketsOf(ItemStack mold, ItemStack base) {
		if (!isMold(mold)) {
			return 0;
		}
		return mold.get(ModComponents.SOCKETS);
	}

	public static boolean isGem(ItemStack stack) {
		String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		return path.endsWith("_ingot") || path.equals("coal") || path.equals("redstone") || path.equals("lapis_lazuli") || path.equals("amethyst_shard") || path.equals("emerald") || path.equals("diamond") || path.equals("quartz");
	}

	private static Item technique(String path, String name, String id) {
		Item item = item(path, name, 64);
		return item;
	}

	private static Item mold(String path, String name, int sockets) {
		var key = ItemStats.key(path);
		Item.Properties properties = base(key, name, 1);
		properties.component(ModComponents.SLOT_KIND, "mold");
		properties.component(ModComponents.SOCKETS, sockets);
		return register(key, properties);
	}

	private static Item item(String path, String name, int stack) {
		var key = ItemStats.key(path);
		Item.Properties properties = base(key, name, stack);
		String id = switch (path) {
			case "technique_bash" -> Techniques.BASH;
			case "technique_burst" -> Techniques.BURST;
			case "technique_fan" -> Techniques.FAN;
			case "technique_lunge" -> Techniques.LUNGE;
			default -> Techniques.STOMP;
		};
		properties.component(ModComponents.TECHNIQUE, id);
		return register(key, properties);
	}

	private static Item.Properties base(net.minecraft.resources.ResourceKey<Item> key, String name, int stack) {
		Item.Properties properties = new Item.Properties().setId(key).stacksTo(stack);
		properties.component(DataComponents.ITEM_MODEL, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder"));
		return properties;
	}

	private static Item register(net.minecraft.resources.ResourceKey<Item> key, Item.Properties properties) {
		Item item = net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ITEM, key, new Item(properties));
		ModItems.CREATIVE.add(item);
		return item;
	}
}

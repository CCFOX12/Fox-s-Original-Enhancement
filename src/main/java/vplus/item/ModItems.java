package vplus.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import vplus.VPlusMod;
import vplus.weapon.Blueprint;
import vplus.weapon.DamageKind;
import vplus.weapon.MaterialTier;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;
import vplus.weapon.ShieldSpec;
import vplus.weapon.WeaponSize;

public final class ModItems {
	public static Item IRON_KNIFE;
	public static final List<Item> CREATIVE = new ArrayList<>();
	public static final List<Item> ALL_MOD = new ArrayList<>();

	private static final ShieldSpec BUCKLER = new ShieldSpec(1, 90.0f, 0.10f, 0.95f, 3.0f, 3.0f);
	private static final ShieldSpec MEDIUM = new ShieldSpec(2, 120.0f, 0.20f, 0.80f, 2.0f, 5.0f);
	private static final ShieldSpec TOWER = new ShieldSpec(3, 150.0f, 0.35f, 0.65f, 1.0f, 6.0f);
	private static final ShieldSpec HEAVY = new ShieldSpec(4, 180.0f, 0.50f, 0.45f, 0.0f, 8.0f);

	private ModItems() {
	}

	public static void register() {
		registerFamilies();
		bindVanilla();
		VPlusMod.LOGGER.info("原版增强武器条目 {}", Profiles.BY_ITEM.size());
	}

	private static void registerFamilies() {
		family(weapon("machete", "砍刀", DamageKind.SLASH, Profiles.traits(), WeaponSize.MEDIUM, 6, 0, 1.60f, 0, 3.0f, 3.0f, 0.50f, 0, 0, 0, 250, "sword", ItemStats.ALL));
		family(weapon("shortsword", "短剑", DamageKind.PIERCE, Profiles.traits(Profile.Trait.LIGHT, Profile.Trait.FINESSE), WeaponSize.SMALL, 5, 0, 2.20f, 0, 2.6f, 2.0f, 0, 0, 0, 0, 250, "shortsword", ItemStats.ALL));
		family(weapon("rapier", "细剑", DamageKind.PIERCE, Profiles.traits(Profile.Trait.FINESSE), WeaponSize.MEDIUM, 6, 0, 1.80f, 0, 3.2f, 2.0f, 0, 0, 0, 0, 250, "rapier", ItemStats.ALL));
		family(weapon("hammer", "铁锤", DamageKind.BLUNT, Profiles.traits(Profile.Trait.VERSATILE), WeaponSize.MEDIUM, 6, 8, 1.60f, 1.40f, 3.0f, 4.0f, 0, 0, 0, 0, 500, "light_blunt", ItemStats.ALL));
		family(weapon("throwing_axe", "飞斧", DamageKind.SLASH, Profiles.traits(Profile.Trait.LIGHT, Profile.Trait.THROWN), WeaponSize.SMALL, 5, 0, 2.00f, 0, 3.0f, 2.0f, 0, 0, 6, 12, 64, "throwing_axe", ItemStats.ALL));
		family(weapon("throwing_knife", "飞刀", DamageKind.PIERCE, Profiles.traits(Profile.Trait.LIGHT, Profile.Trait.FINESSE, Profile.Trait.THROWN), WeaponSize.SMALL, 3, 0, 2.20f, 0, 2.4f, 0.5f, 0, 0, 8, 16, 0, "throwing_knife", ItemStats.ALL));
		family(weapon("gauntlet", "拳套", DamageKind.BLUNT, Profiles.traits(Profile.Trait.LIGHT, Profile.Trait.FINESSE), WeaponSize.SMALL, 3, 0, 2.20f, 0, 2.4f, 1.0f, 0, 0, 0, 0, 180, "gauntlet", List.of(MaterialTier.LEATHER, MaterialTier.IRON, MaterialTier.GOLD, MaterialTier.DIAMOND, MaterialTier.NETHERITE)));
		family(weapon("katana", "长刀", DamageKind.SLASH, Profiles.traits(Profile.Trait.TWO_HAND, Profile.Trait.FINESSE), WeaponSize.HEAVY, 8, 0, 1.80f, 0, 3.2f, 3.0f, 0, 0, 0, 0, 250, "katana", List.of(MaterialTier.IRON, MaterialTier.GOLD, MaterialTier.DIAMOND, MaterialTier.NETHERITE)));
		family(weapon("dart", "镖", DamageKind.PIERCE, Profiles.traits(Profile.Trait.THROWN, Profile.Trait.REACH), WeaponSize.MEDIUM, 5, 0, 1.60f, 0, 4.0f, 2.0f, 0, 0, 10, 20, 250, "pole", List.of(MaterialTier.BAMBOO, MaterialTier.IRON)));
		family(weapon("great_hammer", "大铁锤", DamageKind.BLUNT, Profiles.traits(Profile.Trait.HEAVY, Profile.Trait.TWO_HAND), WeaponSize.HEAVY, 11, 0, 1.10f, 0, 3.0f, 10.0f, 0, 5, 0, 0, 500, "heavy_blunt", List.of(MaterialTier.IRON, MaterialTier.NETHERITE)));
		family(weapon("flail", "链锤", DamageKind.BLUNT, Profiles.traits(Profile.Trait.HEAVY, Profile.Trait.TWO_HAND, Profile.Trait.REACH), WeaponSize.HEAVY, 8, 0, 1.10f, 0, 4.0f, 6.0f, 0, 0, 0, 0, 400, "flail", List.of(MaterialTier.IRON, MaterialTier.NETHERITE)));
		family(shield("buckler", "小圆盾", BUCKLER, Profiles.traits(Profile.Trait.LIGHT), WeaponSize.SMALL, 2.0f, ItemStats.ALL));
		family(shield("medium_shield", "中盾", MEDIUM, Profiles.traits(), WeaponSize.MEDIUM, 6.0f, ItemStats.ALL));
		family(shield("tower_shield", "大盾", TOWER, Profiles.traits(Profile.Trait.HEAVY), WeaponSize.HEAVY, 10.0f, ItemStats.ALL));
		family(shield("heavy_shield", "重盾", HEAVY, Profiles.traits(Profile.Trait.HEAVY, Profile.Trait.TWO_HAND), WeaponSize.HEAVY, 16.0f, List.of(MaterialTier.IRON, MaterialTier.NETHERITE)));
		Item unique = simple("unique_knife", "独飞刀", 1);
		if (IRON_KNIFE != null) {
			Profile knife = Profiles.of(IRON_KNIFE);
			if (knife != null) {
				Profiles.put(knife.withItem(unique));
			}
		}
	}

	private static void bindVanilla() {
		bindTools(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.COPPER_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
				swordLike("sword", DamageKind.SLASH, Profiles.traits(Profile.Trait.VERSATILE), WeaponSize.MEDIUM, 6, 8, 1.60f, 1.40f, 3, 3, 0.40f, 0, 0, 0, 250, true, "sword"));
		bindTools(Items.WOODEN_AXE, Items.STONE_AXE, Items.COPPER_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE,
				swordLike("axe", DamageKind.SLASH, Profiles.traits(Profile.Trait.VERSATILE), WeaponSize.MEDIUM, 6, 8, 1.60f, 1.40f, 3, 4, 0, 5, 0, 0, 250, false, "axe"));
		bindTools(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.COPPER_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE,
				swordLike("pickaxe", DamageKind.PIERCE, Profiles.traits(), WeaponSize.MEDIUM, 6, 0, 1.60f, 0, 3, 4, 0, 0, 0, 0, 250, false, "pickaxe"));
		bindTools(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.COPPER_SHOVEL, Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL,
				swordLike("shovel", DamageKind.BLUNT, Profiles.traits(), WeaponSize.MEDIUM, 5, 0, 1.60f, 0, 3, 3, 0, 0, 0, 0, 250, false, "light_blunt"));
		bindTools(Items.WOODEN_HOE, Items.STONE_HOE, Items.COPPER_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE,
				swordLike("hoe", DamageKind.SLASH, Profiles.traits(Profile.Trait.LIGHT), WeaponSize.SMALL, 3, 0, 2.00f, 0, 2.6f, 2, 0, 0, 0, 0, 250, false, "hoe"));
		bindTools(Items.WOODEN_SPEAR, Items.STONE_SPEAR, Items.COPPER_SPEAR, Items.IRON_SPEAR, Items.GOLDEN_SPEAR, Items.DIAMOND_SPEAR, Items.NETHERITE_SPEAR,
				swordLike("spear", DamageKind.PIERCE, Profiles.traits(Profile.Trait.VERSATILE, Profile.Trait.THROWN, Profile.Trait.REACH), WeaponSize.MEDIUM, 5, 6, 1.40f, 1.20f, 4, 3, 0, 0, 8, 16, 250, true, "pole"));
		bind(Items.TRIDENT, swordLike("trident", DamageKind.PIERCE, Profiles.traits(Profile.Trait.VERSATILE, Profile.Trait.THROWN, Profile.Trait.REACH), WeaponSize.MEDIUM, 6, 8, 1.40f, 1.20f, 4, 4, 0, 0, 10, 20, 250, true, "pole"), MaterialTier.IRON);
		bind(Items.MACE, swordLike("mace", DamageKind.BLUNT, Profiles.traits(Profile.Trait.HEAVY), WeaponSize.HEAVY, 8, 0, 1.10f, 0, 3, 4, 0, 0, 0, 0, 500, true, "heavy_blunt"), MaterialTier.IRON);
		bind(Items.BOW, swordLike("bow", DamageKind.PIERCE, Profiles.traits(Profile.Trait.AMMO, Profile.Trait.TWO_HAND), WeaponSize.HEAVY, 6, 0, 1.00f, 0, 0, 2, 0, 0, 16, 32, 384, true, "bow"), MaterialTier.IRON);
		bind(Items.CROSSBOW, swordLike("crossbow", DamageKind.PIERCE, Profiles.traits(Profile.Trait.AMMO, Profile.Trait.TWO_HAND, Profile.Trait.LOAD), WeaponSize.HEAVY, 8, 0, 1.00f, 0, 0, 3, 0, 0, 16, 32, 465, true, "crossbow"), MaterialTier.IRON);
		bind(Items.SHIELD, shield("vanilla_shield", "盾", MEDIUM, Profiles.traits(), WeaponSize.MEDIUM, 6.0f, List.of(MaterialTier.IRON)), MaterialTier.IRON);
	}

	private static void family(Blueprint blueprint) {
		for (MaterialTier tier : blueprint.tiers()) {
			String path = tier.id + "_" + blueprint.baseId();
			String name = tier.zh + blueprint.zh();
			Item item = registerWeapon(path, name, tier, blueprint);
			Profiles.put(ItemStats.resolve(blueprint, tier, item, true));
		}
	}

	private static void bindTools(Item wood, Item stone, Item copper, Item iron, Item gold, Item diamond, Item netherite, Blueprint blueprint) {
		bind(wood, blueprint, MaterialTier.WOOD);
		bind(stone, blueprint, MaterialTier.STONE);
		bind(copper, blueprint, MaterialTier.COPPER);
		bind(iron, blueprint, MaterialTier.IRON);
		bind(gold, blueprint, MaterialTier.GOLD);
		bind(diamond, blueprint, MaterialTier.DIAMOND);
		bind(netherite, blueprint, MaterialTier.NETHERITE);
	}

	private static void bind(Item item, Blueprint blueprint, MaterialTier tier) {
		Profiles.put(ItemStats.resolve(blueprint, tier, item, blueprint.replaceDurability()));
	}

	private static Item registerWeapon(String path, String name, MaterialTier tier, Blueprint blueprint) {
		var key = ItemStats.key(path);
		Item.Properties properties = new Item.Properties().setId(key);
		properties.component(DataComponents.ITEM_MODEL, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder"));
		Profile preview = ItemStats.resolve(blueprint, tier, Items.AIR, true);
		properties.attributes(ItemStats.attributes(preview));
		if (preview.shield != null) {
			properties.durability(preview.durability);
			properties.component(DataComponents.BLOCKS_ATTACKS, ItemStats.blocks(preview.shield));
			properties.equippableUnswappable(net.minecraft.world.entity.EquipmentSlot.OFFHAND);
		} else if (preview.has(Profile.Trait.THROWN) && blueprint.baseId().equals("throwing_knife")) {
			properties.stacksTo(16);
		} else if (preview.durability > 0) {
			properties.durability(preview.durability);
		}
		if (preview.shield == null && !preview.has(Profile.Trait.AMMO)) {
			properties.component(DataComponents.ATTACK_RANGE, ItemStats.range(preview));
			properties.component(DataComponents.WEAPON, ItemStats.weaponComponent(preview));
		}
		properties.enchantable(tier.enchantability);
		if (tier == MaterialTier.NETHERITE) {
			properties.fireResistant();
			properties.rarity(Rarity.UNCOMMON);
		}
		Item registered = Registry.register(BuiltInRegistries.ITEM, key, new VPlusWeaponItem(properties));
		if ("iron_throwing_knife".equals(path)) {
			IRON_KNIFE = registered;
		}
		ALL_MOD.add(registered);
		CREATIVE.add(registered);
		return registered;
	}

	public static Item simple(String path, String name, int stack) {
		var key = ItemStats.key(path);
		Item.Properties properties = new Item.Properties().setId(key).stacksTo(stack);
		properties.component(DataComponents.ITEM_MODEL, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "placeholder"));
		Item item = Registry.register(BuiltInRegistries.ITEM, key, new Item(properties));
		ALL_MOD.add(item);
		CREATIVE.add(item);
		return item;
	}

	public static ItemStack stack(Item item) {
		return new ItemStack(item);
	}

	private static Blueprint weapon(
			String id, String zh, DamageKind kind, java.util.EnumSet<Profile.Trait> traits, WeaponSize size,
			int damage, int twoDamage, float speed, float twoSpeed, float reach, float weight, float sweep,
			float shieldBreak, float near, float far, int durability, String group, List<MaterialTier> tiers) {
		return Blueprint.weapon(id, zh, kind, traits, size, damage, twoDamage, speed, twoSpeed, reach, weight, sweep, shieldBreak, near, far, durability, group, tiers);
	}

	private static Blueprint swordLike(
			String id, DamageKind kind, java.util.EnumSet<Profile.Trait> traits, WeaponSize size,
			int damage, int twoDamage, float speed, float twoSpeed, float reach, float weight, float sweep,
			float shieldBreak, float near, float far, int durability, boolean replaceDurability, String group) {
		return new Blueprint(id, id, kind, traits, size, damage, twoDamage, speed, twoSpeed, reach, weight, sweep, shieldBreak, near, far, durability, replaceDurability, null, group, List.of(MaterialTier.IRON));
	}

	private static Blueprint shield(String id, String zh, ShieldSpec spec, java.util.EnumSet<Profile.Trait> traits, WeaponSize size, float weight, List<MaterialTier> tiers) {
		return new Blueprint(id, zh, DamageKind.NONE, traits, size, 3, 0, 1.60f, 0, 0, weight, 0, 0, 0, 0, 336, true, spec, "shield", tiers);
	}
}

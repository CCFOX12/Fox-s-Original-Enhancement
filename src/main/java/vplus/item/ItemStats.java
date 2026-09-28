package vplus.item;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import vplus.VPlusMod;
import vplus.weapon.Blueprint;
import vplus.weapon.MaterialMath;
import vplus.weapon.MaterialTier;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;
import vplus.weapon.ShieldSpec;

public final class ItemStats {
	public static final Identifier REACH_ID = Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "reach");
	public static final List<MaterialTier> ALL = List.of(
			MaterialTier.WOOD, MaterialTier.STONE, MaterialTier.COPPER, MaterialTier.IRON,
			MaterialTier.GOLD, MaterialTier.DIAMOND, MaterialTier.NETHERITE);

	private ItemStats() {
	}

	public static Profile resolve(Blueprint blueprint, MaterialTier tier, Item item, boolean replaceDurability) {
		float one = blueprint.shield() == null ? MaterialMath.damage(blueprint.ironDamage(), tier) : 0.0f;
		float two = blueprint.ironTwoDamage() > 0 ? MaterialMath.damage(blueprint.ironTwoDamage(), tier) : 0.0f;
		float oneSpeed = MaterialMath.speed(blueprint.ironSpeed(), tier);
		float twoSpeed = blueprint.ironTwoSpeed() > 0.0f ? MaterialMath.speed(blueprint.ironTwoSpeed(), tier) : 0.0f;
		int durability = replaceDurability ? MaterialMath.durability(blueprint.durabilityBase(), tier) : 0;
		return new Profile(
				blueprint.baseId(),
				item,
				blueprint.damageKind(),
				blueprint.traits(),
				blueprint.size(),
				one,
				two,
				oneSpeed,
				twoSpeed,
				blueprint.reach(),
				blueprint.weight(),
				blueprint.sweep(),
				blueprint.shieldBreak(),
				blueprint.throwNear(),
				blueprint.throwFar(),
				durability,
				replaceDurability,
				blueprint.shield(),
				blueprint.affixGroup());
	}

	public static ItemAttributeModifiers attributes(Profile profile) {
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
		if (profile.shield == null && !profile.has(Profile.Trait.AMMO)) {
			builder.add(
					Attributes.ATTACK_DAMAGE,
					new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, profile.oneDamage - 1.0f, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
			builder.add(
					Attributes.ATTACK_SPEED,
					new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, profile.oneSpeed - 4.0f, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
		}
		if (profile.reach > 0.0f && profile.shield == null) {
			builder.add(
					Attributes.ENTITY_INTERACTION_RANGE,
					new AttributeModifier(REACH_ID, profile.reach - 3.0f, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND);
		}
		return builder.build();
	}

	public static AttackRange range(Profile profile) {
		float reach = profile.reach <= 0.0f ? 3.0f : profile.reach;
		return new AttackRange(0.0f, reach, 0.0f, reach, 0.3f, 0.0f);
	}

	public static BlocksAttacks blocks(ShieldSpec shield) {
		float factor = shield.reduction();
		return new BlocksAttacks(
				shield.raiseSeconds(),
				1.0f,
				List.of(new BlocksAttacks.DamageReduction(shield.angle() * 0.5f, Optional.empty(), 0.0f, factor)),
				BlocksAttacks.ItemDamageFunction.DEFAULT,
				Optional.empty(),
				Optional.of(net.minecraft.sounds.SoundEvents.SHIELD_BLOCK),
				Optional.of(net.minecraft.sounds.SoundEvents.SHIELD_BREAK));
	}

	public static Weapon weaponComponent(Profile profile) {
		float disable = profile.shieldBreak > 0.0f ? profile.shieldBreak : 0.0f;
		return new Weapon(1, disable);
	}

	public static ResourceKey<Item> key(String path) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, path));
	}
}

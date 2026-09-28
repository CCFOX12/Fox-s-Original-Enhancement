package vplus.weapon;

import java.util.EnumSet;

import net.minecraft.world.item.Item;

public final class Profile {
	public enum Trait {
		LIGHT,
		FINESSE,
		HEAVY,
		TWO_HAND,
		VERSATILE,
		THROWN,
		REACH,
		AMMO,
		LOAD
	}

	public final String id;
	public final Item item;
	public final DamageKind damageKind;
	public final EnumSet<Trait> traits;
	public final WeaponSize size;
	public final float oneDamage;
	public final float twoDamage;
	public final float oneSpeed;
	public final float twoSpeed;
	public final float reach;
	public final float weight;
	public final float sweep;
	public final float shieldBreak;
	public final float throwNear;
	public final float throwFar;
	public final int durability;
	public final boolean replaceDurability;
	public final ShieldSpec shield;
	public final String affixGroup;

	public Profile(
			String id,
			Item item,
			DamageKind damageKind,
			EnumSet<Trait> traits,
			WeaponSize size,
			float oneDamage,
			float twoDamage,
			float oneSpeed,
			float twoSpeed,
			float reach,
			float weight,
			float sweep,
			float shieldBreak,
			float throwNear,
			float throwFar,
			int durability,
			boolean replaceDurability,
			ShieldSpec shield,
			String affixGroup) {
		this.id = id;
		this.item = item;
		this.damageKind = damageKind;
		this.traits = traits;
		this.size = size;
		this.oneDamage = oneDamage;
		this.twoDamage = twoDamage;
		this.oneSpeed = oneSpeed;
		this.twoSpeed = twoSpeed;
		this.reach = reach;
		this.weight = weight;
		this.sweep = sweep;
		this.shieldBreak = shieldBreak;
		this.throwNear = throwNear;
		this.throwFar = throwFar;
		this.durability = durability;
		this.replaceDurability = replaceDurability;
		this.shield = shield;
		this.affixGroup = affixGroup;
	}

	public Profile withItem(Item replacement) {
		return new Profile(this.id, replacement, this.damageKind, this.traits, this.size, this.oneDamage, this.twoDamage, this.oneSpeed, this.twoSpeed, this.reach, this.weight, this.sweep, this.shieldBreak, this.throwNear, this.throwFar, this.durability, this.replaceDurability, this.shield, this.affixGroup);
	}

	public boolean has(Trait trait) {
		return this.traits.contains(trait);
	}

	public boolean suppressesOffhand() {
		return this.has(Trait.TWO_HAND) || this.has(Trait.HEAVY);
	}

	public float speedWithoutFinesse() {
		float speed = this.oneSpeed;
		if (this.has(Trait.LIGHT)) {
			speed -= 0.40f;
		}
		if (this.has(Trait.FINESSE)) {
			speed -= 0.20f;
		}
		return MaterialTier.roundSpeed(speed);
	}
}

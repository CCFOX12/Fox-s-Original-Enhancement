package vplus.weapon;

import java.util.EnumSet;
import java.util.List;

public record Blueprint(
		String baseId,
		String zh,
		DamageKind damageKind,
		EnumSet<Profile.Trait> traits,
		WeaponSize size,
		int ironDamage,
		int ironTwoDamage,
		float ironSpeed,
		float ironTwoSpeed,
		float reach,
		float weight,
		float sweep,
		float shieldBreak,
		float throwNear,
		float throwFar,
		int durabilityBase,
		boolean replaceDurability,
		ShieldSpec shield,
		String affixGroup,
		List<MaterialTier> tiers) {

	public static Blueprint weapon(
			String baseId,
			String zh,
			DamageKind kind,
			EnumSet<Profile.Trait> traits,
			WeaponSize size,
			int damage,
			int twoDamage,
			float speed,
			float twoSpeed,
			float reach,
			float weight,
			float sweep,
			float shieldBreak,
			float throwNear,
			float throwFar,
			int durability,
			String group,
			List<MaterialTier> tiers) {
		return new Blueprint(baseId, zh, kind, traits, size, damage, twoDamage, speed, twoSpeed, reach, weight, sweep, shieldBreak, throwNear, throwFar, durability, true, null, group, tiers);
	}
}

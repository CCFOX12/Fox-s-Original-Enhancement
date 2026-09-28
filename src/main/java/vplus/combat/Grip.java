package vplus.combat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;
import vplus.weapon.ShieldSpec;

public final class Grip {
	public final Profile main;
	public final Profile off;
	public final ItemStack mainStack;
	public final ItemStack offStack;
	public final boolean dual;
	public final boolean twoHandGrip;
	public final boolean suppressOffhand;
	public final boolean blocking;

	private Grip(Profile main, Profile off, ItemStack mainStack, ItemStack offStack, boolean dual, boolean twoHandGrip, boolean suppressOffhand, boolean blocking) {
		this.main = main;
		this.off = off;
		this.mainStack = mainStack;
		this.offStack = offStack;
		this.dual = dual;
		this.twoHandGrip = twoHandGrip;
		this.suppressOffhand = suppressOffhand;
		this.blocking = blocking;
	}

	public static Grip of(Player player) {
		ItemStack mainStack;
		ItemStack offStack;
		boolean blocking;
		try {
			mainStack = player.getMainHandItem();
			offStack = player.getOffhandItem();
			blocking = player.isBlocking();
		} catch (NullPointerException ignored) {
			return new Grip(null, null, ItemStack.EMPTY, ItemStack.EMPTY, false, false, false, false);
		}
		Profile main = Profiles.of(mainStack.getItem());
		Profile off = Profiles.of(offStack.getItem());
		boolean suppress = main != null && main.suppressesOffhand();
		boolean dual = !suppress && main != null && off != null && main.shield == null && off.shield == null && main.has(Profile.Trait.LIGHT) && off.has(Profile.Trait.LIGHT);
		boolean twoHandGrip = main != null && main.has(Profile.Trait.VERSATILE) && offStack.isEmpty();
		return new Grip(main, off, mainStack, offStack, dual, twoHandGrip, suppress, blocking);
	}

	public float attackDamage() {
		float damage = this.main == null ? 1.0f : (this.twoHandGrip && this.main.twoDamage > 0.0f ? this.main.twoDamage : this.main.oneDamage);
		vplus.affix.Affix affix = vplus.affix.Affix.of(this.mainStack);
		return affix == null ? damage : damage + affix.damage;
	}

	public float shownDamage() {
		return this.main == null ? 1.0f : this.main.oneDamage;
	}

	public float attackSpeed() {
		if (this.main == null) {
			return 2.20f;
		}
		float speed = this.main.oneSpeed;
		if (this.dual) {
			speed = 1.40f;
		} else if (this.twoHandGrip && this.main.twoSpeed > 0.0f) {
			speed = this.main.twoSpeed;
		}
		if (this.blocking) {
			speed = this.main.speedWithoutFinesse();
		}
		vplus.affix.Affix affix = vplus.affix.Affix.of(this.mainStack);
		if (affix != null) {
			speed += affix.speed;
		}
		return speed;
	}

	public float shownSpeed() {
		return this.main == null ? 4.0f : this.main.oneSpeed;
	}

	public float reach() {
		if (this.main == null) {
			return 2.4f;
		}
		float reach = this.main.reach <= 0.0f ? 3.0f : this.main.reach;
		if (this.dual && this.off != null) {
			reach = Math.min(reach, this.off.reach);
		}
		vplus.affix.Affix affix = vplus.affix.Affix.of(this.mainStack);
		return affix == null ? reach : reach + affix.reach;
	}

	public float shownReach() {
		if (this.main == null || this.main.reach <= 0.0f) {
			return 3.0f;
		}
		return this.main.reach;
	}

	public ShieldSpec raisedShield() {
		if (!this.blocking) {
			return null;
		}
		if (this.off != null && this.off.shield != null && !this.suppressOffhand) {
			return this.off.shield;
		}
		if (this.main != null && this.main.shield != null) {
			return this.main.shield;
		}
		return null;
	}

	public float moveMultiplier() {
		float weight = weightOf(this.main) + weightOf(this.off);
		vplus.affix.Affix affix = vplus.affix.Affix.of(this.mainStack);
		if (affix == vplus.affix.Affix.LIGHTEN) {
			weight = Math.max(0.0f, weight - 1.0f);
		}
		float extra = Math.max(0.0f, weight - 6.0f);
		float multiplier = 1.0f - Math.min(0.15f, extra * 0.01f);
		if ((this.main != null && this.main.has(Profile.Trait.HEAVY)) || (this.off != null && this.off.has(Profile.Trait.HEAVY))) {
			multiplier *= 0.90f;
		}
		ShieldSpec shield = this.raisedShield();
		if (shield != null) {
			multiplier *= shield.move();
		}
		return multiplier;
	}

	private static float weightOf(Profile profile) {
		return profile == null ? 0.0f : profile.weight;
	}
}

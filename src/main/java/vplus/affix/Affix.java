package vplus.affix;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import vplus.item.ModComponents;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public enum Affix {
	EDGE("G-L01", "锋刃", "低阶", 1, 0, 0, 0),
	QUICK("G-L02", "轻快", "低阶", 0, 0.08f, 0, 0),
	NUDGE("G-L03", "微击退", "低阶", 0, 0, 0.15f, 0),
	LONGER("G-L04", "加长", "低阶", 0, 0, 0, 0.2f),
	LIGHTEN("G-L05", "减轻", "低阶", 0, 0, 0, 0),
	UNARMORED("G-L07", "无甲", "低阶", 0, 0, 0, 0),
	ARMORED("G-L08", "对甲", "低阶", 0, 0, 0, 0),
	DEEP("G-M01", "深锋", "中阶", 2, 0, 0, 0),
	CHAIN("G-M02", "连击", "中阶", 0, 0.15f, 0, 0),
	SHOVE("G-M03", "强击退", "中阶", 0, 0, 0.3f, 0),
	FAR("G-M04", "远击", "中阶", 0, 0, 0, 0.4f),
	FINE("G-H01", "精工", "高阶", 3, 0.08f, 0, 0);

	public final String id;
	public final String name;
	public final String tier;
	public final float damage;
	public final float speed;
	public final float knockback;
	public final float reach;

	Affix(String id, String name, String tier, float damage, float speed, float knockback, float reach) {
		this.id = id;
		this.name = name;
		this.tier = tier;
		this.damage = damage;
		this.speed = speed;
		this.knockback = knockback;
		this.reach = reach;
	}

	public static final List<Affix> LOW = List.of(EDGE, QUICK, NUDGE, LONGER, LIGHTEN, UNARMORED, ARMORED);

	public static Affix byId(String id) {
		if (id == null) {
			return null;
		}
		for (Affix affix : values()) {
			if (affix.id.equals(id)) {
				return affix;
			}
		}
		return null;
	}

	public static Affix of(ItemStack stack) {
		return byId(stack.get(ModComponents.AFFIX));
	}

	public static void factory(ItemStack stack) {
		Profile profile = Profiles.of(stack.getItem());
		if (profile == null) {
			return;
		}
		if ("throwing_knife".equals(profile.id) && stack.getMaxStackSize() > 1) {
			return;
		}
		if (Affix.byId(stack.get(ModComponents.AFFIX)) != null) {
			return;
		}
		Affix rolled = LOW.get(ThreadLocalRandom.current().nextInt(LOW.size()));
		stack.set(ModComponents.AFFIX, rolled.id);
	}

	public Component line() {
		return Component.literal("锻纹 · " + this.tier + " · " + this.name);
	}
}

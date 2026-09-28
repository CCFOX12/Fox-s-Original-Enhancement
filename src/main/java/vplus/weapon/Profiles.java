package vplus.weapon;

import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.world.item.Item;

public final class Profiles {
	public static final Map<Item, Profile> BY_ITEM = new IdentityHashMap<>();

	private Profiles() {
	}

	public static Profile of(Item item) {
		return item == null ? null : BY_ITEM.get(item);
	}

	public static void put(Profile profile) {
		BY_ITEM.put(profile.item, profile);
	}

	public static EnumSet<Profile.Trait> traits(Profile.Trait... values) {
		EnumSet<Profile.Trait> set = EnumSet.noneOf(Profile.Trait.class);
		for (Profile.Trait value : values) {
			set.add(value);
		}
		return set;
	}
}

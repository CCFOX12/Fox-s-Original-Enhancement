package vplus.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public final class VanillaRetune {
	private VanillaRetune() {
	}

	public static void register() {
		DefaultItemComponentEvents.MODIFY.register(context -> {
			for (Profile profile : Profiles.BY_ITEM.values()) {
				if (!"minecraft".equals(BuiltInRegistries.ITEM.getKey(profile.item).getNamespace())) {
					continue;
				}
				context.modify(profile.item, builder -> apply(builder, profile));
			}
		});
	}

	private static void apply(net.minecraft.core.component.DataComponentMap.Builder builder, Profile profile) {
		builder.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemStats.attributes(profile));
		if (profile.shield != null) {
			builder.set(DataComponents.BLOCKS_ATTACKS, ItemStats.blocks(profile.shield));
		} else if (!profile.has(Profile.Trait.AMMO)) {
			builder.set(DataComponents.ATTACK_RANGE, ItemStats.range(profile));
			if (profile.shieldBreak > 0.0f) {
				builder.set(DataComponents.WEAPON, ItemStats.weaponComponent(profile));
			}
		}
		if (profile.replaceDurability && profile.durability > 0) {
			builder.set(DataComponents.MAX_DAMAGE, profile.durability);
		}
	}
}

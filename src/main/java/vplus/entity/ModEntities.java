package vplus.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import vplus.VPlusMod;

public final class ModEntities {
	public static EntityType<ThrownGear> THROWN;

	private ModEntities() {
	}

	public static void register() {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "thrown_gear"));
		THROWN = Registry.register(
				BuiltInRegistries.ENTITY_TYPE,
				key,
				EntityType.Builder.<ThrownGear>of(ThrownGear::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build(key));
	}
}

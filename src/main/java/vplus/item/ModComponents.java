package vplus.item;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import vplus.VPlusMod;

public final class ModComponents {
	public static final DataComponentType<String> SLOT_KIND = register("slot_kind", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<String> AFFIX = register("affix", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<String> TECHNIQUE = register("technique", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<Integer> SOCKETS = register("sockets", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
	public static final DataComponentType<String> GEMS = register("gems", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<String> COSMETIC_MOB = register("cosmetic_mob", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<Integer> COSMETIC_PIECES = register("cosmetic_pieces", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

	private ModComponents() {
	}

	public static void register() {
		SLOT_KIND.toString();
	}

	private static <T> DataComponentType<T> register(String path, DataComponentType<T> type) {
		ResourceKey<DataComponentType<?>> key = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, net.minecraft.resources.Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, path));
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, key, type);
	}
}

package vplus.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StandData(int entityId) {
	public static final StreamCodec<RegistryFriendlyByteBuf, StandData> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			StandData::entityId,
			StandData::new);
}

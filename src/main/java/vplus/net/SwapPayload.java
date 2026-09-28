package vplus.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import vplus.VPlusMod;
import vplus.loadout.LoadoutRules;

public record SwapPayload(int slot) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SwapPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "swap"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwapPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, SwapPayload::slot, SwapPayload::new);

	public static void register() {
		PayloadTypeRegistry.playC2S().register(TYPE, CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> context.server().execute(() -> LoadoutRules.swap(context.player(), payload.slot())));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

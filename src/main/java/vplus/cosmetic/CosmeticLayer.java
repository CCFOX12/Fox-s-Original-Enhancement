package vplus.cosmetic;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import vplus.VPlusMod;

public final class CosmeticLayer {
	public static final String SKIN = "skin";
	public static final String ARMOR = "armor";
	public static final AttachmentType<String> ATTACHMENT = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "cosmetic_layer"),
			builder -> builder.persistent(Codec.STRING).initializer(() -> SKIN).syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all()));

	private CosmeticLayer() {
	}

	public static void register() {
		ATTACHMENT.toString();
		PayloadTypeRegistry.playC2S().register(LayerPayload.TYPE, LayerPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(LayerPayload.TYPE, (payload, context) -> context.server().execute(() -> set(context.player(), payload.armor())));
	}

	public static boolean armor(Player player) {
		return ARMOR.equals(player.getAttachedOrCreate(ATTACHMENT));
	}

	public static void set(Player player, boolean armor) {
		player.setAttached(ATTACHMENT, armor ? ARMOR : SKIN);
	}

	public record LayerPayload(boolean armor) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<LayerPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "cosmetic_layer"));
		public static final StreamCodec<RegistryFriendlyByteBuf, LayerPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, LayerPayload::armor, LayerPayload::new);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
}

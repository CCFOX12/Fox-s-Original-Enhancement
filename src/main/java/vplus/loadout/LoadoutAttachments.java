package vplus.loadout;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import vplus.VPlusMod;

public final class LoadoutAttachments {
	public static final AttachmentType<Loadout> LOADOUT = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "loadout"),
			builder -> builder.persistent(Loadout.CODEC).initializer(Loadout::empty).syncWith(Loadout.STREAM, AttachmentSyncPredicate.all()));

	private LoadoutAttachments() {
	}

	public static void register() {
		LOADOUT.toString();
	}
}

package vplus.cosmetic;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import vplus.VPlusMod;
import vplus.loadout.Loadout;

public final class StandLoadout {
	public static final AttachmentType<Loadout> ATTACHMENT = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "stand_loadout"),
			builder -> builder.persistent(Loadout.CODEC).initializer(Loadout::empty).syncWith(Loadout.STREAM, AttachmentSyncPredicate.all()));

	private StandLoadout() {
	}

	public static void register() {
		ATTACHMENT.toString();
	}

	public static Loadout get(ArmorStand stand) {
		return stand.getAttachedOrCreate(ATTACHMENT);
	}

	public static void set(ArmorStand stand, Loadout loadout) {
		stand.setAttached(ATTACHMENT, loadout);
	}

	public static void drop(ArmorStand stand, ServerLevel level) {
		Loadout loadout = get(stand);
		boolean any = false;
		for (int slot = 0; slot < 8; slot++) {
			ItemStack stack = loadout.get(slot);
			if (!stack.isEmpty()) {
				stand.spawnAtLocation(level, stack.copy());
				any = true;
			}
		}
		if (any) {
			set(stand, Loadout.empty());
		}
	}
}

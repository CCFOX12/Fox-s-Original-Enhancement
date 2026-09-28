package vplus.game;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class CosmeticDrops {
	private CosmeticDrops() {
	}

	public static void roll(LivingEntity entity, DamageSource source) {
		if (!(entity.level() instanceof ServerLevel level)) {
			return;
		}
		Player player = source.getEntity() instanceof Player direct ? direct : null;
		if (player == null) {
			return;
		}
		String mob = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
		float chance = chance(mob);
		if (chance <= 0.0f) {
			return;
		}
		if (player.getRandom().nextFloat() > chance) {
			return;
		}
		String piece = piece(mob, player.getRandom().nextFloat());
		if (piece == null) {
			return;
		}
		var item = BuiltInRegistries.ITEM.get(net.minecraft.resources.Identifier.fromNamespaceAndPath("vplus", piece)).map(net.minecraft.core.Holder::value).orElse(null);
		if (item != null) {
			entity.spawnAtLocation(level, new ItemStack(item));
		}
	}

	private static float chance(String mob) {
		return switch (mob) {
			case "warden" -> 0.18f;
			case "ender_dragon", "wither" -> 0.20f;
			case "zombie", "husk", "drowned", "skeleton", "stray", "bogged", "creeper", "spider", "witch", "pillager", "vindicator", "piglin", "zombified_piglin" -> 0.08f;
			default -> 0.10f;
		};
	}

	private static String piece(String mob, float roll) {
		return switch (mob) {
			case "ender_dragon" -> "dragon_horn";
			case "wither" -> "wither_coat";
			case "enderman", "warden" -> roll < 0.5f ? mob + "_head" : mob + "_coat";
			case "creeper", "spider", "cave_spider", "slime", "magma_cube", "blaze", "ghast", "shulker", "phantom", "guardian", "elder_guardian", "breeze", "creaking", "silverfish", "endermite", "vex", "ravager", "hoglin", "zoglin", "zombie_horse", "camel_husk", "zombie_nautilus" -> mob + "_hide";
			default -> {
				if (roll < 0.30f) {
					yield mob + "_head";
				}
				if (roll < 0.70f) {
					yield mob + "_coat";
				}
				if (roll < 0.90f) {
					yield mob + "_legs";
				}
				yield mob + "_feet";
			}
		};
	}
}

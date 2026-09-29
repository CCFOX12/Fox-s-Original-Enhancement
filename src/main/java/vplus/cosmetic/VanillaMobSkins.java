package vplus.cosmetic;

import java.util.Map;

public final class VanillaMobSkins {
	public enum Mesh {
		HUMANOID,
		SKELETON,
		WITHER_SKELETON
	}

	public record Skin(String texture, String overlay, Mesh mesh) {
	}

	private static final Map<String, Skin> BY_MOB = Map.ofEntries(
			Map.entry("zombie", skin("textures/skins/zombie", null, Mesh.HUMANOID)),
			Map.entry("husk", skin("textures/skins/husk", null, Mesh.HUMANOID)),
			Map.entry("drowned", skin("textures/skins/drowned_outer", null, Mesh.HUMANOID)),
			Map.entry("piglin", skin("textures/skins/piglin", null, Mesh.HUMANOID)),
			Map.entry("zombified_piglin", skin("textures/skins/zombified_piglin", null, Mesh.HUMANOID)),
			Map.entry("piglin_brute", skin("textures/skins/piglin_brute", null, Mesh.HUMANOID)),
			Map.entry("skeleton", skin("textures/skins/skeleton", null, Mesh.SKELETON)),
			Map.entry("stray", skin("textures/skins/stray", "textures/skins/stray_overlay", Mesh.SKELETON)),
			Map.entry("bogged", skin("textures/skins/bogged", null, Mesh.SKELETON)),
			Map.entry("wither_skeleton", skin("textures/skins/wither_skeleton", null, Mesh.WITHER_SKELETON)),
			Map.entry("witch", skin("textures/skins/witch", null, Mesh.HUMANOID)),
			Map.entry("pillager", skin("textures/skins/pillager", null, Mesh.HUMANOID)),
			Map.entry("vindicator", skin("textures/skins/vindicator", null, Mesh.HUMANOID)),
			Map.entry("evoker", skin("textures/skins/evoker", null, Mesh.HUMANOID)),
			Map.entry("enderman", skin("textures/skins/enderman", null, Mesh.HUMANOID)),
			Map.entry("warden", skin("textures/skins/warden", null, Mesh.HUMANOID)),
			Map.entry("ender_dragon", skin("textures/skins/ender_dragon", null, Mesh.HUMANOID)),
			Map.entry("wither", skin("textures/skins/wither", null, Mesh.HUMANOID)),
			Map.entry("creeper", skin("textures/skins/creeper", null, Mesh.HUMANOID)),
			Map.entry("spider", skin("textures/skins/spider", null, Mesh.HUMANOID)),
			Map.entry("cave_spider", skin("textures/skins/cave_spider", null, Mesh.HUMANOID)),
			Map.entry("slime", skin("textures/skins/slime", null, Mesh.HUMANOID)),
			Map.entry("magma_cube", skin("textures/skins/magma_cube", null, Mesh.HUMANOID)),
			Map.entry("blaze", skin("textures/skins/blaze", null, Mesh.HUMANOID)),
			Map.entry("ghast", skin("textures/skins/ghast", null, Mesh.HUMANOID)),
			Map.entry("shulker", skin("textures/skins/shulker", null, Mesh.HUMANOID)),
			Map.entry("phantom", skin("textures/skins/phantom", null, Mesh.HUMANOID)),
			Map.entry("guardian", skin("textures/skins/guardian", null, Mesh.HUMANOID)),
			Map.entry("elder_guardian", skin("textures/skins/elder_guardian", null, Mesh.HUMANOID)),
			Map.entry("breeze", skin("textures/skins/breeze", null, Mesh.HUMANOID)),
			Map.entry("creaking", skin("textures/skins/creaking", null, Mesh.HUMANOID)),
			Map.entry("silverfish", skin("textures/skins/silverfish", null, Mesh.HUMANOID)),
			Map.entry("endermite", skin("textures/skins/endermite", null, Mesh.HUMANOID)),
			Map.entry("vex", skin("textures/skins/vex", null, Mesh.HUMANOID)),
			Map.entry("ravager", skin("textures/skins/ravager", null, Mesh.HUMANOID)),
			Map.entry("hoglin", skin("textures/skins/hoglin", null, Mesh.HUMANOID)),
			Map.entry("zoglin", skin("textures/skins/zoglin", null, Mesh.HUMANOID)),
			Map.entry("zombie_horse", skin("textures/skins/zombie_horse", null, Mesh.HUMANOID)),
			Map.entry("camel_husk", skin("textures/skins/camel_husk", null, Mesh.HUMANOID)),
			Map.entry("zombie_nautilus", skin("textures/skins/zombie_nautilus", null, Mesh.HUMANOID)));

	private VanillaMobSkins() {
	}

	public static Skin get(String mob) {
		return mob == null ? null : BY_MOB.get(mob);
	}

	private static Skin skin(String texture, String overlay, Mesh mesh) {
		return new Skin(texture, overlay, mesh);
	}
}

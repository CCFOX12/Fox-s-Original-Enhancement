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
			Map.entry("drowned", skin("textures/skins/drowned", "textures/skins/drowned_outer", Mesh.HUMANOID)),
			Map.entry("piglin", skin("textures/skins/piglin", null, Mesh.HUMANOID)),
			Map.entry("zombified_piglin", skin("textures/skins/zombified_piglin", null, Mesh.HUMANOID)),
			Map.entry("piglin_brute", skin("textures/skins/piglin_brute", null, Mesh.HUMANOID)),
			Map.entry("skeleton", skin("textures/skins/skeleton", null, Mesh.SKELETON)),
			Map.entry("stray", skin("textures/skins/stray", "textures/skins/stray_overlay", Mesh.SKELETON)),
			Map.entry("bogged", skin("textures/skins/bogged", "textures/skins/bogged_overlay", Mesh.SKELETON)),
			Map.entry("wither_skeleton", skin("textures/skins/wither_skeleton", null, Mesh.WITHER_SKELETON)));

	private VanillaMobSkins() {
	}

	public static Skin get(String mob) {
		return mob == null ? null : BY_MOB.get(mob);
	}

	private static Skin skin(String texture, String overlay, Mesh mesh) {
		return new Skin(texture, overlay, mesh);
	}
}

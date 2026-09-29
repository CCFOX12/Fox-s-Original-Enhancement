package vplus.client.cosmetic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import vplus.VPlusMod;
import vplus.cosmetic.StandLoadout;
import vplus.cosmetic.VanillaMobSkins;
import vplus.item.ModComponents;
import vplus.loadout.Loadout;
import vplus.loadout.LoadoutSlots;

public final class SkinComposite {
	public static final net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey<List<Piece>> PIECES = net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey.create(() -> "vplus_mob_skins");
	public static final net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey<Gear> GEAR = net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey.create(() -> "vplus_worn_gear");
	public static final net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey<List<Piece>> STAND = net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey.create(() -> "vplus_stand_skins");
	public static final net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey<Integer> ENTITY = net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey.create(() -> "vplus_entity_id");
	public static final Identifier STEVE = Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png");
	private static final Identifier CLEAR = Identifier.fromNamespaceAndPath("vplus", "stand_clear");

	private static final int MAGENTA = 0xFFF800F8;
	private static final int BLACK = 0xFF000000;
	private static final Map<Integer, Cached> CACHE = new ConcurrentHashMap<>();
	private static final Map<Identifier, NativeImage> SOURCES = new ConcurrentHashMap<>();
	private static final Map<String, Boolean> COVERS = new ConcurrentHashMap<>();
	private static final java.util.Set<Identifier> MISSING = ConcurrentHashMap.newKeySet();

	private SkinComposite() {
	}

	public static List<Piece> read(Player player) {
		return fromLoadout(Loadout.get(player));
	}

	public static List<Piece> fromLoadout(Loadout loadout) {
		List<Piece> pieces = new ArrayList<>(8);
		add(pieces, loadout, LoadoutSlots.DISPLAY_HEAD, "display_head");
		add(pieces, loadout, LoadoutSlots.DISPLAY_COAT, "display_coat");
		add(pieces, loadout, LoadoutSlots.DISPLAY_LEGS, "display_legs");
		add(pieces, loadout, LoadoutSlots.DISPLAY_FEET, "display_feet");
		add(pieces, loadout, LoadoutSlots.UTILITY_HEAD, "utility_head");
		add(pieces, loadout, LoadoutSlots.UTILITY_COAT, "utility_coat");
		add(pieces, loadout, LoadoutSlots.UTILITY_LEGS, "utility_legs");
		add(pieces, loadout, LoadoutSlots.UTILITY_FEET, "utility_feet");
		return List.copyOf(pieces);
	}

	public static boolean hides(LivingEntity entity, EquipmentSlot slot) {
		Part wanted = partOf(slot);
		if (wanted == null) {
			return false;
		}
		List<Piece> pieces = List.of();
		if (entity instanceof Player player) {
			pieces = read(player);
		} else if (entity instanceof ArmorStand stand) {
			pieces = fromLoadout(StandLoadout.get(stand));
		}
		for (Piece piece : pieces) {
			if (partOf(piece.slot()) == wanted && opaque(piece.mob(), wanted)) {
				return true;
			}
		}
		Piece worn = pieceOf(entity.getItemBySlot(slot));
		return worn != null && partOf(worn.slot()) == wanted && opaque(worn.mob(), wanted);
	}

	public static Identifier texture(AvatarRenderState state, Identifier baseId) {
		Integer entityId = state.getData(ENTITY);
		return cached(entityId == null ? state.id : entityId, baseId, stamps(state.getData(PIECES), state.getData(GEAR)));
	}

	public static Identifier standTexture(HumanoidRenderState state) {
		Integer entityId = state.getData(ENTITY);
		return cached(entityId == null ? 0 : entityId, CLEAR, stamps(state.getData(STAND), state.getData(GEAR)));
	}

	public static StandParts standParts(HumanoidRenderState state) {
		boolean head = false;
		boolean coat = false;
		boolean legs = false;
		for (Stamp stamp : stamps(state.getData(STAND), state.getData(GEAR))) {
			switch (partOf(stamp.kind())) {
				case HEAD -> head = true;
				case COAT -> coat = true;
				case LEGS, FEET -> legs = true;
				case null -> {
				}
			}
		}
		return new StandParts(head, coat, legs);
	}

	private static Identifier cached(int entityId, Identifier baseId, List<Stamp> stamps) {
		if (baseId == null || stamps.isEmpty()) {
			return null;
		}
		List<Stamp> ordered = new ArrayList<>(stamps);
		ordered.sort(Comparator.comparingInt(Stamp::rank));
		String nextKey = key(baseId, ordered);
		Cached cached = CACHE.computeIfAbsent(entityId, Cached::new);
		if (nextKey.equals(cached.key)) {
			return cached.id;
		}
		NativeImage image = compose(baseId, ordered);
		if (image == null) {
			return null;
		}
		if (cached.texture == null) {
			cached.texture = new DynamicTexture(() -> "vplus-skin-" + entityId, image);
			Minecraft.getInstance().getTextureManager().register(cached.id, cached.texture);
			cached.texture.upload();
		} else {
			NativeImage pixels = cached.texture.getPixels();
			if (pixels != null && pixels.getWidth() == image.getWidth() && pixels.getHeight() == image.getHeight()) {
				pixels.copyFrom(image);
				image.close();
			} else {
				cached.texture.setPixels(image);
			}
			cached.texture.upload();
		}
		cached.key = nextKey;
		return cached.id;
	}

	private static String key(Identifier baseId, List<Stamp> stamps) {
		StringBuilder builder = new StringBuilder(baseId.toString());
		for (Stamp stamp : stamps) {
			builder.append('|').append(stamp.rank()).append(':').append(stamp.kind()).append(':').append(stamp.mob());
		}
		return builder.toString();
	}

	private static List<Stamp> stamps(List<Piece> pieces, Gear gear) {
		List<Stamp> stamps = new ArrayList<>();
		if (pieces != null) {
			for (Piece piece : pieces) {
				if (partOf(piece.slot()) != null) {
					stamps.add(new Stamp(piece.mob(), piece.slot(), familyRank(piece.slot())));
				}
			}
		}
		if (gear != null) {
			addArmor(stamps, gear.head());
			addArmor(stamps, gear.chest());
			addArmor(stamps, gear.legs());
			addArmor(stamps, gear.feet());
		}
		return stamps;
	}

	private static void addArmor(List<Stamp> stamps, ItemStack stack) {
		Piece piece = pieceOf(stack);
		if (piece != null) {
			stamps.add(new Stamp(piece.mob(), piece.slot(), 0));
		}
	}

	private static NativeImage compose(Identifier baseId, List<Stamp> stamps) {
		NativeImage image = new NativeImage(64, 64, true);
		if (!CLEAR.equals(baseId)) {
			NativeImage base = source(baseId);
			if (base == null) {
				image.close();
				return null;
			}
			if (base.getWidth() == 64 && base.getHeight() == 64) {
				image.copyFrom(base);
			} else if (base.getWidth() == 64 && base.getHeight() == 32) {
				blit(base, image, 0, 0, 0, 0, 64, 32, false);
				mirrorLimb(base, image, 40, 16, 32, 48, false, false);
				mirrorLimb(base, image, 40, 16, 48, 48, false, false);
				mirrorLimb(base, image, 0, 16, 16, 48, false, false);
				mirrorLimb(base, image, 0, 16, 0, 48, false, false);
			} else {
				image.close();
				return null;
			}
		}
		for (Stamp stamp : stamps) {
			Part part = partOf(stamp.kind());
			if (part == Part.LEGS || part == Part.FEET) {
				continue;
			}
			paint(image, stamp, part);
		}
		for (Stamp stamp : stamps) {
			if (partOf(stamp.kind()) == Part.LEGS) {
				paint(image, stamp, Part.LEGS);
			}
		}
		for (Stamp stamp : stamps) {
			if (partOf(stamp.kind()) == Part.FEET) {
				paint(image, stamp, Part.FEET);
			}
		}
		return image;
	}

	private static void paint(NativeImage dst, Stamp stamp, Part part) {
		VanillaMobSkins.Skin skin = VanillaMobSkins.get(stamp.mob());
		if (skin == null) {
			fillMissing(dst, part);
			return;
		}
		NativeImage mob = source(file(skin.texture()));
		if (mob == null) {
			fillMissing(dst, part);
			return;
		}
		paintSheet(mob, dst, part);
		if (skin.overlay() != null) {
			NativeImage overlay = source(file(skin.overlay()));
			if (overlay != null) {
				paintSheet(overlay, dst, part);
			}
		}
	}

	private static void paintSheet(NativeImage src, NativeImage dst, Part part) {
		if (!anyOpaque(src, part)) {
			fillMissing(dst, part);
			return;
		}
		boolean tall = src.getHeight() >= 64;
		boolean shoes = part == Part.FEET;
		if (part == Part.HEAD) {
			cover(src, dst, 0, 0, 0, 0, 32, 16, false, false);
			if (tall) {
				cover(src, dst, 32, 0, 0, 0, 32, 16, false, false);
			}
			return;
		}
		if (part == Part.COAT) {
			cover(src, dst, 16, 16, 16, 16, 24, 16, false, false);
			cover(src, dst, 40, 16, 40, 16, 16, 16, false, false);
			if (tall && opaqueRect(src, 32, 48, 16, 16, false, false)) {
				cover(src, dst, 32, 48, 32, 48, 16, 16, false, false);
			} else {
				mirrorLimb(src, dst, 40, 16, 32, 48, false, true);
			}
			if (tall) {
				cover(src, dst, 16, 32, 16, 16, 24, 16, false, false);
				cover(src, dst, 40, 32, 40, 16, 16, 16, false, false);
				cover(src, dst, 48, 48, 32, 48, 16, 16, false, false);
			}
			return;
		}
		cover(src, dst, 0, 16, 0, 16, 16, 16, false, shoes);
		if (tall && opaqueRect(src, 16, 48, 16, 16, false, shoes)) {
			cover(src, dst, 16, 48, 16, 48, 16, 16, false, shoes);
		} else {
			mirrorLimb(src, dst, 0, 16, 16, 48, shoes, true);
		}
		if (tall) {
			cover(src, dst, 0, 32, 0, 16, 16, 16, false, shoes);
			cover(src, dst, 0, 48, 16, 48, 16, 16, false, shoes);
		}
	}

	private static void fillMissing(NativeImage dst, Part part) {
		boolean shoes = part == Part.FEET;
		if (part == Part.HEAD) {
			fill(dst, 0, 0, 32, 16, false);
			return;
		}
		if (part == Part.COAT) {
			fill(dst, 16, 16, 24, 16, false);
			fill(dst, 40, 16, 16, 16, false);
			fill(dst, 32, 48, 16, 16, false);
			return;
		}
		fill(dst, 0, 16, 16, 16, shoes);
		fill(dst, 16, 48, 16, 16, shoes);
	}

	private static void fill(NativeImage dst, int x, int y, int width, int height, boolean shoes) {
		for (int dy = 0; dy < height; dy++) {
			for (int dx = 0; dx < width; dx++) {
				if (shoes && !shoePixel(dx, dy)) {
					continue;
				}
				int px = x + dx;
				int py = y + dy;
				boolean magenta = (((px >> 2) ^ (py >> 2)) & 1) == 0;
				dst.setPixel(px, py, magenta ? MAGENTA : BLACK);
				clearOverlay(dst, px, py);
			}
		}
	}

	private static void cover(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, int width, int height, boolean flipX, boolean shoes) {
		if (sx < 0 || sy < 0 || sx + width > src.getWidth() || sy + height > src.getHeight()) {
			return;
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (shoes && !shoePixel(x, y)) {
					continue;
				}
				int color = src.getPixel(sx + (flipX ? width - 1 - x : x), sy + y);
				if ((color >>> 24) == 0) {
					continue;
				}
				int px = dx + x;
				int py = dy + y;
				dst.setPixel(px, py, color);
				clearOverlay(dst, px, py);
			}
		}
	}

	private static void mirrorLimb(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, boolean shoes, boolean cover) {
		if (!shoes) {
			copyFace(src, dst, sx + 4, sy, dx + 4, dy, 4, 4, dx, dy, shoes, cover);
		}
		copyFace(src, dst, sx + 8, sy, dx + 8, dy, 4, 4, dx, dy, shoes, cover);
		copyFace(src, dst, sx, sy + 4, dx + 8, dy + 4, 4, 12, dx, dy, shoes, cover);
		copyFace(src, dst, sx + 4, sy + 4, dx + 4, dy + 4, 4, 12, dx, dy, shoes, cover);
		copyFace(src, dst, sx + 8, sy + 4, dx, dy + 4, 4, 12, dx, dy, shoes, cover);
		copyFace(src, dst, sx + 12, sy + 4, dx + 12, dy + 4, 4, 12, dx, dy, shoes, cover);
	}

	private static void copyFace(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, int width, int height, int tileX, int tileY, boolean shoes, boolean cover) {
		if (sx < 0 || sy < 0 || sx + width > src.getWidth() || sy + height > src.getHeight()) {
			return;
		}
		if (dx < 0 || dy < 0 || dx + width > dst.getWidth() || dy + height > dst.getHeight()) {
			return;
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int px = dx + x;
				int py = dy + y;
				if (shoes && !shoePixel(px - tileX, py - tileY)) {
					continue;
				}
				int color = src.getPixel(sx + width - 1 - x, sy + y);
				if ((color >>> 24) == 0) {
					continue;
				}
				dst.setPixel(px, py, color);
				if (cover) {
					clearOverlay(dst, px, py);
				}
			}
		}
	}

	private static boolean shoePixel(int localX, int localY) {
		if (localY <= 3 && localX >= 8 && localX <= 11) {
			return true;
		}
		return localY >= 12;
	}

	private static void clearOverlay(NativeImage dst, int x, int y) {
		int ox = -1;
		int oy = -1;
		if (y < 16 && x < 32) {
			ox = x + 32;
			oy = y;
		} else if (y >= 16 && y < 32 && x >= 16 && x < 40) {
			ox = x;
			oy = y + 16;
		} else if (y >= 16 && y < 32 && x >= 40 && x < 56) {
			ox = x;
			oy = y + 16;
		} else if (y >= 48 && x >= 32 && x < 48) {
			ox = x + 16;
			oy = y;
		} else if (y >= 16 && y < 32 && x < 16) {
			ox = x;
			oy = y + 16;
		} else if (y >= 48 && x >= 16 && x < 32) {
			ox = x - 16;
			oy = y;
		}
		if (ox >= 0 && oy >= 0 && ox < dst.getWidth() && oy < dst.getHeight()) {
			dst.setPixel(ox, oy, 0);
		}
	}

	private static void blit(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, int width, int height, boolean flipX) {
		if (sx < 0 || sy < 0 || sx + width > src.getWidth() || sy + height > src.getHeight()) {
			return;
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				dst.setPixel(dx + x, dy + y, src.getPixel(sx + (flipX ? width - 1 - x : x), sy + y));
			}
		}
	}

	private static boolean opaque(String mob, Part part) {
		String key = (mob == null ? "" : mob) + "\0" + part.name();
		Boolean cached = COVERS.get(key);
		if (cached != null) {
			return cached;
		}
		Boolean scanned = scan(mob, part);
		if (scanned == null) {
			return true;
		}
		COVERS.put(key, scanned);
		return scanned;
	}

	private static Boolean scan(String mob, Part part) {
		VanillaMobSkins.Skin skin = VanillaMobSkins.get(mob);
		if (skin == null) {
			return true;
		}
		NativeImage base = source(file(skin.texture()));
		if (base == null) {
			return null;
		}
		if (anyOpaque(base, part)) {
			return true;
		}
		if (skin.overlay() == null) {
			return false;
		}
		NativeImage overlay = source(file(skin.overlay()));
		if (overlay == null) {
			return null;
		}
		return anyOpaque(overlay, part);
	}

	private static boolean anyOpaque(NativeImage src, Part part) {
		boolean tall = src.getHeight() >= 64;
		boolean shoes = part == Part.FEET;
		if (part == Part.HEAD) {
			return opaqueRect(src, 0, 0, 32, 16, false, false) || (tall && opaqueRect(src, 32, 0, 32, 16, false, false));
		}
		if (part == Part.COAT) {
			if (opaqueRect(src, 16, 16, 24, 16, false, false) || opaqueRect(src, 40, 16, 16, 16, false, false)) {
				return true;
			}
			if (tall) {
				return opaqueRect(src, 32, 48, 16, 16, false, false)
						|| opaqueRect(src, 16, 32, 24, 16, false, false)
						|| opaqueRect(src, 40, 32, 16, 16, false, false)
						|| opaqueRect(src, 48, 48, 16, 16, false, false);
			}
			return opaqueRect(src, 40, 16, 16, 16, true, false);
		}
		if (opaqueRect(src, 0, 16, 16, 16, false, shoes)) {
			return true;
		}
		if (tall) {
			return opaqueRect(src, 16, 48, 16, 16, false, shoes)
					|| opaqueRect(src, 0, 32, 16, 16, false, shoes)
					|| opaqueRect(src, 0, 48, 16, 16, false, shoes);
		}
		return opaqueRect(src, 0, 16, 16, 16, true, shoes);
	}

	private static boolean opaqueRect(NativeImage src, int sx, int sy, int width, int height, boolean flipX, boolean shoes) {
		if (sx < 0 || sy < 0 || sx + width > src.getWidth() || sy + height > src.getHeight()) {
			return false;
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (shoes && !shoePixel(x, y)) {
					continue;
				}
				int color = src.getPixel(sx + (flipX ? width - 1 - x : x), sy + y);
				if ((color >>> 24) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static Identifier file(String path) {
		if (!path.endsWith(".png")) {
			path = path + ".png";
		}
		return Identifier.fromNamespaceAndPath("vplus", path);
	}

	private static NativeImage source(Identifier id) {
		NativeImage cached = SOURCES.get(id);
		if (cached != null) {
			return cached;
		}
		NativeImage loaded = loadAny(id);
		if (loaded == null) {
			if (MISSING.add(id)) {
				VPlusMod.LOGGER.warn("装扮贴图读不到 {}", id);
			}
			return null;
		}
		SOURCES.put(id, loaded);
		return loaded;
	}

	private static NativeImage loadAny(Identifier id) {
		NativeImage image = readResource(id);
		if (image == null && !id.getPath().endsWith(".png")) {
			image = readResource(id.withSuffix(".png"));
		}
		if (image == null && !id.getPath().startsWith("textures/")) {
			Identifier prefixed = id.withPrefix("textures/");
			image = readResource(prefixed);
			if (image == null && !prefixed.getPath().endsWith(".png")) {
				image = readResource(prefixed.withSuffix(".png"));
			}
		}
		if (image == null) {
			image = copyDynamic(id);
		}
		return image;
	}

	private static NativeImage readResource(Identifier id) {
		Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(id);
		if (resource.isEmpty()) {
			return null;
		}
		try (InputStream stream = resource.get().open()) {
			return NativeImage.read(stream);
		} catch (IOException ignored) {
			return null;
		}
	}

	private static NativeImage copyDynamic(Identifier id) {
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(id);
		if (!(texture instanceof DynamicTexture dynamic)) {
			return null;
		}
		NativeImage pixels = dynamic.getPixels();
		if (pixels == null) {
			return null;
		}
		NativeImage copy = new NativeImage(pixels.getWidth(), pixels.getHeight(), true);
		copy.copyFrom(pixels);
		return copy;
	}

	private static Piece pieceOf(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return null;
		}
		String kind = stack.get(ModComponents.SLOT_KIND);
		if (partOf(kind) == null) {
			return null;
		}
		String mob = stack.get(ModComponents.GEMS);
		return new Piece(mob == null ? "" : mob, kind);
	}

	private static void add(List<Piece> pieces, Loadout loadout, int slot, String kind) {
		ItemStack stack = loadout.get(slot);
		if (stack.isEmpty() || !kind.equals(stack.get(ModComponents.SLOT_KIND))) {
			return;
		}
		String mob = stack.get(ModComponents.GEMS);
		pieces.add(new Piece(mob == null ? "" : mob, kind));
	}

	private static int familyRank(String kind) {
		if (kind.startsWith("display_")) {
			return 2;
		}
		if (kind.startsWith("utility_")) {
			return 1;
		}
		return 0;
	}

	private static Part partOf(String kind) {
		if (kind == null) {
			return null;
		}
		if (kind.endsWith("_head")) {
			return Part.HEAD;
		}
		if (kind.endsWith("_coat")) {
			return Part.COAT;
		}
		if (kind.endsWith("_legs")) {
			return Part.LEGS;
		}
		if (kind.endsWith("_feet")) {
			return Part.FEET;
		}
		return null;
	}

	private static Part partOf(EquipmentSlot slot) {
		return switch (slot) {
			case HEAD -> Part.HEAD;
			case CHEST -> Part.COAT;
			case LEGS -> Part.LEGS;
			case FEET -> Part.FEET;
			default -> null;
		};
	}

	private enum Part {
		HEAD,
		COAT,
		LEGS,
		FEET
	}

	private record Stamp(String mob, String kind, int rank) {
	}

	public record Piece(String mob, String slot) {
	}

	public record StandParts(boolean head, boolean coat, boolean legs) {
	}

	public record Gear(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
	}

	private static final class Cached {
		private final Identifier id;
		private DynamicTexture texture;
		private String key = "";

		private Cached(int entityId) {
			this.id = Identifier.fromNamespaceAndPath("vplus", "skin_overlay/" + entityId);
		}
	}
}

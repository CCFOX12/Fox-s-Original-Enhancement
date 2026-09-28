package vplus.affix;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.entity.player.Player;

public final class Cooldown {
	private static final Map<UUID, Map<String, Long>> READY = new ConcurrentHashMap<>();

	private Cooldown() {
	}

	public static boolean ready(Player player, String id) {
		Map<String, Long> map = READY.get(player.getUUID());
		if (map == null) {
			return true;
		}
		Long at = map.get(id);
		return at == null || player.level().getGameTime() >= at;
	}

	public static void start(Player player, String id, long ticks) {
		READY.computeIfAbsent(player.getUUID(), key -> new ConcurrentHashMap<>()).put(id, player.level().getGameTime() + ticks);
	}
}

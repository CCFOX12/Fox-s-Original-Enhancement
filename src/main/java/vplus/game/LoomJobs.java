package vplus.game;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import vplus.item.ModComponents;

public final class LoomJobs {
	private static final java.util.Map<java.util.UUID, Long> READY = new java.util.concurrent.ConcurrentHashMap<>();
	private static final java.util.Map<java.util.UUID, String> PATTERN = new java.util.concurrent.ConcurrentHashMap<>();

	private LoomJobs() {
	}

	public static void register() {
		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
			if (!world.getBlockState(hit.getBlockPos()).is(Blocks.LOOM)) {
				return InteractionResult.PASS;
			}
			ItemStack stack = player.getItemInHand(hand);
			if (!"pattern".equals(stack.get(ModComponents.SLOT_KIND)) || !player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}
			if (!world.isClientSide()) {
				READY.put(player.getUUID(), world.getGameTime() + 40L);
				PATTERN.put(player.getUUID(), BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
			}
			return InteractionResult.SUCCESS;
		});
	}

	public static void tick(Player player) {
		Long ready = READY.get(player.getUUID());
		if (ready == null || player.level().getGameTime() < ready || !(player.level() instanceof ServerLevel level)) {
			return;
		}
		String pattern = PATTERN.remove(player.getUUID());
		READY.remove(player.getUUID());
		if (pattern == null || !pattern.endsWith("_pattern")) {
			return;
		}
		boolean shepherd = pattern.startsWith("shepherd");
		int wool = 0;
		for (int i = 0; i < Math.min(36, player.getInventory().getContainerSize()); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(Items.WHITE_WOOL) || (shepherd && stack.is(net.minecraft.tags.ItemTags.WOOL))) {
				wool += stack.getCount();
			}
		}
		ItemStack patternStack = findPattern(player, pattern);
		if (patternStack.isEmpty() || wool < 4) {
			return;
		}
		int left = 4;
		for (int i = 0; i < Math.min(36, player.getInventory().getContainerSize()) && left > 0; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			boolean match = shepherd ? stack.is(net.minecraft.tags.ItemTags.WOOL) : stack.is(Items.WHITE_WOOL);
			if (!match) {
				continue;
			}
			int take = Math.min(left, stack.getCount());
			stack.shrink(take);
			left -= take;
		}
		patternStack.shrink(1);
		String cloth = pattern.substring(0, pattern.length() - "_pattern".length()) + "_cloth";
		Item clothItem = BuiltInRegistries.ITEM.get(net.minecraft.resources.Identifier.fromNamespaceAndPath("vplus", cloth)).map(net.minecraft.core.Holder::value).orElse(Items.AIR);
		if (clothItem != Items.AIR) {
			player.spawnAtLocation(level, new ItemStack(clothItem));
		}
	}

	private static ItemStack findPattern(Player player, String path) {
		for (int i = 0; i < Math.min(36, player.getInventory().getContainerSize()); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (path.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath())) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}

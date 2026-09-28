package vplus.loadout;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record Loadout(List<ItemStack> stacks) {
	public static final int SIZE = 26;
	public static final Codec<Loadout> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(Loadout::from, Loadout::stacks);
	public static final StreamCodec<RegistryFriendlyByteBuf, Loadout> STREAM = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(Loadout::from, Loadout::stacks);

	public static Loadout empty() {
		return from(List.of());
	}

	public static Loadout from(List<ItemStack> incoming) {
		List<ItemStack> stacks = new ArrayList<>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			ItemStack stack = i < incoming.size() ? incoming.get(i) : ItemStack.EMPTY;
			stacks.add(stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
		}
		return new Loadout(List.copyOf(stacks));
	}

	public ItemStack get(int slot) {
		if (slot < 0 || slot >= SIZE) {
			return ItemStack.EMPTY;
		}
		return this.stacks.get(slot);
	}

	public Loadout with(int slot, ItemStack stack) {
		List<ItemStack> next = new ArrayList<>(this.stacks);
		next.set(slot, stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
		return new Loadout(List.copyOf(next));
	}

	public static Loadout get(Player player) {
		return player.getAttachedOrCreate(LoadoutAttachments.LOADOUT);
	}

	public static void set(Player player, Loadout loadout) {
		player.setAttached(LoadoutAttachments.LOADOUT, loadout);
	}

	public void dropAll(Player player) {
		if (!(player.level() instanceof net.minecraft.server.level.ServerLevel level)) {
			return;
		}
		for (ItemStack stack : this.stacks) {
			if (!stack.isEmpty()) {
				player.spawnAtLocation(level, stack.copy());
			}
		}
		set(player, empty());
	}
}

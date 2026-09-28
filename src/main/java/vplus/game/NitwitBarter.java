package vplus.game;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import vplus.item.ModGear;

public final class NitwitBarter {
	private static final Map<UUID, Long> DONE = new ConcurrentHashMap<>();

	private NitwitBarter() {
	}

	public static void tick(Villager villager) {
		if (villager.isBaby() || villager.level().isClientSide() || !villager.getVillagerData().profession().is(VillagerProfession.NITWIT)) {
			return;
		}
		if (SillyCosmetics.ITEMS.isEmpty()) {
			return;
		}
		Long ready = DONE.get(villager.getUUID());
		if (ready != null && villager.level().getGameTime() < ready) {
			if (villager.hurtTime > 0) {
				villager.spawnAtLocation((ServerLevel) villager.level(), new ItemStack(ModGear.TASTY_BREAD));
				DONE.remove(villager.getUUID());
			}
			return;
		}
		if (ready != null) {
			ItemStack reward = new ItemStack(SillyCosmetics.ITEMS.get(villager.getRandom().nextInt(SillyCosmetics.ITEMS.size())));
			villager.spawnAtLocation((ServerLevel) villager.level(), reward);
			DONE.remove(villager.getUUID());
		}
		for (ItemEntity entity : villager.level().getEntitiesOfClass(ItemEntity.class, villager.getBoundingBox().inflate(4.0))) {
			if (entity.getItem().is(ModGear.TASTY_BREAD)) {
				ItemStack stack = entity.getItem();
				stack.shrink(1);
				if (stack.isEmpty()) {
					entity.discard();
				}
				DONE.put(villager.getUUID(), villager.level().getGameTime() + 120L);
				break;
			}
		}
	}
}

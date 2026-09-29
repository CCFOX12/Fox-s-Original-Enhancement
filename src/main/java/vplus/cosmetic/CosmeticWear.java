package vplus.cosmetic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import vplus.item.ModComponents;
import vplus.loadout.Loadout;

public final class CosmeticWear {
	private CosmeticWear() {
	}

	public static void register() {
		net.fabricmc.fabric.api.event.player.UseItemCallback.EVENT.register(CosmeticWear::use);
		net.fabricmc.fabric.api.event.player.UseEntityCallback.EVENT.register(CosmeticWear::useStand);
		CosmeticDispense.register();
	}

	public static InteractionResult use(Player player, Level level, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isSpectator() || !wearable(stack)) {
			return InteractionResult.PASS;
		}
		equip(player, stack);
		return InteractionResult.SUCCESS;
	}

	public static InteractionResult useStand(Player player, Level level, InteractionHand hand, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.EntityHitResult hit) {
		if (!(entity instanceof ArmorStand stand) || player.isSpectator() || player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		if (player instanceof net.minecraft.server.level.ServerPlayer server) {
			server.openMenu(new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory<vplus.menu.StandData>() {
				@Override
				public vplus.menu.StandData getScreenOpeningData(net.minecraft.server.level.ServerPlayer opening) {
					return new vplus.menu.StandData(stand.getId());
				}

				@Override
				public net.minecraft.network.chat.Component getDisplayName() {
					return net.minecraft.network.chat.Component.translatable("container.vplus.stand");
				}

				@Override
				public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inventory, Player opening) {
					return new vplus.menu.StandMenu(id, inventory, stand);
				}
			});
		}
		return InteractionResult.SUCCESS;
	}

	public static boolean equip(Player player, ItemStack stack) {
		String kind = stack.get(ModComponents.SLOT_KIND);
		int slot = loadoutIndex(kind);
		EquipmentSlot armor = armorSlot(kind);
		if (slot < 0 || armor == null) {
			return false;
		}
		if (Loadout.get(player).get(slot).isEmpty()) {
			placeLoadout(player, slot, takeOne(player, stack));
			play(player);
			return true;
		}
		placeArmor(player, armor, takeOne(player, stack));
		play(player);
		return true;
	}

	public static boolean equip(ArmorStand stand, ItemStack stack) {
		return equip(stand, stack, null);
	}

	public static boolean equip(ArmorStand stand, ItemStack stack, Player player) {
		String kind = stack.get(ModComponents.SLOT_KIND);
		int slot = loadoutIndex(kind);
		EquipmentSlot armor = armorSlot(kind);
		if (slot >= 0 && armor != null) {
			if (StandLoadout.get(stand).get(slot).isEmpty()) {
				placeLoadout(stand, slot, takeOne(stack));
				play(stand);
				return true;
			}
			placeArmor(stand, armor, takeOne(stack), player);
			play(stand);
			return true;
		}
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null || armorSlot(equippable.slot()) == null) {
			return false;
		}
		placeArmor(stand, equippable.slot(), takeOne(stack), player);
		play(stand);
		return true;
	}

	private static void placeLoadout(Player player, int slot, ItemStack stack) {
		Loadout.set(player, Loadout.get(player).with(slot, stack));
	}

	private static void placeLoadout(ArmorStand stand, int slot, ItemStack stack) {
		StandLoadout.set(stand, StandLoadout.get(stand).with(slot, stack));
	}

	private static void placeArmor(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
		placeArmor(entity, slot, stack, entity instanceof Player player ? player : null);
	}

	private static void placeArmor(LivingEntity entity, EquipmentSlot slot, ItemStack stack, Player recipient) {
		ItemStack previous = entity.getItemBySlot(slot);
		entity.setItemSlot(slot, stack);
		if (previous.isEmpty()) {
			return;
		}
		if (recipient != null) {
			give(recipient, previous);
			return;
		}
		if (entity instanceof ArmorStand stand && entity.level() instanceof net.minecraft.server.level.ServerLevel level) {
			stand.spawnAtLocation(level, previous);
		}
	}

	public static void give(Player player, ItemStack stack) {
		if (stack.isEmpty()) {
			return;
		}
		if (!player.getInventory().add(stack) && !player.level().isClientSide()) {
			player.drop(stack, false);
		}
	}

	private static ItemStack takeOne(Player player, ItemStack stack) {
		ItemStack one = stack.copyWithCount(1);
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return one;
	}

	private static ItemStack takeOne(ItemStack stack) {
		ItemStack one = stack.copyWithCount(1);
		stack.shrink(1);
		return one;
	}

	private static void play(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			return;
		}
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
	}

	public static boolean wearable(ItemStack stack) {
		return loadoutIndex(stack.get(ModComponents.SLOT_KIND)) >= 0;
	}

	public static int loadoutIndex(String kind) {
		return switch (kind) {
			case "display_head" -> 0;
			case "display_coat", "display_set" -> 1;
			case "display_legs" -> 2;
			case "display_feet" -> 3;
			case "utility_head" -> 4;
			case "utility_coat" -> 5;
			case "utility_legs" -> 6;
			case "utility_feet" -> 7;
			case null, default -> -1;
		};
	}

	public static EquipmentSlot armorSlot(String kind) {
		return switch (kind) {
			case "display_head", "utility_head" -> EquipmentSlot.HEAD;
			case "display_coat", "utility_coat", "display_set" -> EquipmentSlot.CHEST;
			case "display_legs", "utility_legs" -> EquipmentSlot.LEGS;
			case "display_feet", "utility_feet" -> EquipmentSlot.FEET;
			case null, default -> null;
		};
	}

	private static EquipmentSlot armorSlot(EquipmentSlot slot) {
		return switch (slot) {
			case HEAD, CHEST, LEGS, FEET -> slot;
			default -> null;
		};
	}
}

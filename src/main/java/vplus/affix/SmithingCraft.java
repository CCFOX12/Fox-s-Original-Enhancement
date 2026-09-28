package vplus.affix;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import vplus.item.ModComponents;
import vplus.item.ModTech;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public final class SmithingCraft {
	private SmithingCraft() {
	}

	public static void apply(SmithingMenu menu) {
		ItemStack template = menu.getSlot(SmithingMenu.TEMPLATE_SLOT).getItem();
		ItemStack base = menu.getSlot(SmithingMenu.BASE_SLOT).getItem();
		ItemStack addition = menu.getSlot(SmithingMenu.ADDITIONAL_SLOT).getItem();
		if (base.isEmpty() || Profiles.of(base.getItem()) == null) {
			return;
		}
		ItemStack result = ItemStack.EMPTY;
		if (ModTech.isTechnique(template) && addition.isEmpty() && allows(template, base)) {
			result = base.copy();
			result.set(ModComponents.TECHNIQUE, ModTech.idOf(template));
		} else if (ModTech.isMold(template) && addition.is(net.minecraft.world.item.Items.COPPER_INGOT)) {
			int sockets = ModTech.socketsOf(template, base);
			if (sockets > 0) {
				result = base.copy();
				result.set(ModComponents.SOCKETS, sockets);
			}
		} else if (template.isEmpty() && ModTech.isGem(addition)) {
			Integer sockets = base.get(ModComponents.SOCKETS);
			String gems = base.get(ModComponents.GEMS);
			int filled = gems == null || gems.isEmpty() ? 0 : gems.split(",").length;
			if (sockets != null && filled < sockets) {
				result = base.copy();
				result.set(ModComponents.GEMS, filled == 0 ? gemName(addition) : gems + "," + gemName(addition));
			}
		}
		if (!result.isEmpty()) {
			menu.getSlot(SmithingMenu.RESULT_SLOT).set(result);
		}
	}

	public static boolean isOurs(ItemStack stack) {
		return stack.get(ModComponents.TECHNIQUE) != null || stack.get(ModComponents.SOCKETS) != null;
	}

	private static boolean allows(ItemStack template, ItemStack base) {
		String id = ModTech.idOf(template);
		Profile profile = Profiles.of(base.getItem());
		if (profile == null || id == null) {
			return false;
		}
		if ((id.equals(Techniques.BASH) || id.equals(Techniques.BURST)) && profile.shield != null) {
			return true;
		}
		String group = profile.affixGroup;
		return switch (id) {
			case Techniques.FAN -> group.equals("sword") || group.equals("axe") || group.equals("katana") || group.equals("hoe");
			case Techniques.LUNGE -> group.equals("rapier") || group.equals("shortsword") || group.equals("pickaxe");
			case Techniques.STOMP -> group.equals("light_blunt") || group.equals("heavy_blunt") || group.equals("flail") || group.equals("gauntlet");
			default -> false;
		};
	}

	private static String gemName(ItemStack stack) {
		return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
	}

	public static void clearGrindstone(ItemStack stack) {
		stack.remove(ModComponents.AFFIX);
		stack.remove(ModComponents.TECHNIQUE);
		stack.remove(ModComponents.GEMS);
	}

	public static void remember(Player player) {
	}
}

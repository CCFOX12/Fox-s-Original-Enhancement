package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vplus.affix.Affix;
import vplus.item.ItemText;
import vplus.item.ModComponents;
import vplus.weapon.Profiles;

@Mixin(Item.class)
public abstract class ItemCraftMixin {
	@Inject(method = "onCraftedBy", at = @At("HEAD"))
	private void vplus$factory(ItemStack stack, Player player, CallbackInfo ci) {
		Affix.factory(stack);
	}

	@Inject(method = "appendHoverText", at = @At("HEAD"))
	private void vplus$tooltip(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<net.minecraft.network.chat.Component> lines, net.minecraft.world.item.TooltipFlag flag, CallbackInfo ci) {
		ItemText.appendDescription(stack, lines);
		Affix affix = Affix.of(stack);
		if (affix != null) {
			lines.accept(affix.line());
		}
		String technique = stack.get(ModComponents.TECHNIQUE);
		if (technique != null) {
			lines.accept(net.minecraft.network.chat.Component.literal("技巧模板 · " + vplus.affix.Techniques.name(technique)));
		}
		if (Profiles.of(stack.getItem()) == null) {
			return;
		}
		Integer sockets = stack.get(ModComponents.SOCKETS);
		if (sockets != null && sockets > 0) {
			String gems = stack.get(ModComponents.GEMS);
			lines.accept(net.minecraft.network.chat.Component.literal(gems == null || gems.isEmpty() ? "镶嵌槽 □".repeat(Math.min(sockets, 1)) + (sockets > 1 ? " □" : "") : "镶嵌 · " + gems));
		}
	}
}

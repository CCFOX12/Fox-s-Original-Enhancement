package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import vplus.combat.HitContext;
import vplus.loadout.Pauldron;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

@Mixin(BlocksAttacks.class)
public abstract class ShieldBreakMixin {
	@ModifyVariable(method = "disable", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float vplus$seconds(float seconds, ServerLevel level, LivingEntity user, float ignored, ItemStack stack) {
		Profile shield = Profiles.of(stack.getItem());
		float shieldTime = shield != null && shield.shield != null ? shield.shield.disableSeconds() : seconds;
		Player attacker = HitContext.ATTACKER.get();
		if (attacker != null) {
			Profile weapon = Profiles.of(attacker.getMainHandItem().getItem());
			if (weapon != null && "great_hammer".equals(weapon.id) && shield != null && shield.shield != null && shield.shield.armor() >= 4) {
				shieldTime = 10.0f;
			}
		}
		float result = Math.max(seconds, shieldTime);
		if (user instanceof Player player) {
			result -= Pauldron.breakReduction(player);
		}
		return Math.max(1.0f, result);
	}
}

package vplus.affix;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import vplus.combat.CombatMemory;
import vplus.weapon.DamageKind;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public final class Burst {
	private Burst() {
	}

	public static void tryBurst(Player player) {
		ItemStack shield = player.getUseItem();
		if (!Techniques.BURST.equals(Techniques.of(shield)) || !Cooldown.ready(player, Techniques.BURST)) {
			return;
		}
		Profile profile = Profiles.of(shield.getItem());
		if (profile == null || profile.shield == null || !(player.level() instanceof ServerLevel level)) {
			return;
		}
		Cooldown.start(player, Techniques.BURST, 60L);
		float radius = 1.4f + profile.shield.armor() * 0.4f;
		Vec3 center = player.position();
		level.sendParticles(ParticleTypes.GUST, center.x, center.y + 1.0, center.z, 8, 0.4, 0.2, 0.4, 0.0);
		for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius), entity -> entity != player)) {
			CombatMemory.hurt(player, living, 2.0f, DamageKind.BLUNT);
			living.push(living.getX() - player.getX(), 0.2, living.getZ() - player.getZ());
		}
		shield.hurtAndBreak(1, player, player.getUsedItemHand());
	}
}

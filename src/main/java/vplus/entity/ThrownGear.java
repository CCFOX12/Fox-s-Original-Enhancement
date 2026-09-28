package vplus.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import vplus.combat.HitContext;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public class ThrownGear extends ThrowableItemProjectile {
	private Vec3 origin = Vec3.ZERO;
	private boolean returning;

	public ThrownGear(EntityType<? extends ThrownGear> type, Level level) {
		super(type, level);
	}

	public ThrownGear(Level level, LivingEntity owner, ItemStack stack) {
		super(ModEntities.THROWN, owner, level, stack);
		this.origin = owner.position();
	}

	public void shootFrom(Player player) {
		this.origin = player.position();
		this.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.6f, 1.0f);
	}

	@Override
	protected net.minecraft.world.item.Item getDefaultItem() {
		return net.minecraft.world.item.Items.IRON_NUGGET;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.returning && this.getOwner() instanceof LivingEntity owner) {
			Vec3 toward = owner.getEyePosition().subtract(this.position());
			this.setDeltaMovement(toward.normalize().scale(0.6));
			if (this.distanceToSqr(owner) < 1.0) {
				this.giveBack(owner);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (this.level().isClientSide() || !(this.getOwner() instanceof Player player)) {
			super.onHitEntity(result);
			return;
		}
		Profile profile = Profiles.of(this.getItem().getItem());
		if (profile == null || !(result.getEntity() instanceof LivingEntity living) || !(this.level() instanceof ServerLevel level)) {
			this.discard();
			return;
		}
		double distance = this.origin.distanceTo(living.position());
		if (profile.throwFar > 0.0f && distance > profile.throwFar) {
			this.finish(player, profile, false);
			return;
		}
		float damage = profile.oneDamage;
		if (profile.throwNear > 0.0f && distance > profile.throwNear) {
			damage *= 0.75f;
		}
		HitContext.KIND.set(profile.damageKind);
		try {
			living.hurtServer(level, this.damageSources().playerAttack(player), damage);
		} finally {
			HitContext.KIND.remove();
		}
		this.finish(player, profile, true);
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (this.level().isClientSide()) {
			return;
		}
		Profile profile = Profiles.of(this.getItem().getItem());
		if (this.getOwner() instanceof Player player && profile != null) {
			this.finish(player, profile, false);
		} else {
			this.discard();
		}
	}

	private void finish(Player player, Profile profile, boolean hit) {
		if (loyal()) {
			this.returning = true;
			return;
		}
		if ("throwing_knife".equals(profile.id)) {
			this.discard();
			return;
		}
		if ("throwing_axe".equals(profile.id) && !hit) {
			this.spawnAtLocation((ServerLevel) this.level(), this.getItem().copy());
			this.discard();
			return;
		}
		if (!hit) {
			this.spawnAtLocation((ServerLevel) this.level(), this.getItem().copy());
		}
		this.discard();
	}

	private void giveBack(LivingEntity owner) {
		if (owner instanceof Player player && !player.getInventory().add(this.getItem().copy())) {
			player.drop(this.getItem().copy(), false);
		}
		this.discard();
	}

	private boolean loyal() {
		ItemEnchantments enchantments = this.getItem().getEnchantments();
		return enchantments != null && !enchantments.isEmpty() && this.getItem().getEnchantments().toString().contains("loyalty");
	}
}

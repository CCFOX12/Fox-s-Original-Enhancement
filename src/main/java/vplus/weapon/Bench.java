package vplus.weapon;

import net.minecraft.world.item.Items;
import vplus.VPlusMod;

public final class Bench {
	private Bench() {
	}

	public static void log() {
		expect("铁剑单手", Items.IRON_SWORD, false, false, 9.6);
		expect("铁剑双手", Items.IRON_SWORD, true, false, 11.2);
		expect("铁镐有甲", Items.IRON_PICKAXE, false, true, 11.5);
		expect("铁镐无甲", Items.IRON_PICKAXE, false, false, 8.6);
		expect("铁锹有甲", Items.IRON_SHOVEL, false, true, 10.0);
		expect("铁锹无甲", Items.IRON_SHOVEL, false, false, 6.8);
		expect("大铁锤无甲", Items.AIR, false, false, 10.3);
	}

	private static void expect(String label, net.minecraft.world.item.Item item, boolean twoHand, boolean armored, double dps) {
		Profile profile = item == Items.AIR ? greatHammer() : Profiles.of(item);
		if (profile == null) {
			VPlusMod.LOGGER.error("对照缺少 {}", label);
			return;
		}
		float damage = twoHand && profile.twoDamage > 0.0f ? profile.twoDamage : profile.oneDamage;
		float speed = twoHand && profile.twoSpeed > 0.0f ? profile.twoSpeed : profile.oneSpeed;
		float factor = profile.damageKind.factor(armored, false);
		double actual = Math.round(damage * factor * speed * 10.0) / 10.0;
		if (Math.abs(actual - dps) > 0.05) {
			VPlusMod.LOGGER.error("对照不符 {} 期望 {} 实际 {}", label, dps, actual);
		} else {
			VPlusMod.LOGGER.info("对照 {} = {}", label, actual);
		}
	}

	private static Profile greatHammer() {
		for (Profile profile : Profiles.BY_ITEM.values()) {
			if ("great_hammer".equals(profile.id) && profile.oneDamage == 11.0f) {
				return profile;
			}
		}
		return null;
	}
}

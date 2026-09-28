package vplus.weapon;

public final class MaterialMath {
	private static final int[] STEPS = {3, 5, 6, 8, 9, 11};

	private MaterialMath() {
	}

	public static int damage(int iron, MaterialTier tier) {
		int index = indexOf(iron);
		return switch (tier) {
			case WOOD, LEATHER, BAMBOO -> index <= 0 ? 3 : STEPS[index - 1];
			case STONE, COPPER -> iron - 1;
			case IRON, GOLD -> iron;
			case DIAMOND -> index >= STEPS.length - 1 ? iron : STEPS[index + 1];
			case NETHERITE -> {
				int diamond = index >= STEPS.length - 1 ? iron : STEPS[index + 1];
				yield Math.min(12, diamond + 1);
			}
		};
	}

	public static int durability(int ironBase, MaterialTier tier) {
		if (ironBase <= 0) {
			return 0;
		}
		return Math.max(1, Math.round(ironBase * tier.durabilityScale));
	}

	public static float speed(float ironSpeed, MaterialTier tier) {
		return MaterialTier.roundSpeed(ironSpeed + tier.speedBonus);
	}

	private static int indexOf(int iron) {
		for (int i = 0; i < STEPS.length; i++) {
			if (STEPS[i] == iron) {
				return i;
			}
		}
		throw new IllegalArgumentException("铁阶伤害不在伤害阶上: " + iron);
	}
}

package vplus.weapon;

public enum DamageKind {
	SLASH,
	PIERCE,
	BLUNT,
	NONE;

	public float factor(boolean armored, boolean skip) {
		if (skip || this == SLASH || this == NONE) {
			return 1.0f;
		}
		if (this == PIERCE) {
			return armored ? 1.20f : 0.90f;
		}
		return armored ? 1.25f : 0.85f;
	}
}

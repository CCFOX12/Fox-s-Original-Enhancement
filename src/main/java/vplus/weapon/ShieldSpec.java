package vplus.weapon;

public record ShieldSpec(int armor, float angle, float raiseSeconds, float move, float bash, float disableSeconds) {
	public float reduction() {
		return this.armor * 0.15f;
	}
}

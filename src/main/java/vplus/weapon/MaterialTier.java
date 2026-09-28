package vplus.weapon;

public enum MaterialTier {
	WOOD("wooden", "木", 0.35f, 15, -1),
	LEATHER("leather", "皮革", 0.35f, 15, -1),
	BAMBOO("bamboo", "竹", 0.35f, 15, -1),
	STONE("stone", "石", 0.55f, 5, 0),
	COPPER("copper", "铜", 0.72f, 10, 0),
	IRON("iron", "铁", 1.0f, 14, 0),
	GOLD("golden", "金", 0.20f, 22, 1),
	DIAMOND("diamond", "钻石", 2.30f, 10, 2),
	NETHERITE("netherite", "下界合金", 3.0f, 15, 3);

	public final String id;
	public final String zh;
	public final float durabilityScale;
	public final int enchantability;
	public final float speedBonus;

	MaterialTier(String id, String zh, float durabilityScale, int enchantability, int speedKind) {
		this.id = id;
		this.zh = zh;
		this.durabilityScale = durabilityScale;
		this.enchantability = enchantability;
		this.speedBonus = speedKind == 1 ? 0.10f : 0.0f;
	}

	public static float roundSpeed(float speed) {
		return Math.round(speed * 20.0f) / 20.0f;
	}
}

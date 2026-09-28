package vplus.loadout;

public final class LoadoutSlots {
	public static final int DISPLAY_HEAD = 0;
	public static final int DISPLAY_COAT = 1;
	public static final int DISPLAY_LEGS = 2;
	public static final int DISPLAY_FEET = 3;
	public static final int UTILITY_HEAD = 4;
	public static final int UTILITY_COAT = 5;
	public static final int UTILITY_LEGS = 6;
	public static final int UTILITY_FEET = 7;
	public static final int PAULDRON = 8;
	public static final int LINING = 9;
	public static final int SHOULDER_STRAP = 10;
	public static final int BELT_STRAP = 11;
	public static final int BACK_STRAP = 12;
	public static final int SHOULDER_LEFT = 13;
	public static final int SHOULDER_RIGHT = 14;
	public static final int BELT_LEFT = 15;
	public static final int BELT_RIGHT = 16;
	public static final int BACK = 17;
	public static final int QUIVER_START = 18;
	public static final int POTION_START = 22;

	private LoadoutSlots() {
	}

	public static boolean isExtension(int slot) {
		return slot >= SHOULDER_LEFT && slot <= BACK;
	}

	public static boolean isQuiver(int slot) {
		return slot >= QUIVER_START && slot < POTION_START;
	}

	public static boolean isPotion(int slot) {
		return slot >= POTION_START && slot < Loadout.SIZE;
	}
}

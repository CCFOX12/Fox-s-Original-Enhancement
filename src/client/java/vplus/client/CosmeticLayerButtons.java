package vplus.client;

public final class CosmeticLayerButtons {
	public static final int SKIN_X = 168;
	public static final int ARMOR_X = 168;
	public static final int Y = 8;
	public static final int GAP = 14;
	public static final int SIZE = 12;

	private CosmeticLayerButtons() {
	}

	public static int hit(double mouseX, double mouseY, int left, int top) {
		if (inside(mouseX, mouseY, left + SKIN_X, top + Y)) {
			return 0;
		}
		if (inside(mouseX, mouseY, left + ARMOR_X, top + Y + GAP)) {
			return 1;
		}
		return -1;
	}

	private static boolean inside(double mouseX, double mouseY, int x, int y) {
		return mouseX >= x && mouseX < x + SIZE && mouseY >= y && mouseY < y + SIZE;
	}
}

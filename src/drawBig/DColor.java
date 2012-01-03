package drawBig;

public class DColor {
	public int A;
	public int R;
	public int G;
	public int B;

	public DColor(int r, int g, int b) {
		R = r;
		G = g;
		B = b;
		A = 255;
	}

	public DColor(int a, int r, int g, int b) {
		A = a;
		R = r;
		G = g;
		B = b;
	}

	public static DColor Parse(String string) {
		String[] st = string.split("c");

		DColor a = new DColor();
		a.A = Integer.parseInt(st[0]);
		a.R = Integer.parseInt(st[1]);
		a.G = Integer.parseInt(st[2]);
		a.B = Integer.parseInt(st[3]);

		return null;
	}

	public DColor() {
	}

	@Override
	public String toString() {
		return A + "c" + R + "c" + G + "c" + B;
	}

	static java.util.Random r = new java.util.Random();

	public static DColor Random() {
		return new DColor(255, r.nextInt(255), r.nextInt(255), r.nextInt(255));

	}
}

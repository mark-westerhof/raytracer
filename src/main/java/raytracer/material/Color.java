package raytracer.material;

public class Color {

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);

	private int red;
	private int green;
	private int blue;

	public Color(float red, float green, float blue) {
		this((int) (red * 255), (int) (green * 255), (int) (blue * 255));
	}

	public Color(int red, int green, int blue) {
		this.red = verifyColorBoundaries(red);
		this.green = verifyColorBoundaries(green);
		this.blue = verifyColorBoundaries(blue);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public Color add(Color color) {
		int red = verifyColorBoundaries(this.getRed() + color.getRed());
		int green = verifyColorBoundaries(this.getGreen() + color.getGreen());
		int blue = verifyColorBoundaries(this.getBlue() + color.getBlue());
		return new Color(red, green, blue);
	}

	public int getRGB() {
		return ((255 & 0xFF) << 24) | ((this.red & 0xFF) << 16) | ((this.green & 0xFF) << 8)
				| ((this.blue & 0xFF) << 0);
	}

	private static int verifyColorBoundaries(int color) {
		if (color > 255) {
			return 255;
		}
		else if (color < 0) {
			return 0;
		}
		else {
			return color;
		}
	}
}

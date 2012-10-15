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
		this.red = red;
		this.green = green;
		this.blue = blue;
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
		int red = this.red + color.getRed();
		int green = this.green + color.getGreen();
		int blue = this.blue + color.getBlue();
		return new Color(red, green, blue);
	}
	
	public Color divideBy(float value) {
		int red = (int) (this.red / value);
		int green = (int) (this.green / value);
		int blue = (int) (this.blue / value);
		return new Color(red, green, blue);
	}

	public int getRGB() {
		return ((255 & 0xFF) << 24) | ((verifyColorBoundaries(this.red) & 0xFF) << 16)
				| ((verifyColorBoundaries(this.green) & 0xFF) << 8) | ((verifyColorBoundaries(this.blue) & 0xFF) << 0);
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

package raytracer.material;

public class MaterialColor {
	
	private int red;
	private int green;
	private int blue;
	
	public MaterialColor(int red, int green, int blue) {
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
	
	public MaterialColor add(MaterialColor color) {
		int red = verifyColorBoundaries(this.getRed() + color.getRed());
		int green = verifyColorBoundaries(this.getGreen() + color.getGreen());
		int blue = verifyColorBoundaries(this.getBlue() + color.getBlue());
		return new MaterialColor(red, green, blue);
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

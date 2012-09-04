package raytracer.light;

public class Light {
	
	private int red;
	private int green;
	private int blue;
	private float x;
	private float y;
	private float z;
	private boolean ambient;
	
	public Light(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.ambient = true;
	}
	
	public Light(int red, int green, int blue, float x, float y, float z) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ambient = false;
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

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public boolean isAmbientLight() {
		return ambient;
	}
	
	public boolean isPointLight() {
		return !ambient;
	}
}

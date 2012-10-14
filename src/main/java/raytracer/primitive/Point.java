package raytracer.primitive;

public class Point {

	private float x;
	private float y;
	private float z;

	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public Vector minus(Point point) {
		float vectorX = this.x - point.getX();
		float vectorY = this.y - point.getY();
		float vectorZ = this.z - point.getZ();
		
		return new Vector(vectorX, vectorY, vectorZ);
	}
	
	public Point plus(Vector vector) {
		float pointX = this.x + vector.getX();
		float pointY = this.y + vector.getY();
		float pointZ = this.z + vector.getZ();
		
		return new Point(pointX, pointY, pointZ);
	}
}

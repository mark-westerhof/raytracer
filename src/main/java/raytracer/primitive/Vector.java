package raytracer.primitive;

public class Vector {

	private float x;
	private float y;
	private float z;

	public Vector(float x, float y, float z) {
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

	public Vector crossProduct(Vector vector) {
		float vx = (this.y * vector.getZ()) - (this.z * vector.getY());
		float vy = (this.z * vector.getX()) - (this.x * vector.getZ());
		float vz = (this.x * vector.getY()) - (this.y * vector.getX());

		return new Vector(vx, vy, vz);
	}

	public void normalize() {
		float a = (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
		this.x = this.x / a;
		this.y = this.y / a;
		this.z = this.z / a;
	}

	public void invert() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public Vector times(float value) {
		return new Vector(this.x * value, this.y * value, this.z * value);
	}

	public Vector plus(Vector vector) {
		return new Vector(this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
	}

	public float times(Vector vector) {
		return (this.x * vector.getX()) + (this.y * vector.getY()) + (this.z * vector.getZ());
	}
}

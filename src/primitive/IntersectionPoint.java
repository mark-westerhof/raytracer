package primitive;

public class IntersectionPoint extends Point {

	private Integer triangleMeshIndex;

	public IntersectionPoint(float x, float y, float z, Integer triangleMeshIndex) {
		super(x, y, z);
		this.triangleMeshIndex = triangleMeshIndex;
	}

	public IntersectionPoint(Point point, Integer triangleMeshIndex) {
		super(point.getX(), point.getY(), point.getZ());
		this.triangleMeshIndex = triangleMeshIndex;
	}

	public Integer getTriangleMeshIndex() {
		return triangleMeshIndex;
	}
}
package primitive;

public class IntersectionDistance {

	private Float intersectionDistance;
	private Integer triangleMeshIndex;

	public IntersectionDistance(Float intersectionDistance) {
		this(intersectionDistance, null);
	}

	public IntersectionDistance(Float intersectionDistance, Integer triangleMeshIndex) {
		this.intersectionDistance = intersectionDistance;
		this.triangleMeshIndex = triangleMeshIndex;
	}

	public Float getIntersectionDistance() {
		return intersectionDistance;
	}

	public Integer getTriangleMeshIndex() {
		return triangleMeshIndex;
	}
}
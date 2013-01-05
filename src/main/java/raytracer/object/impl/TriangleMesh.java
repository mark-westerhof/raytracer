package raytracer.object.impl;

import java.util.List;

import raytracer.material.Material;
import raytracer.object.SceneObject;
import raytracer.primitive.IntersectionDistance;
import raytracer.primitive.IntersectionPoint;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;
import raytracer.scene.obj.OBJFace;
import raytracer.scene.obj.OBJTextureCoordinate;

public class TriangleMesh implements SceneObject {

	private List<Point> vertices;
	private List<OBJTextureCoordinate> textureCoordinates;
	private List<OBJFace> faces;
	private Material material;

	public TriangleMesh(List<Point> vertices, List<OBJTextureCoordinate> textureCoordinates, List<OBJFace> faces) {
		this(vertices, textureCoordinates, faces, null);
	}

	public TriangleMesh(List<Point> vertices, List<OBJTextureCoordinate> textureCoordinates, List<OBJFace> faces,
			Material material) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
		this.faces = faces;
		this.material = material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public IntersectionDistance intersectionDistance(Ray ray) {
		Float distance = Float.MAX_VALUE;
		Float intersectionDistance = null;
		Integer faceIndex = null;
		int index = 0;
		for (OBJFace face : faces) {
			Point p0 = vertices.get(face.getVertexIndices()[0]);
			Point p1 = vertices.get(face.getVertexIndices()[1]);
			Point p2 = vertices.get(face.getVertexIndices()[2]);

			// Create triangle vectors
			Vector u = p1.minus(p0);
			Vector v = p2.minus(p0);
			Vector n = u.crossProduct(v).normalized();

			Vector w0 = ray.getOrigin().minus(p0);
			float a = -n.dotProduct(w0);
			float b = n.dotProduct(ray.getDirection());

			// Ensure ray isn't parallel to plane
			if (Math.abs(b) > 0.000001f) {

				// Distance from plane
				float r = a / b;

				if (r > 0 && r < distance && r > 0) {
					Point intersectionPoint = ray.getOrigin().plus(ray.getDirection().times(r));

					// Check that intersection point is inside triangle
					float uu = u.dotProduct(u);
					float uv = u.dotProduct(v);
					float vv = v.dotProduct(v);
					Vector w = intersectionPoint.minus(p0);
					float wu = w.dotProduct(u);
					float wv = w.dotProduct(v);
					float d = (uv * uv) - (uu * vv);

					// Test parametric coordinates
					float s = ((uv * wv) - (vv * wu)) / d;
					if (s > 0 && s < 1) {
						float t = ((uv * wu) - (uu * wv)) / d;
						if (t > 0 && (s + t) < 1) {
							distance = r;
							intersectionDistance = r;
							faceIndex = index;
						}
					}
				}
			}
		index++;
		}
		return new IntersectionDistance(intersectionDistance, faceIndex);
	}

	public Material getMaterial() {
		return material;
	}

	public Vector getSurfaceNormal(IntersectionPoint intersectionPoint) {
		OBJFace face = faces.get(intersectionPoint.getTriangleMeshIndex());
		Point p0 = vertices.get(face.getVertexIndices()[0]);
		Point p1 = vertices.get(face.getVertexIndices()[1]);
		Point p2 = vertices.get(face.getVertexIndices()[2]);
		
		Vector u = p1.minus(p0);
		Vector v = p2.minus(p0);
		return u.crossProduct(v).normalized();
	}
}

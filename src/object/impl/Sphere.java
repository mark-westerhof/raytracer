package object.impl;

import material.Material;
import object.SceneObject;
import primitive.IntersectionDistance;
import primitive.IntersectionPoint;
import primitive.Point;
import primitive.Vector;
import ray.Ray;

public class Sphere implements SceneObject {

	private Point origin;
	private float radius;
	private Material material;

	public Sphere(Point origin, float radius, Material material) {
		this.origin = origin;
		this.radius = radius;
		this.material = material;
	}

	public Point getOrigin() {
		return origin;
	}

	public float getRadius() {
		return radius;
	}

	public Material getMaterial() {
		return material;
	}

	public IntersectionDistance intersectionDistance(Ray ray) {

		Vector distance = this.origin.minus(ray.getOrigin());
		float b = ray.getDirection().dotProduct(distance);
		float d = (b * b) - distance.dotProduct(distance) + (this.radius * this.radius);
		if (d < 0) {
			return new IntersectionDistance(null);
		}

		float t0 = (float) (b - Math.sqrt(d));
		float t1 = (float) (b + Math.sqrt(d));

		Float returnValue = null;

		if (t0 > 0.1) {
			returnValue = t0;
		}
		if (t1 > 0.1) {
			if (returnValue == null) {
				returnValue = t1;
			}
			else if (t1 < returnValue) {
				returnValue = t1;
			}
		}
		return new IntersectionDistance(returnValue);
	}

	public Vector getSurfaceNormal(IntersectionPoint intersectionPoint) {

		return intersectionPoint.minus(this.origin).normalized();
	}
}

package raytracer.object.impl;

import raytracer.material.Material;
import raytracer.object.SceneObject;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;

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

	public Float intersectionDistance(Ray ray) {

		Vector distance = this.origin.minus(ray.getOrigin());
		float b = ray.getDirection().times(distance);
		float d = (b * b) - distance.times(distance) + (this.radius * this.radius);
		if (d < 0) {
			return null;
		}

		float t0 = (float) (b - Math.sqrt(d));
		float t1 = (float) (b + Math.sqrt(d));
		
		Float returnValue = null;
		
		if (t0 > 0) {
			returnValue = t0;
		}
		if (t1 > 0) {
			if (returnValue == null) {
				returnValue = t1;
			}
			else if (t1 < returnValue) {
				returnValue = t1;
			}
		}
		return returnValue;
	}
}

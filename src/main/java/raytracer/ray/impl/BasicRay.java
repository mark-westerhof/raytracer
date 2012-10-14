package raytracer.ray.impl;

import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;

public class BasicRay implements Ray {

	private Point origin;
	private Vector direction;

	public BasicRay(Point origin, Vector direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Point getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}
}

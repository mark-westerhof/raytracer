package raytracer.ray.impl;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.object.SceneObject;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;
import raytracer.scene.Scene;

public class TraceRay implements Ray, Runnable {

	protected Point origin;
	protected Vector direction;
	protected Scene scene;
	protected int xPixel;
	protected int yPixel;
	private int traceDepth;

	public TraceRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel) {
		this.origin = origin;
		this.direction = direction;
		this.scene = scene;
		this.xPixel = xPixel;
		this.yPixel = yPixel;
		this.traceDepth = 0;
	}

	public Point getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public void run() {
		Color color = trace(this);
		scene.updateImage(this.xPixel, this.yPixel, color);
	}

	protected Color trace(Ray ray, int depth) {

		// Find closest object intersection
		int hitObject = -1;
		float hitDistance = Float.MAX_VALUE;
		int index = 0;
		for (SceneObject object : scene.getObjects()) {
			Float currentHitDistance = object.intersectionDistance(ray);
			if (currentHitDistance != null && currentHitDistance < hitDistance) {
				hitObject = index;
				hitDistance = currentHitDistance;
			}
			index++;
		}

		// Hit object? Illuminate for each light in scene
		if (hitObject >= 0) {
			Point intersectionPoint = origin.plus(direction.times(hitDistance));
			Color color = Color.BLACK;
			for (Light light : scene.getLights()) {
				color = color.add(light.illuminateObject(intersectionPoint, scene, hitObject));
			}
			return color;
		}
		else {
			return Color.BLACK;
		}
	}

	protected Color trace(Ray ray) {
		return trace(ray, 1);
	}
}

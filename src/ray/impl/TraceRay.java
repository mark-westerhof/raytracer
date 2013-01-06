package ray.impl;

import light.Light;
import material.Color;
import object.SceneObject;
import primitive.IntersectionDistance;
import primitive.IntersectionPoint;
import primitive.Point;
import primitive.Vector;
import ray.Ray;
import scene.Scene;

public class TraceRay implements Ray, Runnable {

	protected Point origin;
	protected Vector direction;
	protected Scene scene;
	protected int xPixel;
	protected int yPixel;
	protected int traceDepth;

	public TraceRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel, int traceDepth) {
		this.origin = origin;
		this.direction = direction;
		this.scene = scene;
		this.xPixel = xPixel;
		this.yPixel = yPixel;
		this.traceDepth = traceDepth;
	}

	public Point getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public void run() {
		Color color = trace();
		scene.updateImage(this.xPixel, this.yPixel, color);
	}

	protected Color trace(Ray ray, int depth) {

		// Find closest object intersection
		int hitObject = -1;
		float hitDistance = Float.MAX_VALUE;
		Integer triangleMeshIndex = null;
		int index = 0;
		for (SceneObject object : scene.getObjects()) {
			IntersectionDistance intersectionDistance = object.intersectionDistance(ray);
			if (intersectionDistance.getIntersectionDistance() != null
					&& intersectionDistance.getIntersectionDistance() < hitDistance) {
				hitObject = index;
				hitDistance = intersectionDistance.getIntersectionDistance();
				triangleMeshIndex = intersectionDistance.getTriangleMeshIndex();
			}
			index++;
		}

		// Hit object? Illuminate for each light in scene
		if (hitObject >= 0) {
			IntersectionPoint intersectionPoint = new IntersectionPoint(origin.plus(direction.times(hitDistance)),
					triangleMeshIndex);
			Color color = Color.BLACK;
			for (Light light : scene.getLights()) {
				color = color.add(light.illuminateObject(intersectionPoint, scene, hitObject));
			}

			// Reflect off?
			float specularConstant = scene.getObject(hitObject).getMaterial().getPhongIllumination()
					.getSpecularConstant();
			if (specularConstant > 0 && depth <= traceDepth) {
				this.origin = intersectionPoint;
				Vector normal = scene.getObject(hitObject).getSurfaceNormal(intersectionPoint);
				this.direction = this.direction.minus(normal.times(this.direction.dotProduct(normal)).times(2))
						.normalized();
				return color.add(trace(ray, depth + 1).times(specularConstant));
			}
			else {
				return color;
			}
		}
		else {
			return Color.BLACK;
		}
	}

	public Color trace() {
		return trace(this, 1);
	}
}

package raytracer.primitive;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.object.SceneObject;
import raytracer.scene.Scene;

public class Ray implements Runnable {

	private Point origin;
	private Vector direction;
	private Scene scene;
	private int pixelX;
	private int pixelY;
	private int traceDepth;

	public Ray(Point origin, Vector direction, Scene scene, int pixelX, int pixelY) {
		this.origin = origin;
		this.direction = direction;
		this.scene = scene;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.traceDepth = 0;
	}

	public Point getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public void run() {
		Color color = trace();
		scene.updateImage(this.pixelX, this.pixelY, color);
	}

	private Color trace() {

		// Find closest object intersection
		int hitObject = -1;
		float hitDistance = Float.MAX_VALUE;
		int index = 0;
		for (SceneObject object : scene.getObjects()) {
			Float currentHitDistance = object.intersectionDistance(this);
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
}

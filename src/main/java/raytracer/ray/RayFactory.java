package raytracer.ray;

import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.impl.SuperSampledTraceRay;
import raytracer.ray.impl.TraceRay;
import raytracer.scene.Scene;

public class RayFactory {

	private RayFactory() {
		throw new AssertionError();
	}

	public static TraceRay createRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel,
			Vector cameraX, Vector cameraY, float xStep, float yStep) {

		if (scene.isSuperSampled()) {
			return new SuperSampledTraceRay(origin, direction, scene, xPixel, yPixel, cameraX, cameraY, xStep, yStep);
		}
		else {
			return new TraceRay(origin, direction, scene, xPixel, yPixel);
		}
	}
}
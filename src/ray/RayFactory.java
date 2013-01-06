package ray;

import primitive.Point;
import primitive.Vector;
import ray.impl.SuperSampledTraceRay;
import ray.impl.TraceRay;
import scene.Scene;

public class RayFactory {

	private RayFactory() {
		throw new AssertionError();
	}

	public static TraceRay createRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel,
			Vector cameraX, Vector cameraY, float xStep, float yStep, int traceDepth) {

		if (scene.isSuperSampled()) {
			return new SuperSampledTraceRay(origin, direction, scene, xPixel, yPixel, cameraX, cameraY, xStep, yStep,
					traceDepth);
		}
		else {
			return new TraceRay(origin, direction, scene, xPixel, yPixel, traceDepth);
		}
	}
}
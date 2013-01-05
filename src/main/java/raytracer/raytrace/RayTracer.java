package raytracer.raytrace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.RayFactory;
import raytracer.ray.impl.TraceRay;
import raytracer.scene.Scene;

public class RayTracer {

	private RayTracer() {
		throw new AssertionError();
	}

	public static void traceScene(Scene scene) {

		// Get camera vectors x & y
		Point origin = scene.getCameraOrigin();
		Point target = scene.getCameraTarget();
		Vector forward = target.minus(origin);
		Vector cameraX = forward.crossProduct(scene.getCameraUp());
		Vector cameraY = cameraX.crossProduct(forward);
		cameraX.normalize();
		cameraY.normalize();
		cameraY.invert();

		// Get camera angles
		float xAngle = scene.getCameraAngle();
		float yAngle = xAngle * (scene.getCameraResolutionY() / (float) scene.getCameraResolutionX());
		xAngle = (float) (xAngle * (Math.PI / 180));
		yAngle = (float) (yAngle * (Math.PI / 180));

		// Calculate distances between pixel rays
		float xStart = (float) -Math.tan(xAngle / 2);
		float yStart = (float) -Math.tan(yAngle / 2);
		float xStep = -xStart / (scene.getCameraResolutionX() / 2);
		float yStep = -yStart / (scene.getCameraResolutionY() / 2);
		Vector xTopLeft = cameraX.times(xStart);
		Vector yTopLeft = cameraY.times(yStart);

		// Initialize ray to point to top left corner
		forward.normalize();
		forward = forward.plus(xTopLeft);
		forward = forward.plus(yTopLeft);

		// Initialize scene & create thread pool
		scene.initialize();
		ExecutorService executer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		// Iterate through pixels, create ray runnable & add to thread pool
		for (int yPixel = 0; yPixel < scene.getCameraResolutionY(); yPixel++) {
			for (int xPixel = 0; xPixel < scene.getCameraResolutionX(); xPixel++) {

				Vector direction = forward.plus(cameraY.times(yPixel * yStep));
				direction = direction.plus(cameraX.times(xPixel * xStep));
				direction.normalize();
				TraceRay traceRay = RayFactory.createRay(origin, direction, scene, xPixel, yPixel, cameraX, cameraY,
						xStep, yStep);
				executer.execute(traceRay);
			}
		}

		// Wait for threads to finish
		executer.shutdown();
		while (!executer.isTerminated()) {
		}
	}
}

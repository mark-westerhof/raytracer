package raytracer.ray.impl;

import java.util.Random;

import raytracer.material.Color;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.scene.Scene;

public class SuperSampledTraceRay extends TraceRay {

	private Vector cameraX;
	private Vector cameraY;
	private float xStep;
	private float yStep;
	private Random generator;

	public SuperSampledTraceRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel, Vector cameraX,
			Vector cameraY, float xStep, float yStep) {
		super(origin, direction, scene, xPixel, yPixel);
		this.cameraX = cameraX;
		this.cameraY = cameraY;
		this.xStep = xStep;
		this.yStep = yStep;
		this.generator = new Random();
	}

	public void run() {

		Color color = trace(this);
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(-0.5f, -0.5f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(-0.5f, -0.1667f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(-0.5f, 0.1667f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(-0.1667f, 0.1667f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(0.1667f, 0.1667f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(0.1667f, -0.1667f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(0.1667f, -0.5f))));
		color = color.add(trace(new BasicRay(this.origin, createDirectionOffset(-0.1667f, -0.5f))));

		this.scene.updateImage(this.xPixel, this.yPixel, color.divideBy(9));
	}

	private float randomOffset() {
		return (float) (generator.nextFloat() * 0.125);
	}

	private Vector createDirectionOffset(float xOffset, float yOffset) {
		Vector vector = this.direction.plus(this.cameraY.times(yOffset * this.yStep).plus(
				this.cameraY.times(randomOffset() * this.yStep)));
		return vector.plus(this.cameraX.times(xOffset * this.xStep).plus(
				this.cameraX.times(randomOffset() * this.xStep)));
	}
}

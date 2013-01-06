package ray.impl;

import java.util.Random;

import material.Color;
import primitive.Point;
import primitive.Vector;
import scene.Scene;

public class SuperSampledTraceRay extends TraceRay {

	private Vector cameraX;
	private Vector cameraY;
	private float xStep;
	private float yStep;
	private Random generator;

	public SuperSampledTraceRay(Point origin, Vector direction, Scene scene, int xPixel, int yPixel, Vector cameraX,
			Vector cameraY, float xStep, float yStep, int traceDepth) {
		super(origin, direction, scene, xPixel, yPixel, traceDepth);
		this.cameraX = cameraX;
		this.cameraY = cameraY;
		this.xStep = xStep;
		this.yStep = yStep;
		this.generator = new Random();
	}

	public void run() {

		Color color = Color.BLACK;
		color = color.add(new TraceRay(origin, createDirectionOffset(-0.5f, -0.5f), scene, xPixel, yPixel, traceDepth)
				.trace());
		color = color.add(new TraceRay(origin, createDirectionOffset(-0.5f, -0.1667f), scene, xPixel, yPixel,
				traceDepth).trace());
		color = color
				.add(new TraceRay(origin, createDirectionOffset(-0.5f, 0.1667f), scene, xPixel, yPixel, traceDepth)
						.trace());
		color = color.add(new TraceRay(origin, createDirectionOffset(-0.1667f, 0.1667f), scene, xPixel, yPixel,
				traceDepth).trace());
		color = color.add(new TraceRay(origin, createDirectionOffset(0.1667f, 0.1667f), scene, xPixel, yPixel,
				traceDepth).trace());
		color = color.add(new TraceRay(origin, createDirectionOffset(0.1667f, -0.1667f), scene, xPixel, yPixel,
				traceDepth).trace());
		color = color
				.add(new TraceRay(origin, createDirectionOffset(0.1667f, -0.5f), scene, xPixel, yPixel, traceDepth)
						.trace());
		color = color.add(new TraceRay(origin, createDirectionOffset(-0.1667f, -0.5f), scene, xPixel, yPixel,
				traceDepth).trace());
		color.add(trace());

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

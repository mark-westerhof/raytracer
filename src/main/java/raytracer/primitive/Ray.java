package raytracer.primitive;

import raytracer.material.Color;
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
	
	public void run() {
		scene.updateImage(this.pixelX, this.pixelY, Color.BLACK);
	}
}

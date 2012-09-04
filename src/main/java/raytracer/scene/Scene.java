package raytracer.scene;

import raytracer.light.Light;
import raytracer.material.MaterialColor;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;

public class Scene {

	// Camera parameters
	private Point cameraOrigin;
	private Point cameraTarget;
	private Vector cameraUp;
	private float cameraAngle;
	private int cameraResolutionX;
	private int cameraResolutionY;
	
	//Simple lighting
	private MaterialColor background;
	private Light ambientLight;

	public Scene() {
	}

	public Point getCameraOrigin() {
		return cameraOrigin;
	}

	public void setCameraOrigin(Point cameraOrigin) {
		this.cameraOrigin = cameraOrigin;
	}

	public Point getCameraTarget() {
		return cameraTarget;
	}

	public void setCameraTarget(Point cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	public Vector getCameraUp() {
		return cameraUp;
	}

	public void setCameraUp(Vector cameraUp) {
		this.cameraUp = cameraUp;
	}

	public float getCameraAngle() {
		return cameraAngle;
	}

	public void setCameraAngle(float cameraAngle) {
		this.cameraAngle = cameraAngle;
	}

	public int getCameraResolutionX() {
		return cameraResolutionX;
	}

	public void setCameraResolutionX(int cameraResolutionX) {
		this.cameraResolutionX = cameraResolutionX;
	}

	public int getCameraResolutionY() {
		return cameraResolutionY;
	}

	public void setCameraResolutionY(int cameraResolutionY) {
		this.cameraResolutionY = cameraResolutionY;
	}

	public MaterialColor getBackground() {
		return background;
	}

	public void setBackground(MaterialColor background) {
		this.background = background;
	}

	public Light getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(Light ambientLight) {
		this.ambientLight = ambientLight;
	}
}

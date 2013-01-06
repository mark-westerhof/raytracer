package raytracer.scene;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.object.SceneObject;
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

	// Lights
	private List<Light> lights = new ArrayList<Light>();

	// Objects
	private List<SceneObject> objects = new ArrayList<SceneObject>();

	// Output image & progress bar for feedback
	private BufferedImage image;
	private JProgressBar progressBar;
	private int progress = 0;
	private int progressInterval;
	
	// Options
	private boolean superSampled = false;
	private int traceDepth;

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

	public Light getLight(int index) {
		return this.lights.get(index);
	}

	public List<Light> getLights() {
		return this.lights;
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public SceneObject getObject(int index) {
		return this.objects.get(index);
	}

	public List<SceneObject> getObjects() {
		return this.objects;
	}

	public void addObject(SceneObject object) {
		this.objects.add(object);
	}

	public void initialize() {
		image = new BufferedImage(cameraResolutionX, cameraResolutionY, BufferedImage.TYPE_INT_RGB);
		progressInterval = (int) (0.1 * cameraResolutionX * cameraResolutionY);
	}

	public synchronized void updateImage(int x, int y, Color color) {

		image.setRGB(x, y, color.getRGB());

		// Update progress bar on EDT every 10%
		progress++;
		if (progress % progressInterval == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					progressBar.setValue(progress);
				}
			});
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public void resetProgressBarState() {
		this.progress = 0;
	}

	public boolean isSuperSampled() {
		return superSampled;
	}

	public void setSuperSampled(boolean superSampled) {
		this.superSampled = superSampled;
	}

	public int getTraceDepth() {
		return traceDepth;
	}

	public void setTraceDepth(int traceDepth) {
		this.traceDepth = traceDepth;
	}
}

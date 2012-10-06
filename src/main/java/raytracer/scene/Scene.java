package raytracer.scene;

import java.awt.image.BufferedImage;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import raytracer.material.Color;
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

	// Output image & progress bar for feedback
	private BufferedImage image;
	private JProgressBar progressBar;
	private int progress = 0;
	private int progressInterval;

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
}

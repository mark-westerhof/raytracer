package raytracer.light;

import raytracer.material.Color;
import raytracer.primitive.Point;
import raytracer.scene.Scene;

public class AmbientLight implements Light {
	
	private int red;
	private int green;
	private int blue;
	
	public AmbientLight(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color contributionFromLight(Point intersectionPoint, Scene scene, int objectIndex) {

		return Color.BLACK;
	}

}

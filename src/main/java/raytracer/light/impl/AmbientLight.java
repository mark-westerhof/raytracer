package raytracer.light.impl;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.primitive.IntersectionPoint;
import raytracer.scene.Scene;

public class AmbientLight implements Light {

	private float red;
	private float green;
	private float blue;

	public AmbientLight(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color illuminateObject(IntersectionPoint intersectionPoint, Scene scene, int objectIndex) {

		Color materialColor = scene.getObject(objectIndex).getMaterial().getColor();
		float materialAmbient = scene.getObject(objectIndex).getMaterial().getPhongIllumination().getAmbientConstant();

		int colorRed = (int) (this.red * materialColor.getRed() * materialAmbient);
		int colorGreen = (int) (this.green * materialColor.getGreen() * materialAmbient);
		int colorBlue = (int) (this.blue * materialColor.getBlue() * materialAmbient);

		return new Color(colorRed, colorGreen, colorBlue);
	}
}
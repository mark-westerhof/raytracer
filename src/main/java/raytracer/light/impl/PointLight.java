package raytracer.light.impl;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.material.PhongIllumination;
import raytracer.object.SceneObject;
import raytracer.object.impl.Sphere;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.impl.BasicRay;
import raytracer.scene.Scene;

public class PointLight implements Light {

	private float red;
	private float green;
	private float blue;
	private Point origin;

	public PointLight(float red, float green, float blue, Point origin) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.origin = origin;
	}

	public Color illuminateObject(Point intersectionPoint, Scene scene, int objectIndex) {

		SceneObject hitObject = scene.getObject(objectIndex);
		if (hitObject instanceof Sphere) {
			return illuminateSphere(intersectionPoint, scene, objectIndex);
		}
		else {
			// TODO
			return null;
		}

	}

	private Color illuminateSphere(Point intersectionPoint, Scene scene, int objectIndex) {
		Sphere sphere = (Sphere) scene.getObject(objectIndex);
		Color color = Color.BLACK;

		Vector normal = intersectionPoint.minus(sphere.getOrigin()).normalized();
		Vector lightDistance = this.origin.minus(intersectionPoint);

		// Is the surface normal and light direction even facing the same way?
		float diffuseDot = normal.dotProduct(lightDistance.normalized());
		if (diffuseDot > 0) {

			// Check if point is in shadow... any other objects between intersection point and light?
			BasicRay shadowCheckRay = new BasicRay(intersectionPoint, lightDistance.normalized());
			boolean inShadow = false;
			int index = 0;
			for (SceneObject object : scene.getObjects()) {
				// Ignore own object
				if (index != objectIndex && object.intersectionDistance(shadowCheckRay) != null) {
					inShadow = true;
					break;
				}
				index++;
			}

			// Not in shadow, illuminate
			if (!inShadow) {
				Color materialColor = sphere.getMaterial().getColor();
				PhongIllumination phong = sphere.getMaterial().getPhongIllumination();

				// Calculate light fall off
				float fallOff = (float) Math.sqrt((Math.pow(lightDistance.getX(), 2)
						+ Math.pow(lightDistance.getY(), 2) + Math.pow(lightDistance.getZ(), 2)));
				fallOff = 1 / (fallOff * fallOff);
				float fallOffRed = this.red * fallOff;
				float fallOffGreen = this.green * fallOff;
				float fallOffBlue = this.blue * fallOff;

				// Calculate diffuse component
				float diffuseValue = diffuseDot * phong.getDiffuseConstant();
				int red = (int) (diffuseValue * materialColor.getRed() * fallOffRed);
				int green = (int) (diffuseValue * materialColor.getGreen() * fallOffGreen);
				int blue = (int) (diffuseValue * materialColor.getBlue() * fallOffBlue);
				color = color.add(new Color(red, green, blue));

				// Calculate specular componenet
				// TODO

			}
		}
		return color;
	}
}

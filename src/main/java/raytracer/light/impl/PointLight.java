package raytracer.light.impl;

import raytracer.light.Light;
import raytracer.material.Color;
import raytracer.material.PhongIllumination;
import raytracer.object.SceneObject;
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
		Color color = Color.BLACK;

		Vector normal = hitObject.getSurfaceNormal(intersectionPoint);
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
				Color materialColor = hitObject.getMaterial().getColor();
				PhongIllumination phong = hitObject.getMaterial().getPhongIllumination();

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

				// Calculate specular component
				if (phong.getSpecularConstant() > 0) {
					lightDistance.normalize();
					Vector r = lightDistance.minus(normal.times(2 * lightDistance.dotProduct(normal)));
					Vector v = intersectionPoint.minus(scene.getCameraOrigin()).normalized();
					if (r.dotProduct(v) > 0) {
						float specularValue = (float) Math.pow(v.dotProduct(r), phong.getShininess())
								* phong.getSpecularConstant();
						red = (int) (specularValue * 255 * fallOffRed);
						green = (int) (specularValue * 255 * fallOffGreen);
						blue = (int) (specularValue * 255 * fallOffBlue);
						color = color.add(new Color(red, green, blue));
					}
				}
			}
		}
		return color;
	}
}

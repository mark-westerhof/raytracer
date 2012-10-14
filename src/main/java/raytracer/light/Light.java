package raytracer.light;

import raytracer.material.Color;
import raytracer.primitive.Point;
import raytracer.scene.Scene;

public interface Light {

	public Color illuminateObject(Point intersectionPoint, Scene scene, int objectIndex);

}

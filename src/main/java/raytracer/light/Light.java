package raytracer.light;

import raytracer.material.Color;
import raytracer.primitive.IntersectionPoint;
import raytracer.scene.Scene;

public interface Light {

	public Color illuminateObject(IntersectionPoint intersectionPoint, Scene scene, int objectIndex);

}

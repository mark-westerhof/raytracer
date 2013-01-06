package light;

import material.Color;
import primitive.IntersectionPoint;
import scene.Scene;

public interface Light {

	public Color illuminateObject(IntersectionPoint intersectionPoint, Scene scene, int objectIndex);

}

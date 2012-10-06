package raytracer.object;

import raytracer.primitive.Ray;

public interface SceneObject {
	
	public int intersectionDistance(Ray ray);

}

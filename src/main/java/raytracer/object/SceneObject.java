package raytracer.object;

import raytracer.material.Material;
import raytracer.primitive.Ray;

public interface SceneObject {
	
	public Float intersectionDistance(Ray ray);
	
	public Material getMaterial();

}

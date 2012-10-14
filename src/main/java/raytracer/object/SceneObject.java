package raytracer.object;

import raytracer.material.Material;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;

public interface SceneObject {

	public Float intersectionDistance(Ray ray);

	public Material getMaterial();
	
	public Vector getSurfaceNormal(Point intersectionPoint);

}

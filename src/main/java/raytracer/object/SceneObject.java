package raytracer.object;

import raytracer.material.Material;
import raytracer.primitive.IntersectionDistance;
import raytracer.primitive.IntersectionPoint;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;

public interface SceneObject {

	public IntersectionDistance intersectionDistance(Ray ray);

	public Material getMaterial();

	public Vector getSurfaceNormal(IntersectionPoint intersectionPoint);

}
package object;

import material.Material;
import primitive.IntersectionDistance;
import primitive.IntersectionPoint;
import primitive.Vector;
import ray.Ray;

public interface SceneObject {

	public IntersectionDistance intersectionDistance(Ray ray);

	public Material getMaterial();

	public Vector getSurfaceNormal(IntersectionPoint intersectionPoint);

}
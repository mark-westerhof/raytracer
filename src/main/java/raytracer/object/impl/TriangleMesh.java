package raytracer.object.impl;

import java.util.List;

import raytracer.material.Material;
import raytracer.object.SceneObject;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.ray.Ray;
import raytracer.scene.obj.OBJFace;
import raytracer.scene.obj.OBJTextureCoordinate;

public class TriangleMesh implements SceneObject {

	private List<Point> vertices;
	private List<OBJTextureCoordinate> textureCoordinates;
	private List<OBJFace> faces;
	private Material material;

	public TriangleMesh(List<Point> vertices, List<OBJTextureCoordinate> textureCoordinates, List<OBJFace> faces) {
		this(vertices, textureCoordinates, faces, null);
	}

	public TriangleMesh(List<Point> vertices, List<OBJTextureCoordinate> textureCoordinates, List<OBJFace> faces,
			Material material) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
		this.faces = faces;
		this.material = material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@Override
	public Float intersectionDistance(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getSurfaceNormal(Point intersectionPoint) {
		// TODO Auto-generated method stub
		return null;
	}

}

package raytracer.ray;

import raytracer.primitive.Point;
import raytracer.primitive.Vector;

public interface Ray {

	public Point getOrigin();

	public Vector getDirection();
}

package raytracer.material;

public class PhongIllumination {

	private float ambientConstant;
	private float diffuseConstant;
	private float specularConstant;
	private float shininess;

	public PhongIllumination(float ambient, float diffuse, float specular, float shininess) {
		this.ambientConstant = verifyRatioBoundaries(ambient);
		this.diffuseConstant = verifyRatioBoundaries(diffuse);
		this.specularConstant = verifyRatioBoundaries(specular);
		this.shininess = shininess;
	}

	public float getAmbientConstant() {
		return ambientConstant;
	}

	public float getDiffuseConstant() {
		return diffuseConstant;
	}

	public float getSpecularConstant() {
		return specularConstant;
	}

	public float getShininess() {
		return shininess;
	}

	private static float verifyRatioBoundaries(float ratio) {
		if (ratio > 1) {
			return 1;
		}
		else if (ratio < 0) {
			return 0;
		}
		else {
			return ratio;
		}
	}
}

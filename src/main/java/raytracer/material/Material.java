package raytracer.material;

public class Material {
	
	private MaterialColor color;
	private PhongIllumination phongIllumination;
	private float transmittance;
	private float indexOfRefraction;
	
	public Material(MaterialColor color, PhongIllumination phong, float transmittance, float indexOfRefraction) {
		this.color = color;
		this.phongIllumination = phong;
		this.transmittance = transmittance;
		this.indexOfRefraction = indexOfRefraction;
	}

	public MaterialColor getColor() {
		return color;
	}

	public PhongIllumination getPhongIllumination() {
		return phongIllumination;
	}

	public float getTransmittance() {
		return transmittance;
	}

	public float getIndexOfRefraction() {
		return indexOfRefraction;
	}
}

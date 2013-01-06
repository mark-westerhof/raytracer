package material;

public class Material {

	private Color color;
	private PhongIllumination phongIllumination;
	private float transmittance;
	private float indexOfRefraction;

	public Material(Color color, PhongIllumination phong, float transmittance, float indexOfRefraction) {
		this.color = color;
		this.phongIllumination = phong;
		this.transmittance = transmittance;
		this.indexOfRefraction = indexOfRefraction;
	}

	public Color getColor() {
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

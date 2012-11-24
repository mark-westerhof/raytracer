package raytracer.scene.obj;

public class OBJFace {

	private int[] vertexIndices;
	private int[] textureCoordinateIndices;

	public OBJFace(int[] vertexIndices, int[] textureCoordinateIndices) {
		this.vertexIndices = vertexIndices;
		this.textureCoordinateIndices = textureCoordinateIndices;
	}
	
	public OBJFace(int[] vertexIndices) {
		this.vertexIndices = vertexIndices;
	}

	public int[] getVertexIndices() {
		return vertexIndices;
	}

	public int[] getTextureCoordinateIndices() {
		return textureCoordinateIndices;
	}
}

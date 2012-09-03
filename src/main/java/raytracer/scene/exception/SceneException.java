package raytracer.scene.exception;

public class SceneException extends Exception {

	private static final long serialVersionUID = 5516078076883481537L;
	private String message = null;

	public SceneException() {
		super();
	}

	public SceneException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}

package raytracer.scene;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import raytracer.light.Light;
import raytracer.material.MaterialColor;
import raytracer.primitive.Point;
import raytracer.primitive.Vector;
import raytracer.scene.exception.SceneException;

public class SceneReader {

	private SceneReader() {
		throw new AssertionError();
	}

	public static Scene readSceneFile(String fileName) throws IOException, SceneException {

		File file = new File(fileName);
		Scene scene = new Scene();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readValue(file, JsonNode.class);
			parseCamera(rootNode, scene);
			parseSimpleLighting(rootNode, scene);
		}
		catch (JsonParseException e) {
			throw new SceneException("Invalid JSON format");
		}
		catch (JsonMappingException e) {
			throw new SceneException("Invalid scene format");
		}
		return scene;
	}

	private static void parseCamera(JsonNode rootNode, Scene scene) throws SceneException {

		JsonNode cameraNode = rootNode.get("camera");
		if (cameraNode == null) {
			throw new SceneException("Camera configuration not present");
		}

		JsonNode cameraOrigin = cameraNode.get("origin");
		if (cameraOrigin == null) {
			throw new SceneException("Camera origin not present");
		}
		scene.setCameraOrigin(parseNodeToPoint(cameraOrigin, "Camera origin"));

		JsonNode cameraTarget = cameraNode.get("target");
		if (cameraTarget == null) {
			throw new SceneException("Camera target not present");
		}
		scene.setCameraTarget(parseNodeToPoint(cameraTarget, "Camera target"));

		JsonNode cameraUp = cameraNode.get("up");
		if (cameraUp == null) {
			throw new SceneException("Camera up not present");
		}
		scene.setCameraUp(parseNodeToVector(cameraUp, "Camera up"));

		JsonNode cameraAngle = cameraNode.get("angle");
		if (cameraAngle == null) {
			throw new SceneException("Camera angle not present");
		}
		scene.setCameraAngle((float) cameraAngle.asDouble());

		JsonNode cameraResolution = cameraNode.get("resolution");
		if (cameraResolution == null) {
			throw new SceneException("Camera resolution not present");
		}
		JsonNode xResolution = cameraResolution.path(0);
		JsonNode yResolution = cameraResolution.path(1);
		if (xResolution.isMissingNode() || yResolution.isMissingNode()) {
			throw new SceneException("Invalid camera resolution");
		}
		scene.setCameraResolutionX(xResolution.asInt());
		scene.setCameraResolutionY(yResolution.asInt());
	}

	private static void parseSimpleLighting(JsonNode rootNode, Scene scene) throws SceneException {

		JsonNode background = rootNode.get("background");
		if (background.isMissingNode()) {
			throw new SceneException("Background color not present");
		}
		scene.setBackground(parseNodeToColor(background, "Background color"));

		JsonNode ambientLight = rootNode.get("ambient-light");
		if (ambientLight.isMissingNode()) {
			throw new SceneException("Ambient light not present");
		}
		scene.setAmbientLight(parseNodeToLight(ambientLight, "Ambient light"));
		if (!scene.getAmbientLight().isAmbientLight()) {
			throw new SceneException("Ambient light should not have a position");
		}
	}

	private static Vector parseNodeToVector(JsonNode array, String id) throws SceneException {

		JsonNode xNode = array.path(0);
		JsonNode yNode = array.path(1);
		JsonNode zNode = array.path(2);
		if (xNode.isMissingNode() || yNode.isMissingNode() || zNode.isMissingNode()) {
			throw new SceneException(id + " could not be parsed as a vector");
		}
		return new Vector((float) xNode.asDouble(), (float) yNode.asDouble(), (float) zNode.asDouble());
	}

	private static Point parseNodeToPoint(JsonNode array, String id) throws SceneException {

		JsonNode xNode = array.path(0);
		JsonNode yNode = array.path(1);
		JsonNode zNode = array.path(2);
		if (xNode.isMissingNode() || yNode.isMissingNode() || zNode.isMissingNode()) {
			throw new SceneException(id + " could not be parsed as a point");
		}
		return new Point((float) xNode.asDouble(), (float) yNode.asDouble(), (float) zNode.asDouble());
	}

	private static MaterialColor parseNodeToColor(JsonNode array, String id) throws SceneException {

		JsonNode redNode = array.path(0);
		JsonNode greenNode = array.path(1);
		JsonNode blueNode = array.path(2);
		if (redNode.isMissingNode() || greenNode.isMissingNode() || blueNode.isMissingNode()) {
			throw new SceneException(id + " could not be parsed as a color");
		}
		return new MaterialColor(redNode.asInt(), greenNode.asInt(), blueNode.asInt());
	}

	private static Light parseNodeToLight(JsonNode array, String id) throws SceneException {

		JsonNode redNode = array.path(0);
		JsonNode greenNode = array.path(1);
		JsonNode blueNode = array.path(2);
		if (redNode.isMissingNode() || greenNode.isMissingNode() || blueNode.isMissingNode()) {
			throw new SceneException(id + " could not be parsed as a light");
		}
		JsonNode xNode = array.path(3);
		JsonNode yNode = array.path(4);
		JsonNode zNode = array.path(5);
		if (xNode.isMissingNode() || yNode.isMissingNode() || zNode.isMissingNode()) {
			return new Light(redNode.asInt(), greenNode.asInt(), blueNode.asInt());
		}
		else {
			return new Light(redNode.asInt(), greenNode.asInt(), blueNode.asInt(), (float) xNode.asDouble(),
					(float) yNode.asDouble(), (float) zNode.asDouble());
		}
	}

}

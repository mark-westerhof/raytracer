package raytracer.scene;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import raytracer.light.Light;
import raytracer.light.impl.AmbientLight;
import raytracer.material.Color;
import raytracer.material.Material;
import raytracer.material.PhongIllumination;
import raytracer.object.SceneObject;
import raytracer.object.impl.Sphere;
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
			parseLights(rootNode, scene);
			parseObjects(rootNode, scene);
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

	private static void parseLights(JsonNode rootNode, Scene scene) throws SceneException {

		JsonNode objectsNode = rootNode.get("lights");
		if (objectsNode != null) {
			Iterator<JsonNode> lightsIterator = objectsNode.getElements();
			while (lightsIterator.hasNext()) {
				scene.addLight(parseNodeToLight(lightsIterator.next()));
			}
		}
	}

	private static void parseObjects(JsonNode rootNode, Scene scene) throws SceneException {

		JsonNode objectsNode = rootNode.get("objects");
		if (objectsNode != null) {
			Iterator<JsonNode> objectsIterator = objectsNode.getElements();
			while (objectsIterator.hasNext()) {
				scene.addObject(parseNodeToObject(objectsIterator.next()));
			}
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

	private static Color parseNodeToColor(JsonNode array, String id) throws SceneException {

		JsonNode redNode = array.path(0);
		JsonNode greenNode = array.path(1);
		JsonNode blueNode = array.path(2);
		if (redNode.isMissingNode() || greenNode.isMissingNode() || blueNode.isMissingNode()) {
			throw new SceneException(id + " could not be parsed as a color");
		}
		return new Color((float) redNode.asDouble(), (float) greenNode.asDouble(), (float) blueNode.asDouble());
	}

	private static Light parseNodeToLight(JsonNode lightNode) throws SceneException {

		JsonNode typeNode = lightNode.get("type");
		JsonNode colorNode = lightNode.get("color");
		if (typeNode == null || colorNode == null) {
			throw new SceneException("Light is missing 'type' or 'color'");
		}

		JsonNode redNode = colorNode.path(0);
		JsonNode greenNode = colorNode.path(1);
		JsonNode blueNode = colorNode.path(2);
		if (redNode.isMissingNode() || greenNode.isMissingNode() || blueNode.isMissingNode()) {
			throw new SceneException("Light color is of invalid format");
		}

		if (typeNode.asText().equals("ambient")) {
			return new AmbientLight(redNode.asInt(), greenNode.asInt(), blueNode.asInt());
		}
		else if (typeNode.asText().equals("point")) {
			return null;
		}
		else {
			throw new SceneException("Invalid light type: '" + typeNode.asText() + "'");
		}
	}

	private static PhongIllumination parseNodeToPhong(JsonNode phongNode) throws SceneException {

		JsonNode ambientNode = phongNode.get("ambient");
		JsonNode diffuseNode = phongNode.get("diffuse");
		JsonNode specularNode = phongNode.get("specular");
		JsonNode shininessNode = phongNode.get("shininess");
		if (ambientNode == null || diffuseNode == null || specularNode == null || shininessNode == null) {
			throw new SceneException("Phong is missing 'ambient', 'diffuse', 'specular', or 'shininess'");
		}

		return new PhongIllumination(ambientNode.asLong(), diffuseNode.asLong(), specularNode.asLong(),
				shininessNode.asLong());
	}

	private static Material parseNodeToMaterial(JsonNode materialNode) throws SceneException {

		JsonNode colorNode = materialNode.get("color");
		JsonNode phongNode = materialNode.get("phong");
		JsonNode transmittance = materialNode.get("transmittance");
		JsonNode indexOfRefraction = materialNode.get("refraction");

		if (colorNode == null || phongNode == null || transmittance == null || indexOfRefraction == null) {
			throw new SceneException("Material is missing 'color', 'phong', 'transmittance', or 'refraction'");
		}

		return new Material(parseNodeToColor(colorNode, "Material"), parseNodeToPhong(phongNode),
				transmittance.asLong(), indexOfRefraction.asLong());
	}

	private static Sphere parseNodeToSphere(JsonNode sphereNode) throws SceneException {

		JsonNode originNode = sphereNode.get("origin");
		JsonNode radiusNode = sphereNode.get("radius");
		JsonNode materialNode = sphereNode.get("material");

		if (originNode == null || radiusNode == null || materialNode == null) {
			throw new SceneException("Sphere is missing 'origin', 'radius', or 'material'");
		}
		return new Sphere(parseNodeToPoint(originNode, "Sphere origin"), radiusNode.asLong(),
				parseNodeToMaterial(materialNode));

	}

	private static SceneObject parseNodeToObject(JsonNode objectNode) throws SceneException {

		JsonNode objectType = objectNode.get("type");
		JsonNode sceneObjectNode = objectNode.get("object");

		if (objectType == null || sceneObjectNode == null) {
			throw new SceneException("Object is missing 'type' or 'object'");
		}

		if (objectType.asText().equals("sphere")) {
			return parseNodeToSphere(sceneObjectNode);
		}
		else {
			throw new SceneException("Invalid object type: '" + objectType.asText() + "'");
		}
	}
}

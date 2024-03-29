package scene;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import light.Light;
import light.impl.AmbientLight;
import light.impl.PointLight;
import material.Color;
import material.Material;
import material.PhongIllumination;
import object.SceneObject;
import object.impl.Sphere;
import object.impl.TriangleMesh;
import primitive.Point;
import primitive.Vector;
import scene.exception.SceneException;
import scene.obj.OBJReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			parseObjects(rootNode, scene, file);
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
			Iterator<JsonNode> lightsIterator = objectsNode.elements();
			while (lightsIterator.hasNext()) {
				scene.addLight(parseNodeToLight(lightsIterator.next()));
			}
		}
	}

	private static void parseObjects(JsonNode rootNode, Scene scene, File sceneFile) throws SceneException {

		JsonNode objectsNode = rootNode.get("objects");
		if (objectsNode != null) {
			Iterator<JsonNode> objectsIterator = objectsNode.elements();
			while (objectsIterator.hasNext()) {
				scene.addObject(parseNodeToObject(objectsIterator.next(), sceneFile));
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
			return new AmbientLight((float) redNode.asDouble(), (float) greenNode.asDouble(),
					(float) blueNode.asDouble());
		}
		else if (typeNode.asText().equals("point")) {
			JsonNode originNode = lightNode.get("origin");
			if (originNode == null) {
				throw new SceneException("Point light is missing 'origin'");
			}
			return new PointLight((float) redNode.asDouble(), (float) greenNode.asDouble(),
					(float) blueNode.asDouble(), parseNodeToPoint(originNode, "Point light origin"));
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

		return new PhongIllumination((float) ambientNode.asDouble(), (float) diffuseNode.asDouble(),
				(float) specularNode.asDouble(), (float) shininessNode.asDouble());
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
				(float) transmittance.asDouble(), (float) indexOfRefraction.asDouble());
	}

	private static Sphere parseNodeToSphere(JsonNode sphereNode) throws SceneException {

		JsonNode originNode = sphereNode.get("origin");
		JsonNode radiusNode = sphereNode.get("radius");
		JsonNode materialNode = sphereNode.get("material");

		if (originNode == null || radiusNode == null || materialNode == null) {
			throw new SceneException("Sphere is missing 'origin', 'radius', or 'material'");
		}
		return new Sphere(parseNodeToPoint(originNode, "Sphere origin"), (float) radiusNode.asDouble(),
				parseNodeToMaterial(materialNode));

	}

	private static TriangleMesh parseNodeToTriangleMesh(JsonNode triangleMeshNode, File sceneFile)
			throws SceneException {

		JsonNode fileNameNode = triangleMeshNode.get("file");
		JsonNode materialNode = triangleMeshNode.get("material");

		if (fileNameNode == null || materialNode == null) {
			throw new SceneException("Triangle mesh is missing 'file' or 'material");
		}

		TriangleMesh triangleMesh = OBJReader.readOBJFile(fileNameNode.asText(), sceneFile);
		triangleMesh.setMaterial(parseNodeToMaterial(materialNode));
		return triangleMesh;
	}

	private static SceneObject parseNodeToObject(JsonNode objectNode, File sceneFile) throws SceneException {

		JsonNode objectType = objectNode.get("type");
		JsonNode sceneObjectNode = objectNode.get("object");

		if (objectType == null || sceneObjectNode == null) {
			throw new SceneException("Object is missing 'type' or 'object'");
		}

		if (objectType.asText().equals("sphere")) {
			return parseNodeToSphere(sceneObjectNode);
		}
		else if (objectType.asText().equals("triangleMesh")) {
			return parseNodeToTriangleMesh(sceneObjectNode, sceneFile);
		}
		else {
			throw new SceneException("Invalid object type: '" + objectType.asText() + "'");
		}
	}
}

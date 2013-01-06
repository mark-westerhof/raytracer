package scene.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import object.impl.TriangleMesh;
import primitive.Point;
import scene.exception.SceneException;

public class OBJReader {

	private OBJReader() {
		throw new AssertionError();
	}

	public static TriangleMesh readOBJFile(String fileName, File sceneFile) throws SceneException {
		// OBJ file is relative to scene file
		File file = new File(sceneFile.getParent(), fileName);
		try {
			FileReader fileReader = new FileReader(file);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {

				List<Point> vertices = new ArrayList<Point>();
				List<OBJTextureCoordinate> textureCoordinates = new ArrayList<OBJTextureCoordinate>();
				List<OBJFace> faces = new ArrayList<OBJFace>();

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					String split[] = line.split("\\s+");

					if (split[0].equals("v")) {
						Point point = new Point(Float.parseFloat(split[1]), Float.parseFloat(split[2]),
								Float.parseFloat(split[3]));
						vertices.add(point);
					}
					else if (split[0].equals("vt")) {
						OBJTextureCoordinate textureCoordinate = new OBJTextureCoordinate(Float.parseFloat(split[1]),
								Float.parseFloat(split[2]));
						textureCoordinates.add(textureCoordinate);

					}

					else if (split[0].equals("f")) {
						// Contains texture coordinates
						if (split[1].contains("/")) {
							String split1[] = split[1].split("/");
							String split2[] = split[2].split("/");
							String split3[] = split[3].split("/");

							int v1 = Integer.parseInt(split1[0]) - 1;
							int vt1 = Integer.parseInt(split1[1]) - 1;
							if ((v1 + 1) > vertices.size() || v1 < 0 || (vt1 + 1) > textureCoordinates.size()
									|| vt1 < 0) {
								throw new Exception();
							}
							int v2 = Integer.parseInt(split2[0]) - 1;
							int vt2 = Integer.parseInt(split2[1]) - 1;
							if ((v2 + 1) > vertices.size() || v2 < 0 || (vt2 + 1) > textureCoordinates.size()
									|| vt2 < 0) {
								throw new Exception();
							}
							int v3 = Integer.parseInt(split3[0]) - 1;
							int vt3 = Integer.parseInt(split3[1]) - 1;
							if ((v3 + 1) > vertices.size() || v3 < 0 || (vt3 + 1) > textureCoordinates.size()
									|| vt3 < 0) {
								throw new Exception();
							}

							OBJFace face = new OBJFace(new int[] { v1, v2, v3 }, new int[] { vt1, vt2, vt3 });
							faces.add(face);
						}
						// No texture coordinates
						else {
							int v1 = Integer.parseInt(split[1]) - 1;
							int v2 = Integer.parseInt(split[2]) - 1;
							int v3 = Integer.parseInt(split[3]) - 1;
							if (v1 + 1 > vertices.size() || v1 < 0 || v2 + 1 > vertices.size() || v2 < 0
									|| v3 + 1 > vertices.size() || v3 < 0) {
								throw new Exception();
							}
							OBJFace face = new OBJFace(new int[] { v1, v2, v3 });
							faces.add(face);
						}
					}
				}
				return new TriangleMesh(vertices, textureCoordinates, faces);
			}
			catch (IOException e) {
				throw new SceneException("Failed to parse OBJ file '" + fileName + "'");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new SceneException(e.getMessage());
			}
			finally {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
				}
			}
		}
		catch (FileNotFoundException e1) {
			throw new SceneException("OBJ file '" + fileName + "' does not exist");
		}
	}
}

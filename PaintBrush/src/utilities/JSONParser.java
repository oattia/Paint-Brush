package utilities;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;
import javax.json.Json;
import javax.json.stream.JsonParser;
import essential.shapes.*;

public class JSONParser {

	private static Hashtable<String, Class<?>> classes;

	public JSONParser(Hashtable<String, Class<?>> supportedShapesClasses) {
		classes = supportedShapesClasses;
	}

	public Vector<Shape> readJSON(File file) {

		boolean flag = false;

		Vector<Shape> container = new Vector<Shape>();
		Shape currentShape = null;
		try {

			JsonParser parser = Json.createParser(new FileReader(file));
			while (parser.hasNext()) {

				JsonParser.Event event = parser.next();

				switch (event) {
				case KEY_NAME: {

					String parsed = parser.getString();
					if (parsed.equals(Utils.line)) {
						currentShape = new Line();
						flag = true;
					} else if (!parsed.equals(Utils.fillColor)
							&& !parsed.equals(Utils.layoutColor)
							&& !parsed.equals(Utils.lowerLeft)
							&& !parsed.equals(Utils.pointsCounter)
							&& !parsed.equals(Utils.numOfPointsToDraw)
							&& !parsed.equals(Utils.upperRight)
							&& !parsed.equals(Utils.vertices)) {
						Class<?> toInstantiate = classes.get(parsed);

						if (toInstantiate != null) {
							currentShape = (Shape) toInstantiate.newInstance();
							flag = true;
						} else {
							flag = false;
						}
					}

					if (parsed.equals(Utils.fillColor) && flag) {
						event = parser.next();
						String state = parser.getString();
						if (!state.equals("null")) {
							currentShape.setFillColor(new Color(Integer
									.parseInt(state)));
						}
						continue;
					}

					if (parsed.equals(Utils.layoutColor) && flag) {
						event = parser.next();
						currentShape.setLayoutColor(new Color(Integer
								.parseInt(parser.getString())));
						continue;
					}

					if (parsed.equals(Utils.numOfPointsToDraw) && flag) {
						event = parser.next();
						currentShape.setNumOfPointsToDraw(Integer
								.parseInt(parser.getString()));
						continue;
					}

					if (parsed.equals(Utils.pointsCounter) && flag) {
						event = parser.next();
						currentShape.setPointsCounter(Integer.parseInt(parser
								.getString()));
						continue;
					}

					if (parsed.equals(Utils.upperRight) && flag) {
						event = parser.next();
						ConicSection section = (ConicSection) currentShape;
						section.setUpperRight(creatPoint(parser.getString()));
						continue;
					}

					if (parsed.equals(Utils.lowerLeft) && flag) {
						event = parser.next();
						ConicSection section = (ConicSection) currentShape;
						section.setLowerLeft(creatPoint(parser.getString()));
						continue;
					}

					if (parsed.equals(Utils.vertices) && flag) {
						event = parser.next();
						if (currentShape instanceof Line) {
							Line line = (Line) currentShape;
							line.setVertices(creatVertices(parser.getString()));
						} else if (currentShape instanceof Polygon) {
							Polygon poly = (Polygon) currentShape;
							poly.setVertices(creatVertices(parser.getString()));
						}
						continue;
					}
				}
					break;

				case END_OBJECT: {
					if (currentShape != null) {
						container.add(currentShape);
						currentShape = null;
					}
				}
					break;

				default: {
				}
					break;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return container;

	}

	private int[][] creatVertices(String data) {

		String[] splitted = data.split(", ");
		int[][] newVertices = new int[2][splitted.length / 2];

		int countRows = 0, countColumns = 0;

		for (int i = 0; i < splitted.length; i++) {
			newVertices[countRows][countColumns] = Integer
					.parseInt(splitted[i]);

			if (countColumns + 1 >= splitted.length / 2) {
				countColumns = 0;
				countRows++;
			} else {
				countColumns++;
			}
		}
		return newVertices;
	}

	private Point creatPoint(String data) {
		String[] splitted = data.split(", ");
		Point newPoint = new Point(Integer.parseInt(splitted[0]),
				Integer.parseInt(splitted[1]));
		return newPoint;
	}
}

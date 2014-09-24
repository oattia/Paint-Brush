package utilities;

import java.awt.Color;
import java.awt.Point;
import java.io.*;
import java.util.*;

import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import essential.shapes.*;

public class XMLParser {

	private static Hashtable<String, Class<?>> classes;

	public XMLParser(Hashtable<String, Class<?>> supportedShapesClasses) {
		classes = supportedShapesClasses;
	}

	public Vector<Shape> readXML(File xmlFile) {

		boolean flag = false;

		Vector<Shape> container = new Vector<Shape>();
		Shape currentShape = null;

		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(xmlFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {

					StartElement startElement = event.asStartElement();

					String parsed = startElement.getName().getLocalPart();

					if (parsed.equals("shapes")) {
						flag = false;
						continue;
					} else if (parsed.equals(Utils.line)) {
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
						event = eventReader.nextEvent();
						String state = event.asCharacters().getData();
						if (!state.equals("null")) {
							currentShape.setFillColor(new Color(Integer
									.parseInt(event.asCharacters().getData())));
						}
						continue;
					}

					if (parsed.equals(Utils.layoutColor) && flag) {
						event = eventReader.nextEvent();
						currentShape.setLayoutColor(new Color(Integer
								.parseInt(event.asCharacters().getData())));
						continue;
					}

					if (parsed.equals(Utils.numOfPointsToDraw) && flag) {
						event = eventReader.nextEvent();
						currentShape.setNumOfPointsToDraw(Integer
								.parseInt(event.asCharacters().getData()));
						continue;
					}

					if (parsed.equals(Utils.pointsCounter) && flag) {
						event = eventReader.nextEvent();
						currentShape.setPointsCounter(Integer.parseInt(event
								.asCharacters().getData()));
						continue;
					}

					if (parsed.equals(Utils.upperRight) && flag) {
						event = eventReader.nextEvent();
						ConicSection section = (ConicSection) currentShape;
						section.setUpperRight(creatPoint(event.asCharacters()
								.getData()));
						continue;
					}

					if (parsed.equals(Utils.lowerLeft) && flag) {
						event = eventReader.nextEvent();
						ConicSection section = (ConicSection) currentShape;
						section.setLowerLeft(creatPoint(event.asCharacters()
								.getData()));
						continue;
					}

					if (parsed.equals(Utils.vertices) && flag) {
						event = eventReader.nextEvent();
						if (currentShape instanceof Line) {
							Line line = (Line) currentShape;
							line.setVertices(creatVertices(event.asCharacters()
									.getData()));
						} else if (currentShape instanceof Polygon) {
							Polygon poly = (Polygon) currentShape;
							poly.setVertices(creatVertices(event.asCharacters()
									.getData()));
						}
						continue;
					}

				}
				if (event.isEndElement()) {

					EndElement endElement = event.asEndElement();
					String parsed = endElement.getName().getLocalPart();

					if (classes.get(parsed) != null
							|| parsed.equals(Utils.line)) {
						container.add(currentShape);
					}

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
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

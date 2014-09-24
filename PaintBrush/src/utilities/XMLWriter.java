package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import essential.shapes.*;

public class XMLWriter {

	private XMLOutputFactory outputFactory;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;
	private XMLEvent end;
	private XMLEvent tab;

	public XMLWriter(File file) {
		try {
			outputFactory = XMLOutputFactory.newInstance();
			eventWriter = outputFactory
					.createXMLEventWriter(new FileOutputStream(file));
			eventFactory = XMLEventFactory.newInstance();
			end = eventFactory.createDTD("\n");
			tab = eventFactory.createDTD("\t");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}

	private void createElement(String name, String value) {
		try {
			// create Start node
			StartElement sElement = eventFactory.createStartElement("", "",
					name);
			eventWriter.add(tab);
			eventWriter.add(sElement);
			// create Content
			Characters characters = eventFactory.createCharacters(value);
			eventWriter.add(characters);
			// create End node
			EndElement eElement = eventFactory.createEndElement("", "", name);
			eventWriter.add(eElement);
			eventWriter.add(end);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public void writeXML(Vector<Shape> container) {

		try {
			// create and write Start Tag
			eventWriter.add(eventFactory.createStartDocument());
			eventWriter.add(end);

			eventWriter.add(eventFactory.createStartElement("", "", "shapes"));
			eventWriter.add(end);

			// iterate over container

			for (Shape s : container) {

				if (s instanceof ConicSection) {
					eventWriter.add(eventFactory.createStartElement("", "", s
							.getClass().getSimpleName()));

					eventWriter.add(end);

					// write ConicSection's attributes
					ConicSection section = (ConicSection) s;
					createElement(Utils.lowerLeft, section.getLowerLeft().x
							+ ", " + section.getLowerLeft().y);
					createElement(Utils.upperRight, section.getUpperRight().x
							+ ", " + section.getUpperRight().y);
					createElement(Utils.fillColor,
							s.getFillColor() == null ? "null" : ""
									+ s.getFillColor().getRGB());
					createElement(Utils.layoutColor, ""
							+ s.getLayoutColor().getRGB());
					createElement(Utils.numOfPointsToDraw,
							"" + s.getNumOfPointsToDraw());
					createElement(Utils.pointsCounter,
							"" + s.getPointsCounter());

					eventWriter.add(eventFactory.createEndElement("", "", s
							.getClass().getSimpleName()));
					eventWriter.add(end);

				} else if (s instanceof Line) {
					eventWriter.add(eventFactory.createStartElement("", "",
							Utils.line));
					eventWriter.add(end);

					// write line's attributes
					Line tempLine = (Line) s;
					createElement(Utils.vertices, tempLine.vertToString());
					createElement(Utils.fillColor,
							s.getFillColor() == null ? "null" : ""
									+ s.getFillColor().getRGB());
					createElement(Utils.layoutColor, ""
							+ s.getLayoutColor().getRGB());
					createElement(Utils.numOfPointsToDraw,
							"" + s.getNumOfPointsToDraw());
					createElement(Utils.pointsCounter,
							"" + s.getPointsCounter());

					eventWriter.add(eventFactory.createEndElement("", "",
							Utils.line));
					eventWriter.add(end);
				} else if (s instanceof Polygon) {

					eventWriter.add(eventFactory.createStartElement("", "", s
							.getClass().getSimpleName()));
					eventWriter.add(end);
					// write polygon's attributes
					Polygon poly = (Polygon) s;
					createElement(Utils.vertices, poly.vertToString());
					createElement(Utils.fillColor,
							s.getFillColor() == null ? "null" : ""
									+ s.getFillColor().getRGB());
					createElement(Utils.layoutColor, ""
							+ s.getLayoutColor().getRGB());
					createElement(Utils.numOfPointsToDraw,
							"" + s.getNumOfPointsToDraw());
					createElement(Utils.pointsCounter,
							"" + s.getPointsCounter());

					eventWriter.add(eventFactory.createEndElement("", "", s
							.getClass().getSimpleName()));
					eventWriter.add(end);
				} else {
					// not supported shape
				}
			}

			eventWriter.add(eventFactory.createEndElement("", "", "shapes"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}

}

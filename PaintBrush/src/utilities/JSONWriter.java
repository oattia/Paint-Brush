package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import essential.shapes.*;

public class JSONWriter {

	public JSONWriter() {

	}

	public void writeJSON(File file, Vector<Shape> container) {

		Map<String, Object> config = new HashMap<String, Object>(1);
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonGeneratorFactory factory = Json.createGeneratorFactory(config);

		JsonGenerator gen = null;

		try {
			gen = factory.createGenerator(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		gen.writeStartObject();

		for (Shape s : container) {

			if (s instanceof ConicSection) {

				gen.writeStartObject(s.getClass().getSimpleName());
				ConicSection section = (ConicSection) s;
				gen.write(Utils.lowerLeft, section.getLowerLeft().x + ", "
						+ section.getLowerLeft().y);
				gen.write(Utils.upperRight, section.getUpperRight().x + ", "
						+ section.getUpperRight().y);
				gen.write(Utils.fillColor, s.getFillColor() == null ? "null"
						: "" + s.getFillColor().getRGB());
				gen.write(Utils.layoutColor, "" + s.getLayoutColor().getRGB());
				gen.write(Utils.numOfPointsToDraw,
						"" + s.getNumOfPointsToDraw());
				gen.write(Utils.pointsCounter, "" + s.getPointsCounter());

				gen.writeEnd();

			} else if (s instanceof Line) {
				gen.writeStartObject(Utils.line);

				Line line = (Line) s;
				gen.write(Utils.vertices, line.vertToString());
				gen.write(Utils.fillColor, s.getFillColor() == null ? "null"
						: "" + s.getFillColor().getRGB());
				gen.write(Utils.layoutColor, "" + s.getLayoutColor().getRGB());
				gen.write(Utils.numOfPointsToDraw,
						"" + s.getNumOfPointsToDraw());
				gen.write(Utils.pointsCounter, "" + s.getPointsCounter());

				gen.writeEnd();
			} else if (s instanceof Polygon) {
				gen.writeStartObject(s.getClass().getSimpleName());
				Polygon poly = (Polygon) s;
				gen.write(Utils.vertices, poly.vertToString());
				gen.write(Utils.fillColor, s.getFillColor() == null ? "null"
						: "" + s.getFillColor().getRGB());
				gen.write(Utils.layoutColor, "" + s.getLayoutColor().getRGB());
				gen.write(Utils.numOfPointsToDraw,
						"" + s.getNumOfPointsToDraw());
				gen.write(Utils.pointsCounter, "" + s.getPointsCounter());

				gen.writeEnd();

				break;

			}

		}

		gen.writeEnd().close();
	}
}

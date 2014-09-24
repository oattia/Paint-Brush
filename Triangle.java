package secondary.shapes;

import essential.shapes.Polygon;

@SuppressWarnings("serial")
public class Triangle extends Polygon {

	// constructor
	public Triangle() {
		setNumOfPointsToDraw(3);
		setPointsCounter(0);
		vertices = new int[2][3];
		vertices[0][0] = -1;
		vertices[1][0] = -1;
		vertices[0][1] = -1;
		vertices[1][1] = -1;
		vertices[0][2] = -1;
		vertices[1][2] = -1;
	}

	// constructor
	public Triangle(int x[], int y[]) {
		setNumOfPointsToDraw(3);
		setPointsCounter(getNumOfPointsToDraw());
		vertices = new int[2][3];
		for (int i = 0; i < 3; i++) {
			vertices[0][i] = x[i];
			vertices[1][i] = y[i];
		}
	}
	
	// constructor
	public Triangle(Triangle p) {
		setVertices(p.getVertices());
		setFillColor(p.getFillColor());
		setLayoutColor(p.getLayoutColor());
	}

	public void drawNextPoint(int x, int y) {
		if (getPointsCounter() < getNumOfPointsToDraw()) {
			updatePointNo(getPointsCounter(), x, y);
			setPointsCounter(getPointsCounter() + 1);
		}
	}

	public void updateNextPoint(int x, int y) {
		if (getPointsCounter() != getNumOfPointsToDraw())
			updatePointNo(getPointsCounter(), x, y);
	}

	public void updatePointNo(int pointNo, int x, int y) {
		if (getPointsCounter() == 0) {
			for (int i = 0; i < getNumOfPointsToDraw(); i++) {
				vertices[0][i] = x;
				vertices[1][i] = y;
			}
		} else {
			vertices[0][pointNo - 1] = x;
			vertices[1][pointNo - 1] = y;
		}
	}
}

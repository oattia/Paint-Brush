package essential.shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

@SuppressWarnings("serial")
public class Line extends Shape {

	private int[][] vertices;

	public Line() {
		vertices = new int[2][2];
		setNumOfPointsToDraw(2);
		setPointsCounter(0);
		vertices[0][0] = -1;
		vertices[1][0] = -1;
		vertices[0][1] = -1;
		vertices[1][1] = -1;
	}

	public Line(int x[], int y[]) {
		vertices = new int[2][2];
		setNumOfPointsToDraw(2);
		setPointsCounter(2);
		vertices[0][0] = x[0];
		vertices[1][0] = y[0];
		vertices[0][1] = x[1];
		vertices[1][1] = y[1];
	}

	public Line(Line l) {
		setVertices(l.getVertices());
		setFillColor(l.getFillColor());
		setLayoutColor(l.getLayoutColor());
	}

	public boolean containsPoint(int x, int y) { // needs modification
		return false;
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
			for (int i = 0; i < vertices[0].length; i++) {
				vertices[0][i] = x;
				vertices[1][i] = y;
			}
		} else {
			vertices[0][pointNo] = x;
			vertices[1][pointNo] = y;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getLayoutColor());
		g2d.drawLine(vertices[0][0], vertices[1][0], vertices[0][1],
				vertices[1][1]);
		super.paintComponent(g2d);
	}

	public java.awt.Rectangle getBorderBounds() {
		return new Line2D.Double(vertices[0][0], vertices[1][0],
				vertices[0][1], vertices[1][1]).getBounds();
	}

	@Override
	public void resizeShape(int cursorPosition, int XComponent, int YComponent) {
	}

	public void moveShape(int XChange, int YChange) {
		for (int i = 0; i < vertices[0].length; i++) { // updating point by
			vertices[0][i] += XChange;
			vertices[1][i] += YChange;
		}
	}

	public String vertToString() {

		String toReturn = "";
		for (int i = 0; i < vertices.length; i++) {
			for (int j = 0; j < vertices[i].length; j++) {
				toReturn += vertices[i][j] + ", ";
			}
		}
		return toReturn;
	}

	public void setVertices(int[][] creatVertices) {
		vertices = creatVertices;
		for (int i = 0; i < creatVertices[0].length; i++) {
			vertices[0][i] = creatVertices[0][i];
			vertices[1][i] = creatVertices[1][i];
		}
	}

	public int[][] getVertices() {
		return vertices;
	}
}

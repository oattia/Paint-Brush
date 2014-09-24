package essential.shapes;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

@SuppressWarnings("serial")
public abstract class Polygon extends Shape {

	protected int[][] vertices;

	public boolean containsPoint(int x, int y) {
		return new java.awt.Polygon(vertices[0], vertices[1],
				vertices[0].length).contains(new Point(x, y));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (getFillColor() == null && getLayoutColor() != null) {
			g2d.setColor(getLayoutColor());
			g2d.drawPolygon(vertices[0], vertices[1], vertices[0].length);
		} else {
			g2d.setColor(getFillColor());
			g2d.fillPolygon(vertices[0], vertices[1], vertices[0].length);
			if (getLayoutColor() != null) {
				g2d.setColor(getLayoutColor());
				g2d.drawPolygon(vertices[0], vertices[1], vertices[0].length);
			}
		}
		super.paintComponent(g2d);
	}

	public java.awt.Rectangle getBorderBounds() {
		return new java.awt.Rectangle(getShapeX() - 10, getShapeY() - 10,
				getShapeWidth() + 20, getShapeHeight() + 20);
	}

	public void resizeShape(int cursorPosition, int XChange, int YChange) {
		int changingPointX = 0, changingPointY = 0;
		int fixedPointX = 0, fixedPointY = 0;
		switch (cursorPosition) {
		case Cursor.S_RESIZE_CURSOR:
			changingPointX = (int) (getShapeX() + 0.5 * getShapeWidth());
			changingPointY = getShapeY() + getShapeHeight();
			fixedPointX = changingPointX;
			fixedPointY = changingPointY - getShapeHeight();
			break;
		case Cursor.N_RESIZE_CURSOR:
			changingPointX = (int) (getShapeX() + 0.5 * getShapeWidth());
			changingPointY = getShapeY();
			fixedPointX = changingPointX;
			fixedPointY = changingPointY + getShapeHeight();
			break;

		case Cursor.E_RESIZE_CURSOR:
			changingPointX = getShapeX() + getShapeWidth();
			changingPointY = (int) (getShapeY() + 0.5 * getShapeHeight());
			fixedPointX = changingPointX - getShapeWidth();
			fixedPointY = changingPointY;
			break;

		case Cursor.W_RESIZE_CURSOR:
			changingPointX = getShapeX();
			changingPointY = (int) (getShapeY() + 0.5 * getShapeHeight());
			fixedPointX = changingPointX + getShapeWidth();
			fixedPointY = changingPointY;
			break;

		case Cursor.SE_RESIZE_CURSOR:
			changingPointX = getShapeX() + getShapeWidth();
			changingPointY = getShapeY() + getShapeHeight();
			fixedPointY = changingPointY - getShapeHeight();
			fixedPointX = changingPointX - getShapeWidth();
			break;

		case Cursor.NW_RESIZE_CURSOR:
			changingPointX = getShapeX();
			changingPointY = getShapeY();
			fixedPointY = changingPointY + getShapeHeight();
			fixedPointX = changingPointX + getShapeWidth();
			break;

		case Cursor.NE_RESIZE_CURSOR:
			changingPointX = getShapeX() + getShapeWidth();
			changingPointY = getShapeY();
			fixedPointY = changingPointY + getShapeHeight();
			fixedPointX = changingPointX - getShapeWidth();
			break;

		case Cursor.SW_RESIZE_CURSOR:
			changingPointX = getShapeX();
			changingPointY = getShapeY() + getShapeHeight();
			fixedPointX = changingPointX + getShapeWidth();
			fixedPointY = changingPointY - getShapeHeight();
			break;
		}

		for (int i = 0; i < vertices[0].length; i++) { // updating point by
														// point
			if ((changingPointX - fixedPointX) != 0)
				vertices[0][i] = (vertices[0][i] - fixedPointX)
						* (changingPointX + XChange - fixedPointX)
						/ (changingPointX - fixedPointX) + fixedPointX;
			if ((changingPointY - fixedPointY) != 0)
				vertices[1][i] = (vertices[1][i] - fixedPointY)
						* (changingPointY + YChange - fixedPointY)
						/ (changingPointY - fixedPointY) + fixedPointY;
		}
	}

	public void moveShape(int XChange, int YChange) {
		for (int i = 0; i < vertices[0].length; i++) { // updating point by
			vertices[0][i] += XChange;
			vertices[1][i] += YChange;
		}
	}

	public int getShapeX() {
		return (int) new java.awt.Polygon(vertices[0], vertices[1],
				vertices[0].length).getBounds().getX();
	}

	public int getShapeY() {
		return (int) new java.awt.Polygon(vertices[0], vertices[1],
				vertices[0].length).getBounds().getY();
	}

	public int getShapeHeight() {
		return (int) new java.awt.Polygon(vertices[0], vertices[1],
				vertices[0].length).getBounds().getHeight();
	}

	public int getShapeWidth() {
		return (int) new java.awt.Polygon(vertices[0], vertices[1],
				vertices[0].length).getBounds().getWidth();
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
	}
	
	public int[][] getVertices() {
		int[][] verticesCopy = new int[2][vertices[0].length];
		for(int i=0; i<vertices[0].length; i++){
			verticesCopy[0][i] = vertices[0][i];
			verticesCopy[1][i] = vertices[1][i];
		}
		return verticesCopy;
	}
}

package essential.shapes;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public abstract class ConicSection extends Shape {

	private Point upperRight;
	private Point lowerLeft ;

	public boolean containsPoint(int x, int y) {
		return new Ellipse2D.Double(getUpperRight().x, getUpperRight().y, getLowerLeft().x - getUpperRight().x, getLowerLeft().y - getUpperRight().y).contains(new Point(x, y));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (getFillColor() == null && getLayoutColor() != null) {
			g2d.setColor(getLayoutColor());
			g2d.drawOval(getUpperRight().x, getUpperRight().y, getLowerLeft().x - getUpperRight().x, getLowerLeft().y - getUpperRight().y);
		} else {
			g2d.setColor(getFillColor());
			g2d.fillOval(getUpperRight().x, getUpperRight().y, getLowerLeft().x - getUpperRight().x, getLowerLeft().y - getUpperRight().y);
			
			if (getLayoutColor() != null) {
				g2d.setColor(getLayoutColor());
				g2d.drawOval(getUpperRight().x, getUpperRight().y, getLowerLeft().x - getUpperRight().x, getLowerLeft().y - getUpperRight().y);
			}
		}
		super.paintComponent(g2d);
	}

	public java.awt.Rectangle getBorderBounds() {
		return new Rectangle(getUpperRight().x-10, getUpperRight().y-10, getLowerLeft().x-getUpperRight().x+20, getLowerLeft().y-getUpperRight().y+20).getBounds();
	}

	public void moveShape(int XChange, int YChange) {
		getUpperRight().x += XChange;	getLowerLeft().x += XChange;
		getUpperRight().y += YChange;	getLowerLeft().y += YChange;	
	}
	
	
	public void resizeShape(int cursorPosition, int XChange, int YChange) {
		int changingPointX = 0, changingPointY = 0;
		int fixedPointX = 0, fixedPointY = 0;
		switch (cursorPosition) {
		case Cursor.S_RESIZE_CURSOR:
			changingPointX = (int) (getShapeX() + 0.5*getShapeWidth());
			changingPointY = getShapeY() + getShapeHeight();
			fixedPointX = changingPointX;		
			fixedPointY = changingPointY - getShapeHeight();
			break;
		case Cursor.N_RESIZE_CURSOR:
			changingPointX = (int) (getShapeX() + 0.5*getShapeWidth());
			changingPointY = getShapeY();
			fixedPointX = changingPointX;		
			fixedPointY = changingPointY + getShapeHeight();
			break;

		case Cursor.E_RESIZE_CURSOR:
			changingPointX = getShapeX() + getShapeWidth();
			changingPointY = (int) (getShapeY() + 0.5*getShapeHeight());
			fixedPointX = changingPointX - getShapeWidth();		
			fixedPointY = changingPointY;
			break;

		case Cursor.W_RESIZE_CURSOR:
			changingPointX = getShapeX();
			changingPointY = (int) (getShapeY() + 0.5*getShapeHeight());
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

		if ((changingPointX - fixedPointX) != 0)
		{
			getUpperRight().x = (getUpperRight().x - fixedPointX) * (changingPointX + XChange - fixedPointX)
					/ (changingPointX - fixedPointX) + fixedPointX;
			getLowerLeft().x = (getLowerLeft().x - fixedPointX) * (changingPointX + XChange - fixedPointX)
					/ (changingPointX - fixedPointX) + fixedPointX;
		}
		if ((changingPointY - fixedPointY) != 0)
		{
			getUpperRight().y = (getUpperRight().y - fixedPointY) * (changingPointY + YChange - fixedPointY)
					/ (changingPointY - fixedPointY) + fixedPointY;
			getLowerLeft().y = (getLowerLeft().y - fixedPointY) * (changingPointY + YChange - fixedPointY)
					/ (changingPointY - fixedPointY) + fixedPointY;
		}
	}

	public int getShapeX() {	return getUpperRight().x;	}

	public int getShapeY() {	return getUpperRight().y;	}

	public int getShapeHeight() {	return getLowerLeft().y - getUpperRight().y;	}

	public int getShapeWidth() 	{	return getLowerLeft().x - getUpperRight().x;	}

	public Point getUpperRight() {
		return upperRight;
	}

	public void setUpperRight(Point upperRight) {
		this.upperRight = upperRight;
	}

	public Point getLowerLeft() {
		return lowerLeft;
	}

	public void setLowerLeft(Point lowerLeft) {
		this.lowerLeft = lowerLeft;
	}
}

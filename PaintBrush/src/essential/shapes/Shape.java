package essential.shapes;
import java.awt.Color;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class Shape extends JLabel {
	
	private Color layoutColor = Color.black;
	private Color fillColor;
	private int pointsCounter;
	private int numOfPointsToDraw;

	public void setLayoutColor(Color c) {
		layoutColor = c;
	}

	public void setFillColor(Color c) {
		fillColor = c;
	}

	public int getNumOfPointsToDraw(){
		return numOfPointsToDraw;
	}
	
	public int getPointsCounter(){
		return pointsCounter;
	}
	
	public boolean isDonePainting(){
		return (getNumOfPointsToDraw() == getPointsCounter());
	}
	
	public void finishDrawing(){
		setPointsCounter(getNumOfPointsToDraw());
	}
	
	public abstract void moveShape(int XChange, int YChange);
	public abstract boolean containsPoint(int x, int y);
	public abstract void drawNextPoint(int x, int y);
	public abstract void updateNextPoint(int x, int y);
	public abstract java.awt.Rectangle getBorderBounds();
	public abstract void resizeShape(int cursorPosition, int XComponent, int YComponent);

	public void setNumOfPointsToDraw(int numOfPointsToDraw) {
		this.numOfPointsToDraw = numOfPointsToDraw;
	}

	public void setPointsCounter(int pointsCounter) {
		this.pointsCounter = pointsCounter;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getLayoutColor() {
		return layoutColor;
	}
}

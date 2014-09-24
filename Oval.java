package secondary.shapes;
import java.awt.Point;

import essential.shapes.ConicSection;

@SuppressWarnings("serial")
public class Oval extends ConicSection {

	// constructor
	public Oval() {
		setNumOfPointsToDraw(2);
		setPointsCounter(0);
		setUpperRight(new Point(-1, -1));
		setLowerLeft(new Point(-1, -1));
	}

	// constructor
	public Oval(int x[], int y[]) {
		setNumOfPointsToDraw(2);
		setPointsCounter(getNumOfPointsToDraw());
		setUpperRight(new Point(x[0], y[0]));
		setLowerLeft(new Point(x[1], y[1]));
	}
	
	public Oval(Oval c) {
		setUpperRight(new Point(c.getUpperRight().x, c.getUpperRight().y));
		setLowerLeft(new Point(c.getLowerLeft().x, c.getLowerLeft().y));
		setFillColor(c.getFillColor());
		setLayoutColor(c.getLayoutColor());
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
			getUpperRight().x = x;
			getUpperRight().y = y;
			getLowerLeft().x = x;
			getLowerLeft().y = y;
		} else {
			if (pointNo == 0)
				update(x, y, getLowerLeft().x, getLowerLeft().y);
			else
				update(getUpperRight().x, getUpperRight().y, x, y);
		}
	}

	private void update(int x1, int y1, int x2, int y2) {
		getUpperRight().x = x1;
		getUpperRight().y = y1;
		getLowerLeft().x = x2;
		getLowerLeft().y = y2;
	}
}

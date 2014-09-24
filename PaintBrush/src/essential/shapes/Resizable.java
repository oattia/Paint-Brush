package essential.shapes;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

// Resizable.java 

@SuppressWarnings("serial")
public class Resizable extends JComponent {

	private Shape selectedShape = null;
	boolean itemChangeFlag = false;

	public Resizable(Shape comp) {
		this(comp, new ResizableBorder(8));
	}

	public Resizable(Shape comp, ResizableBorder border) {
		setLayout(new BorderLayout());
		selectedShape = comp;
		addMouseListener(resizeListener);
		addMouseMotionListener(resizeListener);
		setBorder(border);
	}

	private void resize() {
		if (getParent() != null) {
			((JComponent) getParent()).revalidate();
		}
	}
	
	public void setItemChangeFlag(boolean l){
		itemChangeFlag = l;
	}
	
	public boolean getItemChangeFlag(){
		return itemChangeFlag;
	}

	MouseInputListener resizeListener = new MouseInputAdapter() {

		public void mouseMoved(MouseEvent me) {
			if (hasFocus()) {
				ResizableBorder border = (ResizableBorder) getBorder();
				setCursor(Cursor.getPredefinedCursor(border.getCursor(me)));
			}
		}

		public void mouseExited(MouseEvent mouseEvent) {
			setCursor(Cursor.getDefaultCursor());
		}

		private int cursor;
		private Point startPos = null;

		public void mousePressed(MouseEvent me) {
			ResizableBorder border = (ResizableBorder) getBorder();
			cursor = border.getCursor(me);
			startPos = me.getPoint();
			requestFocus();
			repaint();
		}

		public void mouseDragged(MouseEvent me) {

			if (startPos != null) {

				int x = getX();
				int y = getY();
				int w = getWidth();
				int h = getHeight();

				int dx = me.getX() - startPos.x;
				int dy = me.getY() - startPos.y;

				switch (cursor) {
				case Cursor.N_RESIZE_CURSOR:
					if (!(h - dy < 20)) {
						setBounds(x, y + dy, w, h - dy);
						selectedShape.resizeShape(cursor, 0, dy);
						resize();
					}
					break;

				case Cursor.S_RESIZE_CURSOR:
					if (!(h + dy < 20)) {
						setBounds(x, y, w, h + dy);
						selectedShape.resizeShape(cursor, 0, dy);
						startPos = me.getPoint();
						resize();
					}
					break;

				case Cursor.W_RESIZE_CURSOR:
					if (!(w - dx < 20)) {
						setBounds(x + dx, y, w - dx, h);
						selectedShape.resizeShape(cursor, dx, 0);
						resize();
					}
					break;

				case Cursor.E_RESIZE_CURSOR:
					if (!(w + dx < 20)) {
						setBounds(x, y, w + dx, h);
						selectedShape.resizeShape(cursor, dx, 0);
						startPos = me.getPoint();
						resize();
					}
					break;

				case Cursor.NW_RESIZE_CURSOR:
					if (!(w - dx < 20) && !(h - dy < 20)) {
						setBounds(x + dx, y + dy, w - dx, h - dy);
						selectedShape.resizeShape(cursor, dx, dy);
						resize();
					}
					break;

				case Cursor.NE_RESIZE_CURSOR:
					if (!(w + dx < 20) && !(h - dy < 20)) {
						setBounds(x, y + dy, w + dx, h - dy);
						selectedShape.resizeShape(cursor, dx, dy);
						startPos = new Point(me.getX(), startPos.y);
						resize();
					}
					break;

				case Cursor.SW_RESIZE_CURSOR:
					if (!(w - dx < 20) && !(h + dy < 20)) {
						setBounds(x + dx, y, w - dx, h + dy);
						selectedShape.resizeShape(cursor, dx, dy);
						startPos = new Point(startPos.x, me.getY());
						resize();
					}
					break;

				case Cursor.SE_RESIZE_CURSOR:
					if (!(w + dx < 20) && !(h + dy < 20)) {
						setBounds(x, y, w + dx, h + dy);
						selectedShape.resizeShape(cursor, dx, dy);
						startPos = me.getPoint();
						resize();
					}
					break;

				case Cursor.MOVE_CURSOR:
					Rectangle bounds = getBounds();
					bounds.translate(dx, dy);
					selectedShape.moveShape(dx, dy);
					setBounds(bounds);
					resize();
				}

				setCursor(Cursor.getPredefinedCursor(cursor));
				itemChangeFlag = true;
			}
		}

		public void mouseReleased(MouseEvent mouseEvent) {
			startPos = null;
		}
	};
}
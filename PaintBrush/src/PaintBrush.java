import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileFilter;
import utilities.JSONParser;
import utilities.JSONWriter;
import utilities.ShapesClassLoader;
import utilities.Utils;
import utilities.XMLParser;
import utilities.XMLWriter;
import essential.shapes.Line;
import essential.shapes.Resizable;
import essential.shapes.Shape;

public class PaintBrush {

	private static int NUM_OF_SHAPES;
	private static JFrame mainFrame;
	private static JPanel backgroundPanel;
	private static GridBagConstraints co;
	private static JScrollPane scrollArea;
	private static JLayeredPane layers;

	private static JMenuBar menubar;
	private static JMenu menu;
	private static JMenuItem saveItem;
	private static JMenuItem openItem;
	private static JMenuItem loadItem;

	private static Resizable tempR;
	private static int selectedShapeInd;
	private static int layerCount;
	private static JFileChooser fileChooser;

	private static JButton fillColorButton, undo, redo, clear;

	private static JLabel xLabel;
	private static JLabel yLabel;

	private static Vector<Shape> paintedShapes;
	private static Stack<Vector<Shape>> undoStack;
	private static Stack<Vector<Shape>> redoStack;

	private static Vector<JButton> shapesButtons;

	private static Hashtable<String, Class<?>> supportedShapesClasses;

	private static Vector<String> supportedShapesNames;

	public static void startPaintBrush() {

		paintedShapes = new Vector<Shape>(1);
		undoStack = new Stack<Vector<Shape>>();
		redoStack = new Stack<Vector<Shape>>();
		supportedShapesClasses = new Hashtable<String, Class<?>>();
		supportedShapesNames = new Vector<String>();
		layerCount = 0;
		selectedShapeInd = -1;

		// check for classes, load existing from (secondary/shapes)
		loadExistingClasses();

		// start creatAndShowGUI()
		createAndShowGUI();
	}

	private static Vector<Shape> copyShapesVector(Vector<Shape> orig) {

		Vector<Shape> copy = new Vector<Shape>();

		for (Shape s : orig) {

			// SAFE ZONE !

			if (s instanceof Line) {
				copy.add(new Line((Line) s));

			} else {

				Class<?> sClass = s.getClass();
				Constructor<?> copyConstructor = null;
				try {
					copyConstructor = sClass
							.getConstructor(new Class[] { sClass });
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}

				if (copyConstructor != null) {
					try {
						copy.add((Shape) copyConstructor.newInstance(s));
					} catch (InstantiationException | IllegalAccessException
							| IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return copy;
	}

	private static void loadExistingClasses() {

		try {
			File existingFiles[] = new File(PaintBrush.class.getResource(
					"secondary/shapes/").toURI()).listFiles();

			NUM_OF_SHAPES = existingFiles.length + 1; // one for the line

			ShapesClassLoader existingLoader = new ShapesClassLoader();

			for (int i = 0; i < existingFiles.length; i++) {
				Class<?> tempClass = existingLoader.loadClass(existingFiles[i]);
				supportedShapesClasses
						.put(tempClass.getSimpleName(), tempClass);
				supportedShapesNames.add(tempClass.getSimpleName());
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static ImageIcon createImageIcon(String path) {

		URL imgURL = PaintBrush.class.getResource(path);

		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}

	}

	private static void reDrawShapes() {

		layerCount = 0;
		layers.removeAll();
		layers.repaint();

		for (Shape s : paintedShapes) {
			s.setBounds(0, 0, 1000, 1000);
			layers.add(s, new Integer(layerCount++));
		}
		layers.repaint();
	}

	private static void createAndShowGUI() {

		mainFrame = new JFrame("PaintBrush");
		// setting layout
		co = new GridBagConstraints();
		co.fill = GridBagConstraints.HORIZONTAL;

		// setting background panel
		backgroundPanel = new JPanel(new GridBagLayout());

		// setting the file chooser
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// setting the menu bar
		menubar = new JMenuBar();
		menu = new JMenu("Menu");
		menubar.add(menu);

		saveItem = new JMenuItem("Save Work");
		saveItem.addActionListener(saveDataListener);
		menu.add(saveItem);

		openItem = new JMenuItem("Load Saved Data");
		openItem.addActionListener(loadDataListener);
		menu.add(openItem);

		loadItem = new JMenuItem("Load Plug-in");
		loadItem.addActionListener(pluginsListener);
		menu.add(loadItem);
		mainFrame.setJMenuBar(menubar);

		// setting buttons controlling shapes
		co.gridy = 0;

		shapesButtons = new Vector<JButton>(NUM_OF_SHAPES);

		JButton lineButton = new JButton("Line",
				createImageIcon("images/Line.png"));
		lineButton.setName("Line");
		lineButton.setToolTipText("Draw a new " + "Line");
		lineButton.setBackground(Color.LIGHT_GRAY);
		lineButton.addActionListener(ShapeButtonsListener);
		co.gridx = 0;
		backgroundPanel.add(lineButton, co);
		shapesButtons.add(lineButton);

		for (int i = 1; i < NUM_OF_SHAPES; i++) {
			JButton temp = new JButton(supportedShapesNames.get(i),
					createImageIcon("images/" + supportedShapesNames.get(i)
							+ ".png"));
			temp.setName(supportedShapesNames.get(i));
			temp.setToolTipText("Draw a new " + supportedShapesNames.get(i));
			temp.setBackground(Color.LIGHT_GRAY);
			temp.addActionListener(ShapeButtonsListener);
			co.gridx = i;
			backgroundPanel.add(temp, co);
			shapesButtons.add(temp);
		}

		// setting the fill-color button
		fillColorButton = new JButton(createImageIcon("images/Palette.png"));
		fillColorButton.setToolTipText("Select a shape and change the color");
		fillColorButton.setBackground(Color.LIGHT_GRAY);
		fillColorButton.addActionListener(showPalletListener);
		co.gridx = NUM_OF_SHAPES;
		backgroundPanel.add(fillColorButton, co);

		undoStack.push(copyShapesVector(paintedShapes));

		// setting the undo button
		undo = new JButton(createImageIcon("images/Undo.png"));
		undo.setToolTipText("Undo your last action");
		undo.setBackground(Color.LIGHT_GRAY);
		co.gridx = NUM_OF_SHAPES + 1;
		undo.addActionListener(undoListener);
		backgroundPanel.add(undo, co);

		// setting the redo button
		redo = new JButton(createImageIcon("images/Redo.png"));
		redo.setToolTipText("Redo your last undone action");
		redo.setBackground(Color.LIGHT_GRAY);
		co.gridx = NUM_OF_SHAPES + 2;
		redo.addActionListener(redoListener);
		backgroundPanel.add(redo, co);

		// setting the clear button
		clear = new JButton(createImageIcon("images/Clear.png"));
		clear.setToolTipText("Clear");
		clear.setBackground(Color.LIGHT_GRAY);
		co.gridx = NUM_OF_SHAPES + 3;
		clear.addActionListener(clearListener);
		backgroundPanel.add(clear, co);

		// setting the layered panes and scroll area
		layers = new JLayeredPane();
		layers.setOpaque(true);
		layers.setBackground(Color.white);
		layers.addMouseListener(canvasListener);
		layers.addMouseMotionListener(canvasListener);
		layers.setPreferredSize(new Dimension(1000, 1000));
		scrollArea = new JScrollPane(layers,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollArea.setPreferredSize(new Dimension(500, 500));
		co.gridx = 0;
		co.gridy = 1;
		co.gridwidth = NUM_OF_SHAPES + 10;
		co.gridheight = 1000;
		backgroundPanel.add(scrollArea, co);

		co.gridwidth = 1;
		co.gridheight = 1;

		xLabel = new JLabel(" ");
		co.gridx = 0;
		co.gridy = 1111;
		backgroundPanel.add(xLabel, co);

		yLabel = new JLabel(" ");
		co.gridx = 1;
		co.gridy = 1111;
		backgroundPanel.add(yLabel, co);

		// setting and showing the PaintBrush frame
		mainFrame.add(backgroundPanel);

		// setting the final state of the JFrame
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setIconImage(mainFrame.getToolkit().getImage(
				PaintBrush.class.getResource("images/ICON.gif")));
		mainFrame.setVisible(true);
	}

	private static ActionListener ShapeButtonsListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (!paintedShapes.isEmpty()) {
				if (paintedShapes.lastElement().getPointsCounter() == 0)
					paintedShapes.remove(paintedShapes.size() - 1);
				else {
					paintedShapes.lastElement().finishDrawing();
					undoStack.push(copyShapesVector(paintedShapes));
				}
			}

			if (selectedShapeInd != -1) {
				layers.remove(tempR);
				selectedShapeInd = -1;
				if (tempR.getItemChangeFlag()) {
					undoStack.push(copyShapesVector(paintedShapes));
					tempR.setItemChangeFlag(false);
				}
				layers.repaint();
			}
			layers.repaint();

			Shape s = null;
			String callSource = ((JButton) e.getSource()).getName();

			if (callSource.equals("Line")) {
				s = new Line();

			} else {
				for (String st : supportedShapesNames) {

					if (callSource.equals(st)) {

						Class<?> toInstantiate = supportedShapesClasses
								.get(callSource);

						try {
							s = (Shape) toInstantiate.newInstance();
						} catch (InstantiationException
								| IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				}

			}

			if (s != null) {
				s.setBounds(0, 0, 1000, 1000);
				layers.add(s, new Integer(layerCount++));
				paintedShapes.add(s);
				undoStack.push(new Vector<Shape>(paintedShapes));
				layers.repaint();
			}
		}
	};

	private static ActionListener showPalletListener = new ActionListener() {

		public void actionPerformed(ActionEvent event) {
			Color selectedColor = JColorChooser.showDialog(mainFrame,
					"Pick a Color", Color.GREEN);

			if (selectedShapeInd != -1) {
				paintedShapes.elementAt(selectedShapeInd).setFillColor(
						selectedColor);
				paintedShapes.elementAt(selectedShapeInd).repaint();
				undoStack.push(copyShapesVector(paintedShapes));
			}
		}
	};

	private static ActionListener clearListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (selectedShapeInd != -1) {
				paintedShapes.remove(selectedShapeInd);
				undoStack.push(copyShapesVector(paintedShapes));
				selectedShapeInd = -1;
				reDrawShapes();
			} else {
				layerCount = 0;
				layers.removeAll();
				layers.repaint();

			}
		}
	};

	private static ActionListener undoListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (undoStack.size() > 1) {
				redoStack.push(undoStack.pop());
				paintedShapes = copyShapesVector(undoStack.peek());
				reDrawShapes();
			} else {
				JOptionPane
						.showMessageDialog(mainFrame, "ERROR : Can not undo");
			}
		}
	};

	private static ActionListener redoListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!redoStack.isEmpty()) {
				undoStack.push(redoStack.pop());
				paintedShapes = copyShapesVector(undoStack.peek());
				reDrawShapes();
			} else {
				JOptionPane
						.showMessageDialog(mainFrame, "ERROR : Can not redo");
			}
		}
	};

	private static MouseInputListener canvasListener = new MouseInputAdapter() {

		@Override
		public void mouseMoved(MouseEvent e) {

			xLabel.setText("X = " + e.getX());
			yLabel.setText("Y = " + e.getY());

			if (!paintedShapes.isEmpty()
					&& !paintedShapes.lastElement().isDonePainting()) {
				paintedShapes.lastElement().updateNextPoint(e.getX(), e.getY());
				paintedShapes.lastElement().repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent m) {

			int alreadySelected = selectedShapeInd;

			if (!paintedShapes.isEmpty()
					&& !paintedShapes.lastElement().isDonePainting()) {

				paintedShapes.lastElement().drawNextPoint(m.getX(), m.getY());
				if (paintedShapes.lastElement().isDonePainting()) {
					undoStack.push(copyShapesVector(paintedShapes));
				}
			} else if (!paintedShapes.isEmpty()) {
				int i;
				selectedShapeInd = -1;
				for (i = paintedShapes.size() - 1; i >= 0; i--) {
					if (paintedShapes.get(i).containsPoint(m.getX(), m.getY())) {
						selectedShapeInd = i;
						break;
					}
				}

				if (selectedShapeInd != alreadySelected) {
					if (alreadySelected != -1) {
						layers.remove(tempR);
						layers.repaint();
						if (tempR.getItemChangeFlag()) {
							undoStack.push(copyShapesVector(paintedShapes));
							tempR.setItemChangeFlag(false);
						}
					}
					if (selectedShapeInd != -1) {
						tempR = new Resizable(
								paintedShapes.get(selectedShapeInd));
						tempR.setBounds(paintedShapes.get(selectedShapeInd)
								.getBorderBounds());
						layers.add(tempR);
						layers.repaint();
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			arg0.consume();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			xLabel.setText(" ");
			yLabel.setText(" ");
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			arg0.consume();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			arg0.consume();
		}

	};

	private static FileFilter DataFilter = new FileFilter() {

		@Override
		public String getDescription() {
			return "XML and JSON files";
		}

		@Override
		public boolean accept(File file) {

			if (file.isDirectory()) {
				return true;
			}

			String choiceExt = Utils.getExtension(file);

			if (choiceExt != null) {
				if (choiceExt.equals("xml") || choiceExt.equals("json")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	};

	private static ActionListener saveDataListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			fileChooser.removeChoosableFileFilter(classFilter);
			fileChooser.addChoosableFileFilter(DataFilter);

			int returnVal = fileChooser.showSaveDialog(mainFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();

				if (Utils.getExtension(file) == null) {
					JOptionPane
							.showMessageDialog(mainFrame,
									"Error: You Have To Input (.xml) or (.json) after your file's name");
				} else if (Utils.getExtension(file).equalsIgnoreCase("xml")) {

					try {
						file.getParentFile().mkdirs();
						file.createNewFile();
						XMLWriter writer = new XMLWriter(file);
						writer.writeXML(paintedShapes);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (Utils.getExtension(file).equalsIgnoreCase("json")) {
					try {
						file.getParentFile().mkdirs();
						file.createNewFile();
						JSONWriter writer = new JSONWriter();
						writer.writeJSON(file, paintedShapes);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					JOptionPane
							.showMessageDialog(mainFrame,
									"Error: This program only supports (.xml) and (.json) extentions");
				}

			}
		}
	};

	private static ActionListener loadDataListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			layerCount = 0;

			fileChooser.removeChoosableFileFilter(classFilter);
			fileChooser.addChoosableFileFilter(DataFilter);

			int returnVal = fileChooser.showOpenDialog(mainFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.exists()) {

					if (Utils.getExtension(file) == null) {
						JOptionPane.showMessageDialog(mainFrame,
								"Error: Unsupported Format!");
					} else if (Utils.getExtension(file).equals(Utils.xml)) {

						XMLParser parser = new XMLParser(supportedShapesClasses);
						paintedShapes = parser.readXML(file);
						reDrawShapes();

					} else if (Utils.getExtension(file).equals(Utils.json)) {

						JSONParser parser = new JSONParser(
								supportedShapesClasses);
						paintedShapes = parser.readJSON(file);
						reDrawShapes();
					}
				} else {
					JOptionPane.showMessageDialog(mainFrame,
							"Error: File Not Found!");
				}
			}
		}
	};
	private static FileFilter classFilter = new FileFilter() {

		@Override
		public String getDescription() {
			return ".class files";
		}

		@Override
		public boolean accept(File file) {

			if (file.isDirectory()) {
				return true;
			}

			String choiceExt = Utils.getExtension(file);

			if (choiceExt != null) {
				if (choiceExt.equals("class")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	};

	private static ActionListener pluginsListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			fileChooser.removeChoosableFileFilter(DataFilter);
			fileChooser.addChoosableFileFilter(classFilter);

			int returnVal = fileChooser.showOpenDialog(mainFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();

				if (file.exists()) {

					if (Utils.getExtension(file) == null) {
						JOptionPane.showMessageDialog(mainFrame,
								"Error: Unsupported Format!");
					} else if (Utils.getExtension(file).equals(Utils.classExt)) {

						boolean flag = false;

						ShapesClassLoader tempLoader = new ShapesClassLoader();

						Class<?> tempClass = tempLoader.loadClass(file);

						supportedShapesClasses.put(tempClass.getSimpleName(),
								tempClass);

						for (String s : supportedShapesNames) {
							if (s.equals(tempClass.getSimpleName())) {
								flag = true;
								break;
							}
						}
						if (!flag) {
							supportedShapesNames.add(tempClass.getSimpleName());
							reDrawButtons();
						}

					}
				} else {
					JOptionPane.showMessageDialog(mainFrame,
							"Error: File Not Found!");
				}
			}
		}
	};

	private static void reDrawButtons() {

		NUM_OF_SHAPES++;

		JButton temp = new JButton(supportedShapesNames.lastElement(),
				createImageIcon("images/" + supportedShapesNames.lastElement()
						+ ".png"));
		temp.setName(supportedShapesNames.lastElement());
		temp.setToolTipText("Draw a new " + supportedShapesNames.lastElement());
		temp.setBackground(Color.LIGHT_GRAY);
		temp.addActionListener(ShapeButtonsListener);
		shapesButtons.add(temp);

		co.gridwidth = 1;
		co.gridheight = 1;

		// inside the loop
		co.gridy = 0;

		for (int i = 0; i < NUM_OF_SHAPES; i++) {
			co.gridx = i;
			backgroundPanel.add(shapesButtons.get(i), co);
		}

		// setting the fill-color button
		co.gridx = NUM_OF_SHAPES + 1;
		backgroundPanel.add(fillColorButton, co);

		co.gridx = NUM_OF_SHAPES + 2;
		backgroundPanel.add(undo, co);

		// setting the redo button
		co.gridx = NUM_OF_SHAPES + 3;
		backgroundPanel.add(redo, co);

		// setting the clear button
		co.gridx = NUM_OF_SHAPES + 4;
		backgroundPanel.add(clear, co);

		scrollArea.setPreferredSize(new Dimension(500, 500));
		co.gridx = 0;
		co.gridy = 1;
		co.gridwidth = NUM_OF_SHAPES + 10;
		co.gridheight = 1000;
		backgroundPanel.add(scrollArea, co);

		backgroundPanel.repaint();

	}

}

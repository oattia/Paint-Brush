package utilities;

import java.io.File;

public class Utils {

	public final static String xml = "xml";
	public final static String json = "json";
	public final static String classExt = "class";

	
	public static final String line = "line";

	public static final String upperRight = "upperRight";
	public static final String lowerLeft = "lowerLeft";
	public static final String fillColor = "fillColor";
	public static final String layoutColor = "layoutColor";
	public static final String vertices = "vertices";
	public static final String numOfPointsToDraw = "numOfPointsToDraw";
	public static final String pointsCounter = "pointsCounter";

	
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
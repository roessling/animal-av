/*
 * SimpleRayTracingGenerator.java
 * Philipp von Bauer & Constantin Zinke, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics.simpleraytracing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

public class SimpleRayTracingGenerator implements ValidatingGenerator {

	private Language lang;
	private SimpleRayTracing srt;

	public void init() {
		lang = new AnimalScript("", "Philipp von Bauer & Constantin Zinke",
				1024, 786);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		srt.drawScene();
		srt.simpleRayTrace();
		srt.finish();

		return srt.getAPI().getASUString();
	}

	public String getName() {
		return "Simple Ray Tracing";
	}

	public String getAlgorithmName() {
		return "Simple Ray Tracing";
	}

	public String getAnimationAuthor() {
		return "Philipp von Bauer & Constantin Zinke";
	}

	public String getDescription(){
        return ""
 +"Remarks on the configurable input parameters:"
 +"\n"
 +"\n"
 +"polygonMatrix:"
 +"\n"
 +"To define your own polygons just edit this 2D-String Array. The First column should be the color and possible colors are:"
 +"\n"
 +"black, blue, cyan, darkGray, gray, green, lightGray, magenta, orange, pink, red, white, yellow."
 +"\n"
 +"\n"
 +"The second column should contain either filled<< or notfilled."
 +"\n"
 +"\n"
 +"After the first two columns you can define vertices of a polygon which should be at least three. One column then represents one point you can define them like this: if you want to make a point (10,10) type in the column 10-10. Be sure not to use \",\" in any column since the polygon data is parsed from an xml file which separates elements with \",\" "
 +"\n"
 +"If for instance you want to define one polygon with 4 vertices and one with 3 you will propbably ask what should be done with the 6th column of the 3 vertices polygon since the size of the Array is not that flexible. Just type none in this column."
 +"\n"
 +"\n"
 +"Your coordinates should be inside the scene: they are not relative to the cartesian plane of the scene, keep that in mind. And of course, remember how the cartesian plane of the screen is defined (upper left is the origin)."
 +"\n"
 +"\n"
 +"cameraPosition & cameraPixeldistance & cameraAngle:"
 +"\n"
 +"Place it inside the scene. There should not be any object between the camera and the pixels."
 +"\n"
 +"For instance: you have placed the camera at (100, 100), pixel distance 100 and the angle is 0 degree so it point horizontal to the right. Dont choose vertices four your objects like (150,100)."
 +"\n"
 +"\n"
 +"lightPos:"
 +"\n"
 +"The light should be inside the scene."
 +"\n"
 +"\n"
 +"pixelCount & pixelSize:"
 +"\n"
 +"If you have too many pixel or if they are simply to big so that they will not fit inside your scene, well it probably will give you interesting results!"
 +"\n"
 +"\n"
 +"sourceCodePositionRelativeToScene:"
 +"\n"
 +"This defines where the pseudo code will be displayed. This can be above the scene, to the left or right, or under the scene. Valid inputs are: under, above, left, right."
 +"\n"
 +"And take into consideration if you have placed the code inside the scene.";
    }

    public String getCodeExample(){
        return "public void simpleRayTracing(){"
 +"\n"
 +"	for each Pixel(i){"
 +"\n"
 +"		ray = createRayFromPixelThroughCamera(Pixel i);"
 +"\n"
 +"		interceptPoint = findClosestIntersectionOfRay(ray);"
 +"\n"
 +"		color = getColorOfIntersectedObject();"
 +"\n"
 +"		shadowRay = createRayFromIntereceptPointToLightSource(interceptPoint);"
 +"\n"
 +"		shadowInterceptPoint = getFirstIntersectionOfShadowRay(shadowRay)"
 +"\n"
 +"		if(shadowInterceptPoint != null){"
 +"\n"
 +"			color = shadowTheColor(color);"
 +"\n"
 +"		}"
 +"\n"
 +"		updatePixelColor(pixel i, color);"
 +"\n"
 +"	}"
 +"\n"
 +"}";
    }

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		System.out.println("validating");
		srt = new SimpleRayTracing(lang, props, primitives);

		// get values for validation
		double cameraPixelDistance = (double) primitives
				.get("cameraPixelDistance");
		double cameraXposition = (double) primitives.get("cameraXposition");
		double cameraYposition = (double) primitives.get("cameraYposition");
		int pixelSize = (Integer) primitives.get("pixelSize");
		int pixelCount = (Integer) primitives.get("pixelCount");
		int cameraAngle = (Integer) primitives.get("cameraAngle") % 360;

		double lowerLeftofSceneBorderXpos = (double) primitives
				.get("lowerLeftofSceneBorderXpos");
		double lowerLeftofSceneBorderYpos = (double) primitives
				.get("lowerLeftofSceneBorderYpos");
		double upperRightofSceneBorderYpos = (double) primitives
				.get("upperRightofSceneBorderYpos");
		double upperRightofSceneBorderXpos = (double) primitives
				.get("upperRightofSceneBorderXpos");

		Rectangle2D.Double sceneRect = new Rectangle2D.Double(
				lowerLeftofSceneBorderXpos, upperRightofSceneBorderYpos,
				upperRightofSceneBorderXpos - lowerLeftofSceneBorderXpos,
				lowerLeftofSceneBorderYpos - upperRightofSceneBorderYpos);
		String sourceCodePosition = (String) primitives
				.get("sourceCodePositionRelativeToScene");	
		
		
		// check the defined scene
		boolean negative = false;
		if(lowerLeftofSceneBorderXpos < 0){
			lowerLeftofSceneBorderXpos = 0; negative = true;
		}
			
		if(upperRightofSceneBorderYpos < 0){
			upperRightofSceneBorderYpos = 0;negative = true;
		}
			
		if(lowerLeftofSceneBorderYpos < upperRightofSceneBorderYpos || upperRightofSceneBorderXpos < lowerLeftofSceneBorderXpos){
			JOptionPane
			.showMessageDialog(
					null,
					"Aborting! \n The scene is not defined properly: Either the lowerLeft x value of the scene is greater than the upperRight x value or \n the upperRight y value is greater than the lowerLeft y value ");
			return false;
		}
		
		if(negative){
			JOptionPane
			.showMessageDialog(
					null,
					"The lowerLeft or upperRight points are outside of the screen (they have negative values). They will be adapted to fit.");		
		}
		
		srt.setMinBB(lowerLeftofSceneBorderXpos,lowerLeftofSceneBorderYpos);
		srt.setMaxBB(upperRightofSceneBorderXpos,upperRightofSceneBorderYpos);
		
		// check source code position / check if there is enough space and give warning/change it
		boolean warning = false;
		switch (sourceCodePosition) {
		case "above":
			if(upperRightofSceneBorderYpos - 500 < 0)
				warning = true;
			break;
		case "left":
			if(lowerLeftofSceneBorderXpos - 500 < 0)
				warning = true;
			break;
		case "right":
			break;
		case "under":
			break;
		default:
			JOptionPane.showMessageDialog(null, "The specified value for the sourceCodePosition is not valid. Type: left, right, above or under. It will be set to default.");
			sourceCodePosition = "under";
		}
		
		
		if(warning){
			JOptionPane
			.showMessageDialog(
					null,
					"The chosen position for the displayed pseudo code is "+sourceCodePosition+". Due to the given defitnion of the scene there will not be enough space \n to draw the code properly without colliding with the scene.\n Therefore the position will be changed.");	
			srt.setSourceCodePosition("under");
		} else{
			srt.setSourceCodePosition(sourceCodePosition);
		}
			
		

		// Check the polygonMatrix and give warnings. Default values are
		// defined.
		String[][] polygonMatrix = (String[][]) primitives.get("polygonMatrix");
		LinkedList<String[]> validPolygons = new LinkedList<String[]>();
		for (int i = 0; i < polygonMatrix.length; i++) {
			LinkedList<String> thePolygon = new LinkedList<String>();
			for (int j = 0; j < polygonMatrix[i].length; j++) {
				if (j == 0) {
					try {
						@SuppressWarnings("unused")
						Field field = Color.class.getField(polygonMatrix[i][j]);
					} catch (Exception e) {
						JOptionPane
								.showMessageDialog(
										null,
										"The color "
												+ polygonMatrix[i][j]
												+ " is not a valid color four your polygon number "
												+ i
												+ ". Check out the remarks in the description. Default color will be set to light grey.");
						polygonMatrix[i][j] = "LIGHT_GRAY";
					} finally {
						thePolygon.add(0, polygonMatrix[i][j]);
					}
				} else if (j == 1) {
					if (polygonMatrix[i][j].compareTo("filled") != 0
							&& polygonMatrix[i][j].compareTo("notfilled") != 0) {
						JOptionPane
								.showMessageDialog(
										null,
										"The second column of the polygon "
												+ i
												+ " should be either filled or notfilled. But is is: "
												+ polygonMatrix[i][j]
												+ ". Setting it to filled.");
						polygonMatrix[i][j] = "filled";
					}
					thePolygon.add(1, polygonMatrix[i][j]);
				} else {
					String[] split = polygonMatrix[i][j].split("-");
					try {
						double x = Double.valueOf(split[0]);
						double y = Double.valueOf(split[1]);
						String cutPoint;
						if (!(sceneRect.contains(x, y))) {
							JOptionPane
									.showMessageDialog(
											null,
											"Point in column "
													+ j
													+ " of polygon "
													+ i
													+ " is not inside the scene. It will be cut at the scene border.");
							if (upperRightofSceneBorderXpos < x)
								x = upperRightofSceneBorderXpos;
							if (lowerLeftofSceneBorderYpos < y)
								y = lowerLeftofSceneBorderYpos;
							if (upperRightofSceneBorderYpos > y)
								y = upperRightofSceneBorderYpos;
							if (lowerLeftofSceneBorderXpos > x)
								x = lowerLeftofSceneBorderXpos;
							cutPoint = x + "-" + y;
							polygonMatrix[i][j] = cutPoint;
						}
						thePolygon.add(j, polygonMatrix[i][j]);
					} catch (Exception e) {
						if (!(polygonMatrix[i][j].compareTo("none") == 0)) {
							JOptionPane
									.showMessageDialog(
											null,
											polygonMatrix[i][j]
													+ " is not a valid point. Points are defined like this 500-400 for a point with x value 500 and y value 400. You can type none if you do not want a point(see description and the probelm with the polygon matrix 2D-array).");
						}
						// the value is not a point, so exit the inner loop
						// since its either "none" or anything else but not a
						// point
						j = polygonMatrix[i].length;
					}
				}
			}
			if (thePolygon.size() < 5) {
				JOptionPane
						.showMessageDialog(
								null,
								"The defined polygon number "
										+ i
										+ " has not enough vertices. It will not be drawn.");
			} else {
				String[] arrayPolygon = new String[thePolygon.size()];
				for (int k = 0; k < thePolygon.size(); k++) {
					arrayPolygon[k] = thePolygon.get(k);
				}
				validPolygons.add(arrayPolygon);
			}
		}

		// check if there are valid polygons, if yes create them.
		if (validPolygons.size() < 1) {
			JOptionPane.showMessageDialog(null,
					"There are no valid polygons defined.");
		} else {
			// create polygons from the string array
			for (int i = 0; i < validPolygons.size(); i++) {
				Color color;
				try {
					Field field = Color.class.getField(validPolygons.get(i)[0]);
					color = (Color) field.get(null);
				} catch (Exception e) {
					color = Color.LIGHT_GRAY; // Not defined
					System.err.println("color not defined");
				}
				boolean fill = false;
				if (validPolygons.get(i)[1].compareTo("filled") == 0) {
					fill = true;
				}

				LinkedList<Line2D> lines = new LinkedList<Line2D>();

				for (int j = 2; j < validPolygons.get(i).length; j++) {
					if (j + 1 == validPolygons.get(i).length) {
						lines.add(MathUtil.createLine(
								parsePoint(validPolygons.get(i)[j]),
								parsePoint(validPolygons.get(i)[2])));
					} else if (j == 2) {

						lines.add(MathUtil.createLine(
								parsePoint(validPolygons.get(i)[j]),
								parsePoint(validPolygons.get(i)[j + 1])));

					} else {
						lines.add(MathUtil.createLine(
								parsePoint(validPolygons.get(i)[j - 1]),
								parsePoint(validPolygons.get(i)[j])));
					}
				}

				if (lines.size() > 2) {
					srt.getObjects().add(new MyShape(lines, color, fill));
				}

			}
		}

		Point2D[] pixels = new Point2D[pixelCount];
		Point2D.Double camera = new Point2D.Double(cameraXposition,
				cameraYposition);

		double lightXpos = (double) primitives.get("lightXpos");
		double lightYpos = (double) primitives.get("lightYpos");
		Point2D.Double lightSource = new Point2D.Double(lightXpos, lightYpos);

		// check if camera is inside the scene
		if (!(sceneRect.contains(camera))) {
			JOptionPane
					.showMessageDialog(null,
							"Camera is not inside the scene, setting it to an other position");
			if (upperRightofSceneBorderXpos < cameraXposition)
				cameraXposition = upperRightofSceneBorderXpos;
			if (lowerLeftofSceneBorderYpos < cameraYposition)
				cameraYposition = lowerLeftofSceneBorderYpos;
			if (upperRightofSceneBorderYpos > cameraYposition)
				cameraYposition = upperRightofSceneBorderYpos;
			if (lowerLeftofSceneBorderXpos > cameraXposition)
				cameraXposition = lowerLeftofSceneBorderXpos;
			camera = new Point2D.Double(cameraXposition, cameraYposition);
		}
		srt.setCamera(camera);

		// check if lightSource is inside the scene
		if (!(sceneRect.contains(lightSource))) {
			JOptionPane
					.showMessageDialog(null,
							"Lightsource is not inside the scene, setting it to an other position");
			if (upperRightofSceneBorderXpos < lightXpos)
				lightXpos = upperRightofSceneBorderXpos;
			if (lowerLeftofSceneBorderYpos < lightYpos)
				lightYpos = lowerLeftofSceneBorderYpos;
			if (upperRightofSceneBorderYpos > lightYpos)
				lightYpos = upperRightofSceneBorderYpos;
			if (lowerLeftofSceneBorderXpos > lightXpos)
				lightXpos = lowerLeftofSceneBorderXpos;
			lightSource = new Point2D.Double(lightXpos, cameraYposition);
		}

		srt.setLight(lightSource);

		// check pixel size, should be at least 1

		if (pixelSize < 1) {
			JOptionPane
					.showMessageDialog(null,
							"The pixel size is too small, setting it to an other value");
			pixelSize = 5;
		}

		srt.setPixelSize(pixelSize);

		// check camera pixel distance
		if (cameraPixelDistance < pixelSize * 2) {
			JOptionPane
					.showMessageDialog(null,
							"The camera Pixel distance is too low setting it to an other value.");
			if (pixelSize * 2 < 10) {
				cameraPixelDistance = 10;
			} else
				cameraPixelDistance = pixelSize * 2;
		}

		Point2D.Double cameraPixelOffset = MathUtil.createPoint(1., 0.);

		Point2D.Double pixelOrigin = MathUtil.addP(camera, new Point2D.Double(
				cameraPixelOffset.getX() * cameraPixelDistance,
				cameraPixelOffset.getY() * cameraPixelDistance));

		Double alpha = Math.toRadians(90) + Math.pow(2, -53);
		Point2D.Double cameraPixelOffsetOrthogonal = MathUtil.createPoint(
				Double.valueOf(Math.round(cameraPixelOffset.getX()
						* Math.cos(alpha) - cameraPixelOffset.y
						* Math.sin(alpha))),
				cameraPixelOffset.x * Math.sin(alpha) + cameraPixelOffset.y
						* Math.cos(alpha));

		// oprthogonale normieren auf länge von einem pixeldurchmesser

		Double norm = Math.sqrt(Math.pow(cameraPixelOffsetOrthogonal.x, 2)
				+ Math.pow(cameraPixelOffsetOrthogonal.y, 2));

		cameraPixelOffsetOrthogonal = MathUtil.createPoint(pixelSize
				* cameraPixelOffsetOrthogonal.x / norm, pixelSize
				* cameraPixelOffsetOrthogonal.y / norm);

		Double tempX = Double.valueOf(pixelCount)
				* cameraPixelOffsetOrthogonal.getX();
		Double tempY = Double.valueOf(pixelCount)
				* cameraPixelOffsetOrthogonal.getY();

		pixelOrigin = MathUtil.addP(pixelOrigin,
				MathUtil.createPoint(-tempX, -tempY));
		pixels = new Point2D.Double[pixelCount];
		LinkedList<Point2D> validPoints = new LinkedList<Point2D>();
		boolean someInvalidPoints = false;
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = MathUtil.addP(pixelOrigin, cameraPixelOffsetOrthogonal);
			pixelOrigin = MathUtil.addP(pixelOrigin,
					cameraPixelOffsetOrthogonal);
			pixelOrigin = MathUtil.addP(pixelOrigin,
					cameraPixelOffsetOrthogonal);

			Point2D p = MathUtil.rotatePointHelper(pixels[i], cameraAngle,
					camera);
			if (sceneRect.contains(p)) {
				validPoints.add(pixels[i]);
			} else {
				someInvalidPoints = true;
			}
		}

		Point2D[] pixels2 = new Point2D[validPoints.size()];

		if (pixels2.length < 1) {
			JOptionPane.showMessageDialog(null,
					"No pixels are inside the scene. Aborting.");
			return false;
		}

		for (int i = 0; i < pixels2.length; i++) {
			pixels2[i] = validPoints.get(i);
		}

		srt.setPixels(pixels2);
		srt.setPixelCount(pixels2.length);

		if (someInvalidPoints) {
			JOptionPane.showMessageDialog(null,
					"Some pixels where outside the scene so they were cut. Pixel count is now: "
							+ pixels2.length);
		}

		return true;
	}

	private Point2D.Double parsePoint(String point) {
		String[] split = point.split("-");
		double x = Double.valueOf(split[0]);
		double y = Double.valueOf(split[1]);
		return MathUtil.createPoint(x, y);
	}

}
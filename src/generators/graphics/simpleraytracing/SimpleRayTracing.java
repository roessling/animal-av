package generators.graphics.simpleraytracing;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;

public class SimpleRayTracing {

	private Color rayColor;
	private Color sceneBorderColor;
	private Color ShadowRayColor;

	private CircleProperties lightsourceProperties;
	private CircleProperties pixelProperties;
	private CircleProperties cameraProperties;
	private CircleProperties shadowRayInterceptPointProperties;
	private CircleProperties rayInterceptPointProperties;

	private int pixelCount;
	private int pixelSize;
	private int cameraAngle;

	private Point2D camera;
	private Point2D light;
	private Point2D minBB;
	private Point2D maxBB;
	private Point2D descriptionTextPosition;
	private Point2D[] pixels;

	private boolean onlyShowRaysOfCurrentIteration;
	private boolean makeRaysOfOldIterationsGrey;
	private boolean cutRaysAfterIntersection;
	private boolean onlyFindFirstIntersectionForShadow = true;

	private Color[] pixelColor;
	private MyShape boundingBox;
	private LinkedList<MyShape> objects;
	private AnimalAPIAdapter api;
	private String sourceCodePosition;

	public SimpleRayTracing(Language lang, AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		setApi(new AnimalAPIAdapter("test", lang));

		if (props != null && primitives != null) {
			// colors
			sceneBorderColor = (Color) primitives.get("sceneBorderColor");
			rayColor = (Color) primitives.get("rayColor");
			ShadowRayColor = (Color) primitives.get("ShadowRayColor");

			// light
			lightsourceProperties = (CircleProperties) props
					.getPropertiesByName("lightsourceProperties");

			// camera
			cameraAngle = (Integer) primitives.get("cameraAngle") % 360;
			cameraProperties = (CircleProperties) props
					.getPropertiesByName("cameraProperties");

			// pixel
			pixelProperties = (CircleProperties) props
					.getPropertiesByName("pixelProperties");

			// intersections
			rayInterceptPointProperties = (CircleProperties) props
					.getPropertiesByName("rayInterceptPointProperties");
			shadowRayInterceptPointProperties = (CircleProperties) props
					.getPropertiesByName("shadowRayInterceptPointProperties");
			cutRaysAfterIntersection = (boolean) primitives
					.get("cutRaysAfterIntersection");

			// options for showing rays
			makeRaysOfOldIterationsGrey = (boolean) primitives
					.get("makeRaysOfOldIterationsGrey");
			onlyShowRaysOfCurrentIteration = (boolean) primitives
					.get("onlyShowRaysOfCurrentIteration");

			// code properties
			api.setSourceCodeProps((SourceCodeProperties) props
					.getPropertiesByName("sourceCodeProperties"));
			api.setDescriptionGeneralProps((SourceCodeProperties) props
					.getPropertiesByName("stepDescriptionProperties"));
			api.setDescriptionCodeProps((SourceCodeProperties) props
					.getPropertiesByName("stepDescriptionProperties"));

		} else {
			System.err.println("no given data");
		}

		objects = new LinkedList<MyShape>();
	}

	private void updatePixelColors() {
		for (int i = 0; i < pixelColor.length; i++) {
			if (pixelColor[i] != null)
				api.changeColorOfPrimitive("pixel" + i, pixelColor[i]);
		}
	}
	
	

	public void drawScene() {
		api.initDescription(new Point2D.Double(50,50));
		api.getSc().highlight(0);
		api.nextStep("Start of description");
		api.nextStep();
		api.getSc().hide();
		descriptionTextPosition = getPositionForDescriptionText();

		pixelColor = new Color[pixelCount];
		for (int i = 0; i < pixelColor.length; i++) {
			pixelColor[i] = Color.black;
		}
		// init description texts
		api.initStepDescriptionGenral(descriptionTextPosition);

		// camera
		createCamera();
		api.highlightCode("cameradescription", api.getDescriptionGeneral());
		api.nextStep();

		// lightsource
		createLightSource();
		api.toggleCodeHighlighting("cameradescription", "lightsource",
				api.getDescriptionGeneral());
		api.nextStep();

		// pixels
		drawPixels();
		api.toggleCodeHighlighting("lightsource", "pixels",
				api.getDescriptionGeneral());
		api.nextStep();

		// bounding box
		drawBoundingBox();
		api.toggleCodeHighlighting("pixels", "boundingbox",
				api.getDescriptionGeneral());
		api.nextStep();

		// drawPolys();
		drawPolygons();
		if(objects.size() > 0){
			api.toggleCodeHighlighting("boundingbox", "objects",
					api.getDescriptionGeneral());
			api.nextStep();
		} else {
			api.unhighlightCode("boundingbox",api.getDescriptionGeneral());
		}
		

		// the source code
		drawSourceCode();
		api.toggleCodeHighlighting("objects", "pseudo",
				api.getDescriptionGeneral());

		// hide all texts
		api.nextStep();
		api.getDescriptionGeneral().hide();
		api.hidePrimitive("camtext");
		api.hidePrimitive("lighttext");
		api.hidePrimitive("pixtext");
		api.hidePrimitive("bbtext");

	}
	
	public void finish(){
		api.nextStep("End of Algorithm");
		api.hideAll();
		api.initFinalSlide(new Point2D.Double(50,50));
		api.highlightCode("complexityHeadLast", api.getLastSlide());
		api.highlightCode("altLast", api.getLastSlide());
		api.nextStep("Complexity and Alternatives");
	

		
	}

	private void drawSourceCode() {
		Point2D upperLeft = new Point2D.Double();
		switch (sourceCodePosition) {
		case "above":
			upperLeft = new Point2D.Double(minBB.getX(), maxBB.getY() - 500.0);
			break;
		case "left":
			upperLeft = new Point2D.Double(minBB.getX() - 520, maxBB.getY());
			break;
		case "right":
			upperLeft = new Point2D.Double(maxBB.getX() + 20, maxBB.getY());
			break;
		case "under":
		default:
			upperLeft = new Point2D.Double(minBB.getX(), minBB.getY() + 20);

		}
		api.initSourceCode(upperLeft);
	}

	private void drawBoundingBox() {
		LinkedList<Line2D> bb = new LinkedList<Line2D>();
		bb.add(MathUtil.createLine(
				MathUtil.createPoint(minBB.getX(), maxBB.getY()), maxBB));
		bb.add(MathUtil.createLine(maxBB,
				MathUtil.createPoint(maxBB.getX(), minBB.getY())));
		bb.add(MathUtil.createLine(
				MathUtil.createPoint(maxBB.getX(), minBB.getY()), minBB));
		bb.add(MathUtil.createLine(minBB,
				MathUtil.createPoint(minBB.getX(), maxBB.getY())));
		boundingBox = new MyShape(bb, sceneBorderColor, false, "bb");
		api.drawShape(boundingBox, "bb");
		api.nextStep();
		api.drawText(MathUtil.addP(maxBB, new Point2D.Double(-10, 10)),
				"The Bounding Box", "bbtext");
	}

	private void drawPolygons() {
		for (int i = 1; i < objects.size() + 1; i++) {
			api.nextStep();
			api.drawShape(objects.get(i - 1), "shape" + i);
		}
	}

	private void drawPixels() {
		for (int i = 0; i < pixels.length; i++) {
			api.drawCircle(
					MathUtil.rotatePointHelper(pixels[i], cameraAngle, camera),
					pixelSize, "pixel" + i, pixelProperties);
		}

		api.nextStep();
		api.drawText(MathUtil.rotatePointHelper(pixels[pixels.length - 1],
				cameraAngle, camera), "The Pixels", "pixtext");

	}

	private void createCamera() {
		api.drawCircle(camera, 5, "camCircle", cameraProperties);
		api.drawRectangle(MathUtil.createPoint(camera.getX() - 15,
				camera.getY()), 10, 7, "cam2", (boolean) cameraProperties
				.get(AnimationPropertiesKeys.FILLED_PROPERTY),
				(Color) cameraProperties
						.get(AnimationPropertiesKeys.COLOR_PROPERTY));

		api.rotate("cam2", cameraAngle,
				MathUtil.createCoordinatesForPoint2D(camera));
		api.nextStep();
		api.drawText(MathUtil.addP(camera, MathUtil.createPoint(5., 5.)),
				"The Camera", "camtext");

	}

	private Point2D getPositionForDescriptionText() {
		Point2D upperLeft = new Point2D.Double();
		switch (sourceCodePosition) {
		case "above":
			upperLeft = new Point2D.Double(minBB.getX() + 500,
					maxBB.getY() - 500.0);
			break;
		case "left":
			upperLeft = new Point2D.Double(minBB.getX() - 520,
					maxBB.getY() + 300);
			break;
		case "right":
			upperLeft = new Point2D.Double(maxBB.getX() + 20,
					maxBB.getY() + 300);
			break;
		case "under":
		default:
			upperLeft = new Point2D.Double(minBB.getX() + 500,
					minBB.getY() + 20);

		}

		return upperLeft;
	}

	private void createLightSource() {

		api.drawCircle(light, 5, "light", lightsourceProperties);
		for (int j = 0; j < 360; j = j + 45) {
			api.drawLine(MathUtil.createLine(
					light,
					MathUtil.rotatePointHelper(
							MathUtil.createPoint(light.getX() + 10,
									light.getY() + 10), j, light)), "sun" + j,
					(Color) lightsourceProperties
							.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		}

		api.nextStep();
		api.drawText(MathUtil.addP(light, MathUtil.createPoint(5., 5.)),
				"The Lightsource", "lighttext");

	}

	public void simpleRayTrace() {
		api.nextStep("Start of the algorithm");
		api.getCode().highlight(0);
		api.initStepDescriptionCode(descriptionTextPosition);
		api.nextStep();
		api.getCode().toggleHighlight(0, 1);
		api.highlightCode("pixiterate", api.getDescriptionCode());
		api.nextStep();
		for (int i = 0; i < pixels.length; i++) {
			// create basic vector from camera to pixel
			Line2D.Double baseRay = new Line2D.Double(camera,
					MathUtil.rotatePointHelper(pixels[i], cameraAngle, camera));
			// scale the vector (baseRay) to actually go through the whole scene
			Line2D.Double scaledRay = MathUtil.scaleRay(baseRay, 1000);
			// get all intersections with the bounding box of the scene in order
			// to cut them at the "sky"
			LinkedList<Point2D.Double> bbIntersects = boundingBox
					.intersectSegments(scaledRay);

			// hide/grey old rays
			if (onlyShowRaysOfCurrentIteration) {

				api.hidePrimitive("ray" + (i - 1));
				api.hidePrimitive("shadow" + (i - 1));
				api.hidePrimitive("WINNER" + (i - 1));
				api.hidePrimitive("shadowWINNER" + (i - 1));

			} else if (makeRaysOfOldIterationsGrey) {
				Color c = Color.LIGHT_GRAY; // not good?
				api.changeColorOfPrimitive("ray" + (i - 1), c);
				api.changeColorOfPrimitive("shadow" + (i - 1), c);
				api.changeColorOfPrimitive("WINNER" + (i - 1), c);
				api.changeColorOfPrimitive("shadowWINNER" + (i - 1), c);
			}

			// draw the ray that ends at the bounding box
			api.nextStep();
			api.getCode().toggleHighlight(1, 2);
			api.toggleCodeHighlighting("pixiterate", "createrays",
					api.getDescriptionCode());
			api.drawLine(MathUtil.scaleToPoint(
					MathUtil.createPoint(bbIntersects.get(0).x,
							bbIntersects.get(0).y), baseRay), "ray" + i,
					rayColor);

			// end of pseudo code line 3

			/*
			 * prepare for intersection of the ray with all objects and finding
			 * the nearest intersection with an object
			 */
			LinkedList<Point2D.Double> intersections = new LinkedList<Point2D.Double>();
			Point2D.Double winner = null;
			Double distance = -1.0;
			objects.add(boundingBox);
			for (Iterator<MyShape> it = objects.iterator(); it.hasNext();) {
				MyShape object = it.next();
				intersections = object.intersectSegments(scaledRay);

				for (Point2D.Double point : intersections) {
					if (distance == -1.0 || point.distance(camera) < distance) {
						// if winner is between camera and pixels ->
						// discard
						if (baseRay.ptSegDist(point) <= 0.001) {
							// point between
						} else {
							distance = point.distance(camera);
							winner = point;
							pixelColor[i] = object.getColor();
						}
					}
				}
			}

			objects.remove(boundingBox);

			if (winner != null) {
				api.nextStep();
				api.toggleCodeHighlighting("createrays", "findintersections",
						api.getDescriptionCode());
				api.getCode().toggleHighlight(2, 3);
				api.drawCircle(winner, 2, "WINNER" + i,
						rayInterceptPointProperties);
				if (cutRaysAfterIntersection) {
					api.nextStep();
					api.drawLine(MathUtil.scaleToPoint(
							MathUtil.createPoint(winner.x, winner.y), baseRay),
							"ray" + i, rayColor);
				}
				api.nextStep();
				api.getCode().toggleHighlight(3, 4);
				api.toggleCodeHighlighting("findintersections", "color1",
						api.getDescriptionCode());

				if (isPointShaded(winner, i)) {
					api.nextStep();
					pixelColor[i] = pixelColor[i].darker();
					api.toggleCodeHighlighting("shadowcontinue", "darken",
							api.getDescriptionCode());
					api.getCode().toggleHighlight(7, 8);
				}
			}
			api.nextStep();
			api.getCode().unhighlight(7);
			api.getCode().toggleHighlight(8, 10);
			api.unhighlightCode("shadow", api.getDescriptionCode());
			api.unhighlightCode("shadowcontinue", api.getDescriptionCode());
			api.toggleCodeHighlighting("darken", "updatepixels",
					api.getDescriptionCode());
			api.changeFillPropertyOfPrimitive("pixel" + i, true, cameraAngle,
					MathUtil.createCoordinatesForPoint2D(camera));
			updatePixelColors();
			api.nextStep();
			api.getCode().unhighlight(10);
			api.unhighlightCode("updatepixels", api.getDescriptionCode());

		}

	}

	private boolean isPointShaded(Point2D.Double point, int nameIndex) {
		Line2D shadowRay = new Line2D.Double(point, light);

		api.nextStep();
		api.getCode().toggleHighlight(4, 5);
		api.toggleCodeHighlighting("color1", "shadow", api.getDescriptionCode());
		api.drawLine(shadowRay, "shadow" + nameIndex, ShadowRayColor);

		LinkedList<Point2D.Double> intersections = null;
		Point2D.Double winner = null;
		Double distance = -1.0;
		boolean result = false;
		for (Iterator<MyShape> it = objects.iterator(); it.hasNext();) {
			MyShape object = it.next();
			intersections = object.intersectSegments(shadowRay);
			for (Point2D.Double p : new LinkedList<Point2D.Double>(
					intersections)) {

				if (p.distance(point) < 0.1) { // to avoid intersections of a //
												// point with itself
					intersections.remove(p);
				} else if (distance == -1.0 || p.distance(point) < distance) {
					distance = p.distance(point);
					winner = p;
				}

			}

			if (intersections.size() > 0) {
				result = true;
				if (onlyFindFirstIntersectionForShadow)
					break;
			}

		}

		api.nextStep();
		api.getCode().toggleHighlight(5, 6);
		api.toggleCodeHighlighting("shadow", "shadowcontinue",
				api.getDescriptionCode());
		if (winner != null) {

			api.drawCircle(winner, 2, "shadowWINNER" + nameIndex,
					shadowRayInterceptPointProperties);
			if (cutRaysAfterIntersection) {
				api.nextStep();
				api.drawLine(MathUtil.scaleFromTo(winner, point, shadowRay),
						"shadow" + nameIndex, ShadowRayColor);
			}
		}

		api.nextStep();
		api.getCode().toggleHighlight(6, 7);

		return result;

	}

	public AnimalAPIAdapter getAPI() {
		return api;
	}

	public void setApi(AnimalAPIAdapter api) {
		this.api = api;
	}

	public void setPixelCount(int pixelCount) {
		this.pixelCount = pixelCount;
	}

	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}

	public void setCamera(Point2D camera) {
		this.camera = camera;
	}

	public void setLight(Point2D light) {
		this.light = light;
	}

	public void setPixels(Point2D[] pixels) {
		this.pixels = pixels;
	}

	public LinkedList<MyShape> getObjects() {
		return objects;
	}

	public void setSourceCodePosition(String sourceCodePosition) {
		this.sourceCodePosition = sourceCodePosition;
	}

	public void setMinBB(double x, double y) {
		this.minBB = MathUtil.createPoint(x, y);

	}

	public void setMaxBB(double x, double y) {
		this.maxBB = MathUtil.createPoint(x, y);
	}

}

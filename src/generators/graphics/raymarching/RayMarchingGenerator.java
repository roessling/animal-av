package generators.graphics.raymarching;

import java.awt.Color;
import java.awt.Font;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class RayMarchingGenerator implements ValidatingGenerator {
    private Language lang;

    public RayMarchingGenerator(){

    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        try {
            Shape[] shapes = parseShapes((String[][]) hashtable.get("Objects"));
        }catch (Exception e){
            return false;
        }
        int[] camPos = (int[]) hashtable.get("Camera Position");
        if(camPos.length < 2)
            return false;
        if(camPos[0] < 0 || camPos[1] < 0)
            return false;
        double camSpread = (int) hashtable.get("Camera Spread");
        if(camSpread < 0)
            return false;
        int numOfRays = (int) hashtable.get("Number of Rays");
        if(numOfRays < 1)
            return false;
        double animSpeed = (double) hashtable.get("Animation Speed");
        if(animSpeed < 0)
            return false;
        return true;
    }

    @Override
    public String generate(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) {
        Shape[] shapes = parseShapes((String[][]) hashtable.get("Objects"));
        int[] camPos = (int[]) hashtable.get("Camera Position");
        Vector2D camPosV = new Vector2D(camPos[0],camPos[1]);
        int camAngle = (int) hashtable.get("Camera Angle");
        double camSpread = (int) hashtable.get("Camera Spread");
        int numOfRays = (int) hashtable.get("Number of Rays");
        double animSpeed = (double) hashtable.get("Animation Speed");
        RectProperties scBack = (RectProperties) animationPropertiesContainer.getPropertiesByName("Code Background");
        SourceCodeProperties scProps = (SourceCodeProperties) animationPropertiesContainer.getPropertiesByName("Code");
        Font codeFont = (Font) scProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        int size = (int) scProps.get(AnimationPropertiesKeys.SIZE_PROPERTY);
        Font scaledFont = codeFont.deriveFont(1.0f * size);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,scaledFont);
        PolygonProperties camProps = (PolygonProperties) animationPropertiesContainer.getPropertiesByName("Camera");
        PolylineProperties rayProps = (PolylineProperties) animationPropertiesContainer.getPropertiesByName("Ray");
        Color circCol = (Color) hashtable.get("Ray Circle Color");
        Color highlightCol = (Color) hashtable.get("Ray Circle Highlight Color");
        Color boundCol = (Color) hashtable.get("Bounding Color");
        Raymarching raymarching = new Raymarching(lang,shapes,camPosV
                ,camAngle,camSpread,numOfRays,animSpeed
                ,scProps,scBack,camProps,rayProps,circCol,highlightCol,boundCol);
        return raymarching.generate();
    }

    @Override
    public String getAlgorithmName() {
        return "Ray Marching";
    }

    @Override
    public String getAnimationAuthor() {
        return "Jonathan Kohlhas, Manuel Brack";
    }

    @Override
    public String getCodeExample() {
        return "function RAY-MARCH(Vector origin, Vector direction)\n"
                +   "\tdepth := 0;\n"
                +   "\tfor i = 0; i < max iterations AND depth < limit; i++\n"
                +   "\t\tposition := origin + depth \u00D7 direction;\n"
                +   "\t\tdistance := +\u221E;\n"
                +   "\t\tfor object in scene\n"
                +   "\t\t\tdistance := min(distance,distanceToObject(object,position));\n"
                +   "\t\t\tif distance < \u03B5\n"
                +   "\t\t\t\treturn position;\n"
                +   "\t\t\tdepth := depth + distance;";
    }

    @Override
    public Locale getContentLocale() {
        return Locale.US;
    }

    @Override
    public String getDescription() {
        return "The ray marching algorithm is used to approximate the intersection points of a ray and the surface of objects modeled using signed distance functions.\n"
                +"\n"
                +"Modifing the objects in the scene:\n" +
                "The objects array describes the objects present in a scene.\n" +
                "The first column sets the shape of the object; the values can be rectangle or circle.\n" +
                "The second column sets the color of the object in RGB.\n" +
                "The third column sets whether or not the object is drawn filled or outlined, true for filled and false for outlined.\n" +
                "The fourth column sets the center of the object.\n" +
                "The fifth column sets the dimensions of the object, radius for circles and width and height for rectangles.\n" +
                "The sixth column sets the rotation of the object and only affects rectangles.";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    @Override
    public String getName() {
        return "Ray Marching of Signed Distance Functions";
    }

    @Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public void init() {
        lang = new AnimalScript("Ray Marching of Signed Distance Functions","Jonathan Kohlhas",1800,700);
    }

    private Shape[] parseShapes(String [][] shapeString){
        Shape[] shapes = new Shape[shapeString.length];
        for(int i = 0; i < shapes.length; i++){
            String[] line = shapeString[i];
            String type = line[0].toLowerCase();
            int[] rgb = Arrays.stream(line[1].toLowerCase().split(" ")).mapToInt(Integer::valueOf).toArray();
            Color color = new Color(rgb[0],rgb[1],rgb[2]);
            boolean fill = Boolean.valueOf(line[2]);
            if(type.equals("circle")){
                int[] center = Arrays.stream(line[3].toLowerCase().split(" ")).mapToInt(Integer::valueOf).toArray();
                Vector2D centerV = new Vector2D(center[0],center[1]);
                int radius = Integer.valueOf(line[4].toLowerCase());
                shapes[i] = new Circle(centerV,radius,color,fill);
            }else if(type.equals("rectangle")){
                int[] center = Arrays.stream(line[3].toLowerCase().split(" ")).mapToInt(Integer::valueOf).toArray();
                Vector2D centerV = new Vector2D(center[0],center[1]);
                int[] dimensions = Arrays.stream(line[4].toLowerCase().split(" ")).mapToInt(Integer::valueOf).toArray();
                Vector2D dimensionsV = new Vector2D(dimensions[0],dimensions[1]);
                int angle = Integer.valueOf(line[5].toLowerCase());
                shapes[i] = new Rectangle(centerV,dimensionsV,angle,color,fill);
            }else{
                InvalidParameterException e = new InvalidParameterException(type + " is not a known shape type");
                throw e;
            }
        }
        return shapes;
    }
}

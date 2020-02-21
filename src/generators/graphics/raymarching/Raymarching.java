package generators.graphics.raymarching;

import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class Raymarching {
    private SourceCode code;
    private Rect background;
    private Shape [] shapes;
    private List<Circle> rayCircles;
    private Ray[] rays;
    private Camera camera;
    private SourceCodeProperties sourceCodeProps;
    private RectProperties scBackProps;
    private PolylineProperties rayProps;
    private Language lang;
    private int numberOfRays;
    private int cameraAngle;
    private double cameraSpread;
    private double animationSpeed;
    private double limit;
    private Color rayCircleColor;
    private Color rayCircleHighlight;
    private Color boundingColor;
    private TextProperties headerProps;
    private TextProperties subHeaderProps;
    private TextProperties descriptionProps;
    final String SOURCECODE =
                "function RAY-MARCH(Vector origin, Vector direction)\n"
            +   "\tdepth := 0;\n"
            +   "\tfor i = 0; i < max iterations AND depth < limit; i++\n"
            +   "\t\tposition := origin + depth \u00D7 direction;\n"
            +   "\t\tdistance := +\u221E;\n"
            +   "\t\tfor object in scene\n"
            +   "\t\t\tdistance := min(distance,distanceToObject(object,position));\n"
            +   "\t\t\tif distance < \u03B5\n"
            +   "\t\t\t\treturn position;\n"
            +   "\t\t\tdepth := depth + distance;";
    final String SDFDESC = "Signed Distance Functions (SDFs) are used to describe 2D or 3D objects by mapping each point in space to the distance between \n" +
            "that point and the surface of the object. Given a point in space the SDF yields a value greater than 0 for points outside of the object, \n" +
            "0 for points on the surface of the object and a value smaller than 0 for points inside the object.\n" +
            "\n" +
            "Mathematical primitives can easily be described using SDFs, e.g. a circle centered at (0,0) with radius r is modeled by the SDF \n" +
            "c(p) = || p || - r . SDFs can also be used to model more complex shapes by transforming or combining primitive SDFs. To apply \n" +
            "transformations like translation, rotation or scaling to the object modeled by the SDF we can apply the inverse transformation to the \n" +
            "input points before applying the SDF to them. E.g. our previous circle translated such that its center is now a points can be  \n" +
            "modeled by the SDF c(p) = || p - s || - r . Given two SDFs f(p) and g(p) we can construct the SDF h(p) = min(f(p), g(p)) that \n" +
            "describes the union of the objects modeled by f(p) and g(p), furthermore, we can construct the SDF i(p) = max(f(p), g(p)) that \n" +
            "describes the intersection of these objects. ";
    final String RAYDESC = "Ray marching is used to calculate the intersection of a ray and objects modeled by an SDF. Starting at the origin point of the ray we \n" +
            "apply the SDF to that origin point, thus calculating the distance d between the origin and the closest object. Therefore, we now \n" +
            "know that the ray can not have any intersection points with any objects with a distance to the origin that is less than the distance d. \n" +
            "This allows us to apply the same logic to a point that is d units along the ray. This principle can be used repeatedly until we have \n" +
            "generated a point for which d is less than some arbitrarily small ε, meaning that we can generate a point arbitrarily close to the \n" +
            "surface of an object and thus allowing us to generate an arbitrarily precise approximation of the intersection point. In the event that \n" +
            "the ray does not intersect with any object, the algorithm would not terminate; practical implementations therefore terminate when a \n" +
            "certain distance from the origin or a set number of iterations is surpassed.\n" +
            "\n" +
            "Ray intersection algorithms like ray marching are often used in rendering algorithms to simulate the behavior of light.";
    final String ADVANTAGES = "+ SDFs are mathematically perfect and ray marching can approximate SDF surfaces with arbitrary precision\n" +
            "+ SDFs allow for set operations like union, intersection and inversion and can thus model complex shapes\n" +
            "+ the computational complexity of ray marching is not dependent on the amount of objects thus operators like the modulo operator\n" +
            "   can describe an infinite amount of objects while only requiring very little computation";
    final String DISADVANTAGES = "- for simple scenes ray marching is slower than calculating ray intersections mathematically\n" +
            "- ray marching can be very slow if a ray passes close to an object\n" +
            "- for rendering applications ray marching cannot be parallelized as efficiently as existing algorithms";
    private int width = 900;
    private int height = 700;

    public Raymarching(Language lang,Shape[] shapes,Vector2D cameraPosition
            ,int cameraAngle ,double cameraSpread, int numberOfRays ,double animationSpeed
            , SourceCodeProperties sourceCodeProps, RectProperties scBackProps, PolygonProperties cameraProps
            , PolylineProperties rayProps, Color rayCircleColor, Color rayCircleHighlight, Color boundingColor){
        this.lang = lang;
        lang.setStepMode(true);
        this.shapes = shapes;
        this.sourceCodeProps = sourceCodeProps;
        this.rayProps = rayProps;
        this.camera = new Camera(cameraPosition,cameraAngle,cameraProps);
        rays = new Ray[numberOfRays];
        this.numberOfRays = numberOfRays;
        this.cameraAngle = cameraAngle;
        this.cameraSpread = cameraSpread;
        this.animationSpeed = animationSpeed;
        this.rayCircleColor = rayCircleColor;
        this.rayCircleHighlight = rayCircleHighlight;
        rayCircles = new ArrayList<Circle>();
        this.boundingColor = boundingColor;
        this.limit = Math.min(width - cameraPosition.x,height - cameraPosition.y);
        this.scBackProps = scBackProps;
        headerProps = new TextProperties();
        subHeaderProps = new TextProperties();
        descriptionProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 50));
        subHeaderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
        subHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        descriptionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 24));
    }

    public String generate() {
        //Intro bla bla
        addIntroduction();
        //maybe highlight each object
        //Text header = lang.newText(new Coordinates(width + 5,50),"Raymarching","",null,headerProps);
        code = placeSourceCode(new Vector2D(width+25 ,100));
        camera.place(lang);
        Circle rad = new Circle(camera.position,limit,Color.gray,false);
        rad.place(lang);
        Text header = lang.newText(new Coordinates(width,15),"Ray Marching of Signed Distance Functions","",null,headerProps);
        for (Shape s: shapes) {
            s.place(lang);
        }
        lang.nextStep();
        //step by step drawing
        for(int rayNum = 0;rayNum < numberOfRays; rayNum++){
            //calc direction of each angle
            double angle = cameraAngle + cameraSpread*(1.0*rayNum/(numberOfRays-1)-0.5);
            Vector2D direction = new Vector2D(Math.cos(Math.toRadians(angle)),Math.sin(Math.toRadians(angle)));
            Vector2D origin = camera.position;
            code.highlight(0);
            lang.nextStep("Ray " + (rayNum+1));
            double rayLength = 0.0;
            code.unhighlight(0);
            code.highlight(1);
            lang.nextStep();
            rays[rayNum] = new Ray(origin,origin,rayProps);
            rays[rayNum].place(lang);
            double minDist = Double.POSITIVE_INFINITY;
            int minElement = -1;

            code.unhighlight(1);
            code.highlight(2);
            lang.nextStep();
            //step for each angle
            for(int itter = 0; itter < 100 && minDist > 0.5 && rayLength < limit;itter++) {
                code.highlight(3);
                code.highlight(4);
                code.unhighlight(2);
                minDist = Double.POSITIVE_INFINITY;
                minElement = -1;
                double[] distances = new double[shapes.length];
                Vector2D position = origin.add(rayLength,direction);
                for (int i = 0; i < distances.length; i++) {
                    double current = shapes[i].signedDistance(position);
                    if(current < minDist) {
                        minDist = current;
                        minElement = i;
                    }
                    distances[i] = current;
                }
                List<Circle> currentCircles = new ArrayList<Circle>();
                for (int i = 0; i < distances.length; i++) {
                    Color col = rayCircleColor;
                    if(minElement == i){
                     col = rayCircleHighlight;
                    }
                    Circle current = new Circle(position, 1, col, false);
                    current.place(lang);
                    if(minElement == i) {
                        rayCircles.add(current);
                    }
                    currentCircles.add(current);
                }
                lang.nextStep();
                code.unhighlight(3);
                code.unhighlight(4);
                code.highlight(5);
                code.highlight(6);
                for (int i = 0; i < distances.length; i++){
                    currentCircles.get(i).growRadius(distances[i],(int)(distances[i]*animationSpeed));
                }
                lang.nextStep();
                code.highlight(7);
                code.unhighlight(5);
                code.unhighlight(6);
                for (int i = 0; i < distances.length; i++){
                    if(i != minElement)
                        currentCircles.get(i).hide();
                }
                currentCircles.clear();
                lang.nextStep();
                code.unhighlight(7);
                if(minDist>0.5) {
                    code.highlight(9);
                    double borderDist = distanceToEdge(origin.add(rayLength + minDist, direction));
                    if (borderDist < 0) {
                        borderDist = distanceToEdge(position);
                        rayLength += borderDist;
                        position = origin.add(rayLength, direction);
                        rays[rayNum].moveEnd(position, (int) (borderDist * animationSpeed));
                        lang.nextStep();
                        code.unhighlight(9);
                        break;
                    }
                    rayLength += minDist;
                    position = origin.add(rayLength, direction);
                    rays[rayNum].moveEnd(position, (int) (minDist * animationSpeed));
                    lang.nextStep();
                    code.unhighlight(9);
                }else{
                    code.highlight(8);
                    lang.nextStep();
                    code.unhighlight(8);
                };
            }
            if(minDist < 0.5) {
                rays[rayNum].element.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, shapes[minElement].color, null, null);
            }else{
                rays[rayNum].element.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, boundingColor, null, null);
            }
            rayCircles.forEach(c->c.hide());
            rayCircles.clear();
            lang.nextStep();

        }
        //conclusion
        camera.hide();
        rad.hide();
        code.hide();
        background.hide();
        header.hide();
        Arrays.stream(rays).forEach(r -> r.hide());
        Arrays.stream(shapes).forEach(s -> s.hide());
        addConclusion();
        return lang.toString();
    }

    private SourceCode placeSourceCode(Vector2D position){
        background = lang.newRect(new Coordinates((int) position.x,0)
                ,new Coordinates(1800,700),""
                ,null,scBackProps);
        SourceCode sc = lang.newSourceCode(position.toNode(),"",null,sourceCodeProps);
        sc.addMultilineCode(SOURCECODE,"Code",null);
        return sc;
    }

    private double distanceToEdge (Vector2D p) {
        Vector2D center = new Vector2D(width/2,height/2);
        Vector2D pt = p.add(-1,center);
        Vector2D d = pt.abs().add(-1,center);
        return -(d.max(new Vector2D(0.0,0.0)).length() + Math.min(Math.max(d.x,d.y),0.0));
    }

    private void addConclusion() {
        LinkedList<Text> texts = new LinkedList<>();
        texts.add(lang.newText(new Coordinates(40,15),"Ray Marching of Signed Distance Functions","",null,headerProps));
        texts.add(lang.newText(new Offset(25,40,texts.getLast(),"SW"),"Conclusion","",null,subHeaderProps));
        texts.add(lang.newText(new Offset(0,20,texts.getLast(),"SW"),"Advantages","",null,subHeaderProps));
        for(String line : ADVANTAGES.split("\n")){
            texts.add(lang.newText(new Offset(0,10,texts.getLast(),"SW"),line,"",null,descriptionProps));
        }
        texts.add(lang.newText(new Offset(0,20,texts.getLast(),"SW"),"Disadvantages","",null,subHeaderProps));
        for(String line : DISADVANTAGES.split("\n")){
            texts.add(lang.newText(new Offset(0,10,texts.getLast(),"SW"),line,"",null,descriptionProps));
        }
        lang.nextStep("Conclusion");

    }

    private void addIntroduction() {
        LinkedList<Text> texts = new LinkedList<>();
        texts.add(lang.newText(new Coordinates(40,15),"Ray Marching of Signed Distance Functions","",null,headerProps));
        texts.add(lang.newText(new Offset(25,40,texts.getLast(),"SW"),"The ray marching algorithm is used to approximate the intersection points of a ray and the surface of objects modeled using","",null,descriptionProps));
        texts.add(lang.newText(new Offset(0,10,texts.getLast(),"SW"),"signed distance functions.","",null,descriptionProps));
        texts.add(lang.newText(new Offset(0,20,texts.getLast(),"SW"),"Signed Distance Functions","",null,subHeaderProps));
        for(String line : SDFDESC.split("\n")){
            texts.add(lang.newText(new Offset(0,10,texts.getLast(),"SW"),line,"",null,descriptionProps));
        }
        lang.nextStep("Introduction");
        texts.listIterator(1).forEachRemaining(Primitive::hide);
        texts.add(lang.newText(new Offset(25,40,texts.getFirst(),"SW"),"Ray Marching","",null,subHeaderProps));
        for(String line : RAYDESC.split("\n")){
            texts.add(lang.newText(new Offset(0,10,texts.getLast(),"SW"),line,"",null,descriptionProps));
        }
        lang.nextStep();
        texts.forEach(Primitive::hide);
    }
}

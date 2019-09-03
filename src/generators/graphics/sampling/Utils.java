package generators.graphics.sampling;

import algoanim.primitives.Point;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import java.util.UUID;

/**
 *
 * @author simon
 */
public class Utils {
    
    private static int uniquePointID = 0;

    private static synchronized String getUniquePointID() {
        return String.format("Point%d", uniquePointID++);
    }

    public static final AnimationPropertiesKeys APK = new AnimationPropertiesKeys() {
    };

    @SuppressWarnings("static-access")
    public static TextProperties clone(TextProperties textProperties) {
        String name = textProperties.toString();
        TextProperties ret = new TextProperties(name + "Clone" + UUID.randomUUID());
        ret.set(APK.COLOR_PROPERTY, textProperties.get(APK.COLOR_PROPERTY));
        ret.set(APK.DEPTH_PROPERTY, textProperties.get(APK.DEPTH_PROPERTY));
        /* The documentation claims to support those properties for text, but it
         doesn't. Why I have to learn this by getting hit by an exception?
         Returning a suiteble default and inform me about it with a logger would
         be to easy, I guess.
         */
        // FIXME: ret.set(APK.FILL_PROPERTY, textProperties.get(APK.FILL_PROPERTY));
        // FIXME: ret.set(APK.FILLED_PROPERTY, textProperties.get(APK.FILLED_PROPERTY));
        ret.set(APK.HIDDEN_PROPERTY, textProperties.get(APK.HIDDEN_PROPERTY));
        ret.set(APK.CENTERED_PROPERTY, textProperties.get(APK.CENTERED_PROPERTY));
        ret.set(APK.FONT_PROPERTY, textProperties.get(APK.FONT_PROPERTY));
        return ret;
    }
    
    /**
     * Workaround to build an offset from an offset.
     * @param lang
     * @param x
     * @param y
     * @param node
     * @param direction
     * @return Offset rooted in a Point
     */
    
    public static Offset buildOffset(Language lang, int x, int y, Node node, String direction){
        Point point = lang.newPoint(node, getUniquePointID(), null, new PointProperties());
        point.hide();
        return new Offset(x, y, point, direction);
    }
    
    public static String removeHTML(String html){
        String ret = html.replaceAll("\\<.*?>","");
        ret = ret.replaceAll("&auml;", "ä");
        ret = ret.replaceAll("&ouml;", "ö");
        ret = ret.replaceAll("&uuml;", "ü");
        ret = ret.replaceAll("&szlig;", "ß");
        return ret;
    }
    
    public static FillInBlanksQuestionModel buildFillInQuestion(String question, String answer, String comment){
        FillInBlanksQuestionModel fillInQ = new FillInBlanksQuestionModel(question);
        fillInQ.setPrompt(question);
        fillInQ.addAnswer(answer, 1, comment);
        return fillInQ;
    }
    
    public static MultipleChoiceQuestionModel buildMultipleChoiceQuestion(String question, String rightAnswer, String rightComment, String falseAnswer, String falseComment){
        MultipleChoiceQuestionModel mcQ = new MultipleChoiceQuestionModel(question+rightAnswer+falseAnswer);
        mcQ.setPrompt(question);
        mcQ.addAnswer(rightAnswer, 1, rightComment);
        mcQ.addAnswer(falseAnswer, 0, falseComment);
        return mcQ;
    }

}

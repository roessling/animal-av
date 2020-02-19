/*
 * LamportClock.java
 * Vladimir Romanenko, Dominik Bierwirth, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;
import java.util.List;

import algoanim.primitives.generators.Language;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

public class LamportClock implements ValidatingGenerator {


    private Language lang;
    private Color highlightColor;
    private Color standardColorCircle;
    private String parseProc;
    private Color highlightContext;
    private String parseMessage;
    private Color highlightCode;
    private int radius;
    private Color standardColorLines;
    private Translator translator;

    public LamportClock(String resource, Locale locale){
        this.translator = new Translator(resource, locale);
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Vladimir Romanenko, Dominik Bierwirth", 800, 600);
        this.lang.setStepMode(true);
        this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    }

    public void init(){
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Vladimir Romanenko, Dominik Bierwirth", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        highlightColor = (Color)primitives.get("highlightColor");
        standardColorCircle = (Color)primitives.get("standardColorCircle");
        parseProc = (String)primitives.get("parseProc");
        highlightContext = (Color)primitives.get("highlightContext");
        parseMessage = (String)primitives.get("parseMessage");
        highlightCode = (Color)primitives.get("highlightCode");
        radius = (Integer)primitives.get("radius");
        standardColorLines = (Color)primitives.get("standardColorLines");

        Logic logic = new Logic();
        logic.parseStrings();
        this.lang.finalizeGeneration();


        return lang.toString();
    }

    public String getName() {
        return translator.translateMessage("algorithmName");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("generatorName");
    }

    public String getAnimationAuthor() {
        return "Vladimir Romanenko, Dominik Bierwirth";
    }

    public String getDescription(){
        return this.translator.translateMessage("description[0]") + translator.translateMessage("description[1]") + translator.translateMessage("description[2]") + "\n"  + "\n"
                + translator.translateMessage("instruction[0]") + translator.translateMessage("instruction[1]")+ translator.translateMessage("instruction[2]")+ translator.translateMessage("instruction[3]")+ translator.translateMessage("instruction[4]");
    }

    public String getCodeExample(){
        return "if(receiver)"
                +"\n"
                +" receive(message, Psend.time)"
                +"\n"
                +" Math.max(time, Psend.time)"
                +"\n"
                +"time = time + 1"
                +"\n"
                +"if(send)"
                +"\n"
                +" send(message, time)";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return translator.getCurrentLocale();
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        int rad = (Integer)hashtable.get("radius");
        String parseProcess = (String)hashtable.get("parseProc");
        String parseMessages =  (String)hashtable.get("parseMessage");
        String errorString = "";
        boolean valid = true;
        if(rad <= 14){
            errorString += translator.translateMessage("error[0]") + radius+ "\n";
            valid= false;
        }
        if(!validateProcParse(parseProcess)){
            errorString += translator.translateMessage("error[1]")+ "\n";
            valid= false;
        }
        if(!validateMessageParse(parseProcess,parseMessages)){
            errorString += translator.translateMessage("error[2]")+ "\n";
            valid= false;
        }
        if(!valid) {
            throw new IllegalArgumentException(errorString);
        }
        return true;
    }

    private boolean validateProcParse(String parseProcess){
        if(parseProcess.isEmpty()) return false;
        char [] parsedProc = parseProcess.toCharArray();
        List<Character> different = new LinkedList<>();
        for(int i = 0; i<parsedProc.length; i++){
            if(parsedProc[i] != ';'){
                if(!different.contains(parsedProc[i])){
                    different.add(parsedProc[i]);
                }
                else{
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateMessageParse(String parseProcess, String parseMessages){
        char [] parsedProc = parseProcess.toCharArray();
        List<Character> eventList = new LinkedList<>();
        char [] parsedMessage = parseMessages.toCharArray();


        char sender = Character.MIN_VALUE;
        char receiver = Character.MIN_VALUE;
        int counter = 0;

        List<Character> dummy = new LinkedList<>();
        List<List<Character>> evByProc = new LinkedList<>();

        for(int i = 0; i<parsedProc.length;i++){

            if(parsedProc[i] == ';'){
                evByProc.add(dummy);
                dummy = new LinkedList<>();
            }
            else{
                eventList.add(parsedProc[i]);
                dummy.add(parsedProc[i]);
            }
        }
        evByProc.add(dummy);

        for(int i = 0; i < parsedMessage.length; i++){
            if(parsedMessage[i] == ','){
                counter = 0;
                sender = Character.MIN_VALUE;
                receiver = Character.MIN_VALUE;
            }
            else if(!(parsedMessage[i] == ',' ) && !eventList.contains(parsedMessage[i])) return false;
            else if(sender == Character.MIN_VALUE){
                sender = parsedMessage[i];
                counter++;
            }
            else if(receiver == Character.MIN_VALUE){
                receiver = parsedMessage[i];
                counter++;
            }
            if(counter == 2){
                if(receiver < sender){
                    return false;
                }
                for(List<Character> cl : evByProc){
                   if(cl.contains(receiver)&&cl.contains(sender)){
                       return false;
                    }
                }

            }

        }
        return true;
    }


    public class Logic{

        public abstract class EventTypes {

            private Process owner;
            private int id;
            private String name;

            public abstract  String getName();


        }

        public class Message extends EventTypes {

            private Event sender;
            private Event receiver;
            private String name;
            private Coordinates[] coordinates;
            private int id;


            public Message(Event sender, Event receiver, int id) {
                this.sender = sender;
                this.receiver = receiver;
                name = "m" + id;

            }

            public String getSenderName() {
                return sender.getName();
            }

            public Event getSender() {
                return sender;
            }

            public Event getReceiver() {
                return receiver;
            }

            public String getName() {
                return name;
            }

            public Coordinates[] getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(Coordinates[] coordinates) {
                this.coordinates = coordinates;
            }

            public int getId() {
                return id;
            }

        }


        public class Event extends EventTypes {

            private Process owner;
            private String name;
            private int clock;
            private int id;
            private int x;
            private int y;

            public Event(String name, Process owner, int y){
                this.name = name;
                this.owner = owner;
                this.y = y;
                clock = 0;
            }


            public Process getOwner() {
                return owner;
            }

            public void setOwner(Process owner) {
                this.owner = owner;
            }


            public String getName() {
                return name;
            }

            public int getClock() {
                return clock;
            }

            public void setClock(int clock) {
                this.clock = clock;
            }

            public int getId() {
                return id;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }
        }


        public class Process{

            private int id;
            private List<Event> eventList;
            private Coordinates [] coordinates;
            private int clock = 0;

            public Process(int id){
                this.id = id;
                eventList = new LinkedList<Event>();

            }

            public List<Event> getEventList() {
                return eventList;
            }

            public void setEventList(List<Event> eventList) {
                this.eventList = eventList;
            }

            public void add (Event e){
                eventList.add(e);
            }

            public Event get (int id){
                return eventList.get(id);
            }

            public int getId() {
                return id;
            }

            public Coordinates[] getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(Coordinates[] coordinates) {
                this.coordinates = coordinates;
            }

            public int getClock() {
                return clock;
            }

            public void setClock(int clock) {
                this.clock = clock;
            }
        }


        Map<String, Circle> circles = new HashMap<>();
        Map<Event, Text> labelNames = new HashMap<>();
        Map<Event, Text> labelClocks = new HashMap<>();
        Map<Message, Polyline> messageLines = new HashMap<>();
        Map<Event, Message> senders = new HashMap<>();
        Map<Event, Message> receivers = new HashMap<>();
        Map<Event, Process> pEvents = new HashMap<>();
        Timing frameRate= new TicksTiming(30);

        int width = 690;
        int height = 480;
        int modifier = radius * 3 + radius/4;
        int questioncounter = 0;

        final String PSEUDO_CODE = "if(receiver)\n\t"+"receive(message, Psend.time)\n\t" + "time = Math.max(time, Psend.time)\n"+"time = time + 1\n" +"if(send)\n\t"+ "send(message, time)\n";
        final String BANNER_STRING = translator.translateMessage("algorithmName");
        final int BANNER_SIZE = 23;

        String IntroText[] = {translator.translateMessage("intro[0]"),
                translator.translateMessage("intro[1]"), translator.translateMessage("intro[2]"),translator.translateMessage("intro[3]"), translator.translateMessage("intro[4]")};
        List<String> OutroText = new LinkedList<>();

        TextProperties bannerTP = new TextProperties();
        RectProperties bannerRectangleProperty = new RectProperties();
        TextProperties introTP = new TextProperties();
        TextProperties outroTP = new TextProperties();
        SourceCodeProperties sourceCodeProperty = new SourceCodeProperties();
        QuestionGroupModel clockUpdateGroup = new QuestionGroupModel("clockQuestions", 3);

        public List<Message> findReceiver(List<Message> mList, Event e){
            List<Message> receivers = new LinkedList<>();
            for(Message m: mList){
                if(m.getReceiver() == e){
                    receivers.add(m);
                }
            }
            return receivers;
        }

        public List<Message> findSender(List<Message> mList, Event e){
            List<Message> senders = new LinkedList<>();
            for(Message m: mList){
                if(m.getSender() == e){
                    senders.add(m);
                }
            }
            return senders;
        }

        private void askClockQuestion(int senderClock, int receiverClock){
            MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(Integer.toString(questioncounter));
            questioncounter++;
            question.setPrompt(translator.translateMessage("questionClock[0]"));
            question.setGroupID("clockQuestions");
           if (senderClock < receiverClock){
               if(senderClock + 1 == receiverClock){
                   question.addAnswer(translator.translateMessage("questionClock[1]") +(receiverClock+1),1,translator.translateMessage("questionClock[2]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +(senderClock+1),0,translator.translateMessage("questionClock[3]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +senderClock,0,translator.translateMessage("questionClock[4]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +(receiverClock+2),0,translator.translateMessage("questionClock[5]"));
               }
               else{
               question.addAnswer(translator.translateMessage("questionClock[1]") +(receiverClock+1),1,translator.translateMessage("questionClock[2]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +(receiverClock+2),0,translator.translateMessage("questionClock[6]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +senderClock,0,translator.translateMessage("questionClock[5]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +receiverClock,0,translator.translateMessage("questionClock[4]"));
               }
           }
           else if(senderClock > receiverClock){
               if(receiverClock + 1 == senderClock){
                   question.addAnswer(translator.translateMessage("questionClock[1]") +receiverClock,0,translator.translateMessage("questionClock[5]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +(senderClock+1),1,translator.translateMessage("questionClock[2]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +senderClock,0,translator.translateMessage("questionClock[4]"));
                   question.addAnswer(translator.translateMessage("questionClock[1]") +(senderClock+2),0,translator.translateMessage("questionClock[6]"));
               }
               question.addAnswer(translator.translateMessage("questionClock[1]") +(receiverClock+1),0,translator.translateMessage("questionClock[7]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +senderClock,0,translator.translateMessage("questionClock[4]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +receiverClock,0,translator.translateMessage("questionClock[5]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") +(senderClock+1),1,translator.translateMessage("questionClock[2]"));
           }
           else{
               question.addAnswer(translator.translateMessage("questionClock[1]") + senderClock,0,translator.translateMessage("questionClock[4]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") + (receiverClock+1),1,translator.translateMessage("questionClock[2]"));
               question.addAnswer(translator.translateMessage("questionClock[1]") + (receiverClock+2), 0, translator.translateMessage("questionClock[6]"));
           }

            lang.addMCQuestion(question);
        }

        private void askHappendBeforeQuestion(boolean condHold){
            MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("happend-before");
            question.setPrompt(translator.translateMessage("questionHB[0]"));
            if(condHold){
                question.addAnswer(translator.translateMessage("questionHB[1]"), 1, translator.translateMessage("questionHB[3]"));
                question.addAnswer(translator.translateMessage("questionHB[2]"), 0, translator.translateMessage("questionHB[4]"));

            }
            else{
                question.addAnswer(translator.translateMessage("questionHB[1]"), 0, translator.translateMessage("questionHB[4]"));
                question.addAnswer(translator.translateMessage("questionHB[2]"), 1, translator.translateMessage("questionHB[3]"));
            }
            lang.addMCQuestion(question);
        }

        public void parseStrings() {
            List<Process> processes = new LinkedList<>();
            List<EventTypes> events = new LinkedList<>();
            parseMessage = parseMessage + ",";

            char[] parsedString = parseProc.toCharArray();
            char[] parsedMessage = parseMessage.toCharArray();

            processes.add(new Process(0));
            Map<Event, Process> pEvents = new HashMap<>();

            for (int i = 0, id = 0; i < parsedString.length; i++) {
                if (parsedString[i] == ';') {
                    id++;
                    processes.add(new Process(id));
                } else {
                    Event e = new Event(parsedString[i]+"", processes.get(id), id +1 );
                    processes.get(id).add(e);
                    events.add(e);
                }

            }

            events.sort(Comparator.comparing(EventTypes:: getName));

            String sender = null;
            String receiver = null;

            for (int i = 0, j = 1; i < parsedMessage.length; i++) {
                if (parsedMessage[i] == ',') {
                    events.add(events.indexOf(findEvent(receiver,events)),new Message(findEvent(sender, events), findEvent(receiver, events), j++));
                    sender = null;
                    receiver = null;
                } else if (sender == null) {
                    sender = parsedMessage[i]+"";
                } else if (receiver == null) {
                    receiver = parsedMessage[i]+"";
                }

            }
            int x = 1;

            for (EventTypes e : events) {
                if(e instanceof Event){
                    ((Event) e).setX(x);
                    x++;
                }
            }

            drawLamport(events, processes);

        }
        public Event findEvent (String c, List<EventTypes> ev){
            for(int i = 0; i<ev.size(); i++){
                if(ev.get(i) instanceof Event){
                    if((ev.get(i)).getName().equals(c)){
                        return (Event)ev.get(i);
                    }
                }
            }
            return null;
        }

        private int getHighest(List<Message> messageList){
            int highest = 0;
            for(Message m: messageList){
                highest = Math.max(highest, m.getSender().getClock());
            }
            return  highest;
        }

        public void drawLamport(List<EventTypes> events, List<Process> pList) {

            lang.setStepMode(true);
            lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
            Polyline[] procLines = new Polyline[pList.size()];

            int biggestX = 0;

            for (Process p : pList) {
                for (Event e : p.getEventList()) {
                    pEvents.put(e, p);
                }
            }

            bannerRectangleProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(195, 187, 187, 1));
            bannerRectangleProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
            bannerRectangleProperty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, -1);
            Rect bannerRectangle = lang.newRect(new Coordinates(width / 2 - ((BANNER_STRING.length() / 2)*BANNER_SIZE), 1), new Coordinates(width / 2 + (BANNER_STRING.length() *BANNER_SIZE), BANNER_SIZE+4),"bannerRect",null, bannerRectangleProperty);

            bannerTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, BANNER_SIZE));
            Text banner = lang.newText(new Coordinates(width / 2 - ((BANNER_STRING.length() / 2)), 0), BANNER_STRING, "banner", null, bannerTP);



            introTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 13));
            Text [] introTextArray = new Text[IntroText.length];
            for(int i = 0; i < IntroText.length; i++){
                Text introText = lang.newText(new Coordinates(25, modifier +i*13), IntroText[i],"introText", null, introTP);
                introTextArray[i] = introText;

            }

            lang.nextStep(translator.translateMessage("step[0]"));

            for(Text t: introTextArray){
                t.hide();
            }

            for (EventTypes e : events) {
                if (e instanceof Event) {
                    CircleProperties cp = new CircleProperties();
                    cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, standardColorCircle);
                    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

                    TextProperties tp = new TextProperties();
                    Text labelClock = lang.newText(new Coordinates(((Event) e).getX() * modifier, ((Event) e).getY() * modifier + radius), ((Event) e).getClock() + "", "clock" + e.getName(), null, tp);
                    labelClocks.put(((Event) e), labelClock);

                    Text labelName = lang.newText(new Coordinates(((Event) e).getX() * modifier, ((Event) e).getY() * modifier - radius - 3*radius/4-1), e.getName(), e.getName(), null, tp);
                    labelNames.put(((Event) e), labelName);

                    String name = e.getName();
                    Circle circle = lang.newCircle(new Coordinates(((Event) e).getX() * modifier, ((Event) e).getY() * modifier), radius, name, null, cp);

                    circles.put(e.getName(), circle);
                    biggestX = ((Event) e).getX() * modifier + radius + radius / 2;
                }
            }

            sourceCodeProperty.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightCode);
            sourceCodeProperty.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, highlightContext);
            sourceCodeProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
            SourceCode sourceCode = lang.newSourceCode(new Coordinates((circles.size() + 1) * modifier, modifier/2), "Pseudo Code", null, sourceCodeProperty);
            sourceCode.addMultilineCode(PSEUDO_CODE, "Pseudo Code;", null);


            Coordinates[][] pCoordinates = new Coordinates[pList.size()][2];
            for (int i = 0; i < pList.size(); i++) {
                pCoordinates[i][0] = new Coordinates(radius / 2, (pList.get(i).getId() + 1) * modifier);
                pCoordinates[i][1] = new Coordinates(biggestX+radius, (pList.get(i).getId() + 1) * modifier);
                pList.get(i).setCoordinates(pCoordinates[i]);
            }

            for (Process p : pList) {
                PolylineProperties pp = new PolylineProperties();
                pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, -1);
                pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
                pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, standardColorLines);
                String name = "pL" + p.getId();
                Polyline line = lang.newPolyline(p.getCoordinates(), name, null, pp);
                procLines[p.getId()] = line;
            }

            int messageAmount = 0;
            List<Message> mList = new LinkedList<>();
            for (EventTypes ev : events) {
                if (ev instanceof Message) {
                    messageAmount++;
                    mList.add((Message) ev);
                }
            }

            Coordinates[][] mCoordinates = new Coordinates[messageAmount][2];


            for (int i = 0; i < mList.size(); i++) {
                mCoordinates[i][0] = (Coordinates) circles.get(mList.get(i).getSenderName()).getCenter();
                mCoordinates[i][1] = (Coordinates) circles.get(mList.get(i).getReceiver().getName()).getCenter();
                mList.get(i).setCoordinates(mCoordinates[i]);
            }

            List<Text> messageTextList = new LinkedList<>();
            for (Message m : mList) {
                PolylineProperties pp = new PolylineProperties();
                pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, -1);
                pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, standardColorLines);
                String name = "mL" + m.getName();
                senders.put(m.getSender(), m);
                receivers.put(m.getReceiver(), m);
                Polyline line = lang.newPolyline(m.getCoordinates(), name, null, pp);
                TextProperties tp = new TextProperties();
                Text labelName = lang.newText(new Coordinates(((m.getReceiver().getX() + m.getSender().getX()) / 2) * modifier + 8, ((m.getReceiver().getY() + m.getSender().getY()) / 2) * modifier + radius +8), m.getName(), m.getName(), null, tp);

                messageLines.put(m, line);
                messageTextList.add(labelName);
            }

            for (EventTypes e : events) {
                boolean rec = false;
                boolean send = false;

                if (e instanceof Event) {
                    sourceCode.highlight(0);
                    circles.get(e.getName()).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlightColor, null, frameRate);
                    lang.nextStep(translator.translateMessage("step[1]") + " "+ ((Event) e).getName());
                    List<Message> lm  = findReceiver(mList,(Event)e);
                    List<Message> lms = findSender(mList, (Event)e);

                    sourceCode.unhighlight(0);
                    if (receivers.containsKey(e)) {
                        sourceCode.highlight(0, 0, true);
                        for(Message m: lm) {
                            messageLines.get(m).changeColor(null, highlightColor, null, frameRate);
                        }
                        rec = true;
                        sourceCode.highlight(1);
                        sourceCode.highlight(2);
                        lang.nextStep();
                    }
                    if (rec) {
                        sourceCode.unhighlight(0, 0, true);
                        sourceCode.unhighlight(1);
                        sourceCode.unhighlight(2);
                    }
                    sourceCode.highlight(3);
                    pEvents.get(e).setClock(pEvents.get(e).getClock() + 1);
                    ((Event) e).setClock(pEvents.get(e).getClock());

                    if(rec){
                    askClockQuestion(getHighest(lm),((Event) e).getClock()-1);
                    }
                    lang.nextStep();

                    labelClocks.get(e).setText(((Event) e).getClock() + "", null, frameRate);
                    lang.nextStep();

                    sourceCode.unhighlight(3);
                    sourceCode.highlight(4);
                    lang.nextStep();

                    if (senders.containsKey(e)) {
                        for(Message m: lms) {
                            messageLines.get(m).changeColor(null, highlightColor, null, frameRate);
                        }
                        sourceCode.highlight(5);
                        sourceCode.unhighlight(4);
                        sourceCode.highlight(4, 0, true);
                        lang.nextStep();
                        send = true;
                    }
                    if (!send) {
                        sourceCode.unhighlight(4);
                    }
                    circles.get(e.getName()).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, standardColorCircle, null, frameRate);
                    if (send) {
                        for(Message m: lms) {
                            messageLines.get(m).changeColor(null, standardColorLines, null, frameRate);
                        }
                        sourceCode.unhighlight(5);
                        sourceCode.unhighlight(4, 0, true);
                    }

                    if (rec) {
                        for(Message m: lm) {
                            messageLines.get(m).changeColor(null, standardColorLines, null, frameRate);
                        }
                    }
                    if(events.indexOf(e) != events.size()-1){
                        lang.nextStep();
                    }

                } else {
                    int tmp = ((Message) e).getSender().getClock();
                    tmp = Math.max(tmp, pEvents.get(((Message) e).getReceiver()).getClock());
                    pEvents.get(((Message) e).getReceiver()).setClock(tmp);
                    ((Message) e).getReceiver().setClock(pEvents.get(((Message) e).getReceiver()).getClock());
                    ((Message) e).getReceiver().setClock(pEvents.get(((Message) e).getReceiver()).getClock());
                }
            }
            sourceCode.hide();
            for(Circle c: circles.values()){
                c.hide();
            }
            for(Polyline p: messageLines.values()){
                p.hide();
            }
            for(Polyline p: procLines){
                p.hide();
            }
            for(Text t: labelNames.values()){
                t.hide();
            }
            for(Text t: labelClocks.values()){
                t.hide();
            }
            for(Text t: messageTextList){
                t.hide();
            }

            boolean happendBefore = false;
            OutroText.add(translator.translateMessage("solution[0]"));
            for(EventTypes e: events){
                if(e instanceof Event){
                    for(EventTypes ev: events){
                        if(ev instanceof Event && events.indexOf(e) < events.indexOf(ev) && ((Event) e).getClock() > ((Event) ev).getClock()){
                            happendBefore = true;
                            break;
                        }
                    }
                }
            }

            askHappendBeforeQuestion(happendBefore);
            lang.nextStep();

            if(happendBefore) {
                OutroText.add(translator.translateMessage("solution[1]"));
                for (EventTypes e : events) {
                    if (e instanceof Event) {
                        for (EventTypes ev : events) {
                            if (ev instanceof Event && events.indexOf(e) < events.indexOf(ev) && ((Event) e).getClock() > ((Event) ev).getClock()) {
                                OutroText.add(translator.translateMessage("solution[2]") + e.getName() + translator.translateMessage("solution[3]") + ev.getName() + translator.translateMessage("solution[4]") + ((Event) e).getClock() + ") " + translator.translateMessage("solution[5]")+ ((Event) ev).getClock() + ")." + " " + translator.translateMessage("solution[6]"));
                            }
                        }
                    }
                }
            }
            else {
                OutroText.add(translator.translateMessage("solution[2]"));
            }


            outroTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 13));
            Text [] outroTextArray = new Text[OutroText.size()];
            for(int i = 0; i < OutroText.size(); i++){
                Text outroText = lang.newText(new Coordinates(25, modifier +i*13), OutroText.get(i),"introText", null, outroTP);
                outroTextArray[i] = outroText;
            }
            lang.nextStep(translator.translateMessage("step[2]"));
        }


    }


}

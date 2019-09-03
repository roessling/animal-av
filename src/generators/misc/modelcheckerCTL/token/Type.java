/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.token;

public enum Type {
    EX("EX"),
    EF("EF"),
    EG("EG"),
    AX("AX"),
    AF("AF"),
    AG("AG"),
    //for the tree build process
    EU_START("E"),
    EU_END("U"),
    AU_START("A"),
    AU_END("U"),
    //
    AND("&"),
    OR("|"),
    NOT("!"),
    IMPL("->"),
    TRUE("true"),
    FALSE("false"),
    BRACKETS_R_OPEN("("),
    BRACKETS_R_CLOSED(")"),
    BRACKETS_C_OPEN("{"),
    BRACKETS_C_CLOSED("}"),
    BRACKETS_S_OPEN("["),
    BRACKETS_S_CLOSED("]"),
    COMMA(","),
    TERMINAL("<terminal>"),
    //for the CTL tree, not the tokenizer
    AU("<AU>"),
    EU("<EU>");

    private final String stringValue;

    Type(String str) {
        this.stringValue = str;
    }

    public static String[] getStringValues() {
        Type[] values = Type.values();
        String[] strings = new String[values.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = values[i].toString();
        }
        return strings;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}


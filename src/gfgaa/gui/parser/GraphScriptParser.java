package gfgaa.gui.parser;

import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.GraphEntry;
import gfgaa.gui.graphs.basic.BasicGraphEntry;
import gfgaa.gui.others.GraphDataBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JEditorPane;

/** Parser class<br>
  * This class handles all parsing actions.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class GraphScriptParser implements ParserTreeInterface {

    /** Flag for debug reasons. */
    private boolean debug = false;

    /** Root node of the complete parser tree. */
    private BNFNode root;

    /** Currently used node during a parsing action. */
    private BNFNode lauf;

    /** Parser Data. */
    private ParserUnit data;

    /** (constructor)<br>
      * Creates the complete graphscript tree and waits
      * for parser actions.
      *
      * @param gb    Reference to the Graph Database
      */
    public GraphScriptParser(final GraphDataBase gb) {

        this.root = new BNFNode(0, 1, "");
        this.data = new ParserUnit(3, 2);

        BNFNode knoten1 = new BNFNode('%', 1, "%");
        knoten1.newInstance(root);

        BNFNode knoten2 =  new BNFNode(WORD, 1, "graphscript");
        knoten2.newInstance(knoten1);

        int size = gb.size();
        BNFNode extension = new BNFNode(EOL, size + 2, "EOL");
        extension.newInstance(knoten2);
        extension.newInstance(extension);

        GraphEntry entry;
        for (int i = 0; i < size; i++) {

            entry = gb.get(i);
            
            if (entry.getTyp() == AbstractGraph.GRAPHTYP_BASIC) {
               
                ((BasicGraphEntry) entry).getSecondParserRoot()
                                         .newInstance(extension);
            }

            entry.getParserRoot().newInstance(extension);
        }
    }

    /** (internal parser method)<br>
      * Test the next token to proove wether it is an expected token or not.
      *
      * @param tok      StreamTokenizer
      * @param type     Parsed token type
      * @param pos      Child position in the current node
      * @return         TRUE  - if it is an expected token,
      *                 FALSE - otherwise
      */
    private boolean testToken(final StreamTokenizer tok,
                              final int type, final int pos) {

        if (type == WORD) {
            String sCode = lauf.getCode(pos);
            if (tok.sval.equals(sCode) || sCode.equals("char")) {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    /** (internal parser method)<br>
      * Method to try to continue parsing after an error had occured.
      *
      * @param tok                  StreamTokenizer
      * @throws IOException         If an error while reading the token occurs
      */
    private void retry(final StreamTokenizer tok) throws IOException {

        // Ggf. Aktionszähler zurücksetzen
        if (data.give != data.done) {
            data.give--;
        }

        // Rest der Zeile ignorieren
        int size, type = tok.ttype;
        while (type != StreamTokenizer.TT_EOL
               && type != StreamTokenizer.TT_EOF) {

            type = tok.nextToken();
        }

        // Wiedereinstieg in den ParserTree
        do {
            size = lauf.getLength();
            for (int i = 0; i < size; i++) {

                if (lauf.getToken(i) == EOL) {
                    lauf = lauf.setNext(i);
                    return;
                }
            }

            lauf = lauf.setNext(0);
        } while(lauf.getLength() != 0);
    }

   /** (internal main parser method)<br>
     * This method try to go through the given file as also through the syntax
     * tree. If an expected token was found the parsing continues otherwise it
     * stops.
     *
     * @param data              The parser database
     * @param myReader          A File- or a StreamReader reading a text
     * @throws IOException      If an error occurred while reading the stream
     */
	private void parseFile(final ParserUnit data,
                           final Reader myReader) throws IOException {

        // Initialisieren der benoetigten Variablen
        StreamTokenizer tok = new StreamTokenizer(myReader);
        tok.eolIsSignificant(true);
        tok.lowerCaseMode(true);

        lauf = root;
       
        // Starten des Parsens
        int gr, a, type;
        boolean correct;
        String keywordError;
        ParserActionInterface act;

        do {
        	
            type = tok.nextToken();
           
            // Suche nach gueltigem Token
            correct = false;
            
            gr = lauf.getLength() - 1;
            //System.out.println(" Größe =" + gr);
            a = -1;
            
            while (a < gr && !correct) {
                a++;
                
                if (debug) { System.out.print(a); }
              
                if (type == lauf.getToken(a)) {
                	/* for(int i=0; i<lauf.getLength(); i++){
                  	   System.out.println(lauf.getCode(i) +"  i=" + i + " warum\n");
                     }*/
                    correct = testToken(tok, type, a);
                   
                   
                }
            }

            if (debug) { System.out.print("?" + correct); }
            // Kein gültiges Token gefunden ?
            if (!correct) {
            
            	
                if (data.state != ParserUnit.STATE_LOADING_INFO) {

                    // Fehlermeldung anfügen und neu versuchen.
                    keywordError = "Error in Line " + tok.lineno()
                                 + "\t-> Unknown or False Keyword ->"
                                 + " Found \"";

                    switch (tok.ttype) {
                        case StreamTokenizer.TT_NUMBER:
                                keywordError += (int) tok.nval;
                            break;
                        case StreamTokenizer.TT_WORD:
                                keywordError += tok.sval;
                            break;
                       case StreamTokenizer.TT_EOL:
                                keywordError += "EOL";
                            break;
                       case StreamTokenizer.TT_EOF:
                                keywordError += "EOF";
                            break;
                       default: keywordError += (char) tok.ttype;
                    }

                    keywordError += "\" instead of " + lauf.getCode(0);
                    gr = lauf.getLength();
                    for (a = 1; a < gr; a++) {
                        keywordError += " | " + lauf.getCode(a);
                    }

                    keywordError += "\n\t-> Parser ignored this line\n";
                    data.errorMessages.add(keywordError);

                    retry(tok);

                } else {

                    // Fehler in kritischer Zone - Parsen wird beendet
                    data.state = ParserUnit.STATE_PARSING_CANCELD;
                    data.errorMessages
                        .add(0, "** Parsing stopped after Critical Errors.\n");

                    return;
                }
            } else {
                // Weitersetzen des Lauf Knotens im SyntaxTree
                if (debug) { System.out.println(">> " + lauf.getCode(a)); }

                lauf = lauf.setNext(a);
               
                act = lauf.getAction();
                if (act != null && !act.execute(tok, data)) {
                    retry(tok);
                }
            }
        } while (type != EOF);

        // Setzt den Status des Parser beim Beenden um festzustellen
        // ob das Parsen erfolgreich verlief.
        switch (data.state) {
            case ParserUnit.STATE_LOADING_INFO:
                    data.state = ParserUnit.STATE_PARSING_CANCELD;
                break;
            //Madieha
            case ParserUnit.STATE_LOADING_OBEREECKE:
            case ParserUnit.STATE_LOADING_StartKnoten:
            case ParserUnit.STATE_LOADING_ZielKnoten:	
            case ParserUnit.STATE_LOADING_NODE:
            case ParserUnit.STATE_LOADING_EDGE:
            case ParserUnit.STATE_LOADING_MATRIX:
                    data.state = ParserUnit.STATE_FINISHED_PARSING;
                break;
            default:
                data.state = ParserUnit.STATE_PARSING_CANCELD;
        }

        data.errorMessages.add(0, "** Parsing finished with "
                                  + data.errorMessages.size()
                                  + " Error(s).\n");

        if (debug) {
            for (int i = 0; i < data.errorMessages.size(); i++) {
                System.out.println(data.errorMessages.get(i));
            }
        }
    }

    /** (parser method)<br>
      * Parses the content of the given file.
      *
      * @param filename     Path of the file
      * @return             State of the parser
      */
    public int parse(final String filename) {
        if (debug) { System.out.println("STARTE PARSING [FILE]"); }

        data.init();
        try {
            parseFile(data, new FileReader(filename));

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return -1;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return -2;
        }

        return data.state;
    }

    /** (parser method)<br>
      * Parses the content of the given pane.
      *
      * @param pane     InputPane from the Graphscript Panel
      * @return         State of the parser
      */
    public int parse(final JEditorPane pane) {
        if (debug) { System.out.println("STARTE PARSING [PANE]"); }

        data.init();
        try {
        
            parseFile(data, new StringReader(pane.getText()));

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return -1;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return -2;
        }

        return data.state;
    }

    /** @deprecated
      * @return NULL
      */
    public BNFNode getRoot() {
        return null;
    }

    /** (internal data method)<br>
      * Returns the parsed graph.
      *
      * @return    Parsed graph
      */
    public AbstractGraph getParsedGraph() {
        return data.graph;
    }

    /** (internal info method)<br>
      * Returns a list containing all errors that
      * occurred while parsing a graphscript notation.
      *
      * @return    List of error messages
      */
	public ArrayList<String> getErrorMessages() {
        return data.errorMessages;
    }
}

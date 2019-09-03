
import interactionsupport.controllers.InteractionController;
import interactionsupport.models.backend.AnimalEvalBackend;

/**
 * Class for testing the interaction module avinteraction.
 *
 * @author Gina Haeussge
 */
public class InteractionTester
{

	//~ Methods -------------------------------------------------

	/**
	 * main
	 *
	 * @param argv Command line parameters
	 *
	 * @throws Exception DOCUMENT ME!
	 */
	public static void main(String[] argv)
		throws Exception
	{

		/** Parser to use */
//		AnimalscriptParser parser = new AnimalscriptParser();

		/** Backend to use */
		AnimalEvalBackend backend = new AnimalEvalBackend();

		/** Instance of the interaction module */
		InteractionController testModule = null;

		testModule = new InteractionController(backend, true);

		// InteractionView definition to use
		testModule.interactionDefinition("test.script",
			"text/animalscript");
		//testModule.interactionDefinition("http://localhost/test.script",
		//	"text/animalscript");

		// interaction calls 
		testModule.interaction("foo");
		testModule.interaction("baz");
		testModule.interaction("iDontExist");
		testModule.interaction("fnord");
		testModule.interaction("bar");

		// the personalized backend shall give out results
		System.out.println(backend.getResultString());

	}
}

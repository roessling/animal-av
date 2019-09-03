package generators.maths.northwestcornerrule;

import algoanim.animalscript.AnimalScript;
import generators.maths.northwestcornerrule.io.Reader;
import generators.maths.northwestcornerrule.io.Writer;
import generators.maths.northwestcornerrule.Algorithm;


public class Generator {

	private Algorithm myAlgorithm;
	private Reader reader;
	private Writer writer;
	
	public Generator(Reader reader, Writer writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public void generate(){
		System.out.println("TEST");
		AnimalScript lang = new AnimalScript("NWCR", "Daniel Appadurai, Benjamin Müller", 800, 600);
		myAlgorithm = new Algorithm(reader.readSupplyArray(), reader.readDemandArray(), lang);
		myAlgorithm.animate();
		writer.write(myAlgorithm.getMyAnimationScript());
	}

}

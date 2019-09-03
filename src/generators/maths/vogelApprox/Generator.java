package generators.maths.vogelApprox;

import generators.maths.vogelApprox.io.Reader;
import generators.maths.vogelApprox.io.Writer;
import generators.maths.vogelApprox.Algorithm;
import algoanim.animalscript.AnimalScript;


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
		AnimalScript lang = new AnimalScript("VogelApprox", "Daniel Appadurai, Benjamin Müller", 800, 600);
		myAlgorithm = new Algorithm(reader.readSupplyArray(), reader.readDemandArray(), reader.readCostArray(), lang);
		myAlgorithm.animate();
		writer.write(myAlgorithm.getMyAnimationScript());
	}

}

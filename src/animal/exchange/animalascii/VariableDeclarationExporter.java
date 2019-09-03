package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.VariableDeclaration;

public class VariableDeclarationExporter extends AnimatorExporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param pw
	 *          the PrintWriter to write to
	 * @param animator
	 *          the current Animator object
	 */
	public void exportTo(PrintWriter pw, Animator animator) {
		// 1. write out the info contained in the ancestor
		// note: this ends without a space
		super.exportTo(pw, animator);
        VariableDeclaration varDecl = (VariableDeclaration)animator;
        pw.print(' ');
        pw.print(varDecl.toString());
		pw.print(System.getProperty("line.separator"));
	}
}

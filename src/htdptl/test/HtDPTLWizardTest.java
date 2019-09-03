package htdptl.test;

import htdptl.gui.BatchPage;
import htdptl.gui.EnterHtDPTLCode;
import htdptl.gui.ExampleCollection;
import htdptl.gui.HtDPTLWizard;

import javax.swing.JComponent;

import junit.framework.TestCase;

public class HtDPTLWizardTest extends TestCase {

	public void test01() {
		HtDPTLWizard.instance.reset();
		HtDPTLWizard.instance.next("enter");
		JComponent page = HtDPTLWizard.instance.getCurrentPage();
		assertTrue(page instanceof EnterHtDPTLCode);
	}
	
	
	
	public void test03() {
		HtDPTLWizard.instance.reset();
		HtDPTLWizard.instance.next("example");
		JComponent page = HtDPTLWizard.instance.getCurrentPage();
		System.out.println(page.getClass());
		assertTrue(page instanceof ExampleCollection);
	}
	
	public void test04() {
		HtDPTLWizard.instance.reset();
		HtDPTLWizard.instance.next("batch");
		JComponent page = HtDPTLWizard.instance.getCurrentPage();
		System.out.println(page.getClass());
		assertTrue(page instanceof BatchPage);
	}
	
	public void test02() {
		HtDPTLWizard.instance.reset();
		HtDPTLWizard.instance.next("load");
		JComponent page = HtDPTLWizard.instance.getCurrentPage();
		System.out.println(page.getClass());
	}
	
}

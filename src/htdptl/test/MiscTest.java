package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Batch;
import htdptl.facade.Facade;

import java.io.File;

import org.junit.Assert;
import junit.framework.TestCase;

public class MiscTest extends TestCase {

  static File out = new File(OutFile.path);

  public static void testString() {
    File in = new File("src/htdptl/test/StringAppend.scm");
    try {
      new Batch(in, out).start();
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  public static void testMap() throws NoExpressionsException,
      TraceTooLargeException {
    Facade f = new Facade();
    f.input("(map + (list 1 2 3) (list 1 2 3))");
    f.animate();
  }

}

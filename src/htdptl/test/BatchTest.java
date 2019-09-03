package htdptl.test;

import htdptl.facade.Batch;

import java.io.File;

import org.junit.Assert;
import junit.framework.TestCase;

public class BatchTest extends TestCase {

	static File out = new File(OutFile.path);
//  static String path = "htdptl/examples/GDI 1 (WS 2009-2010)/";
  static String path = "htdptl/examples/GdI1_WS2009/";

	public static void test0_19() {
		File in = new File(path + "T0.19.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_13() {
		File in = new File(path + "T1.13.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_21() {
		File in = new File(path + "T1.21.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_22() {
		File in = new File(path + "T1.22.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_41() {
		File in = new File(path + "T1.41.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_48() {
		File in = new File(path + "T1.48.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_57() {
		File in = new File(path + "T1.57.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test1_9() {
		File in = new File(path + "T1.9.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test2_32_34() {
		File in = new File(path + "T2.32-34.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test2_35_37() {
		File in = new File(path + "T2.35-37.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test2_6() {
		File in = new File(path + "T2.6.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_19() {
		File in = new File(path + "T3.19.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_22() {
		File in = new File(path + "T3.22.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_23() {
		File in = new File(path + "T3.23.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_26() {
		File in = new File(path + "T3.26.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_27() {
		File in = new File(path + "T3.27.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_4() {
		File in = new File(path + "T3.4.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test3_6() {
		File in = new File(path + "T3.6.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test5_20() {
		File in = new File(path + "T5.20.scm ");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test5_39_50() {
		File in = new File(path + "T5.39-50.scm ");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test5_5_7() {
		File in = new File(path + "T5.5-7.scm ");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test5_54() {
		File in = new File(path + "T5.54.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test5_64_65() {
		File in = new File(path + "T5.64-65.scm ");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test7_32_33() {
		File in = new File(path + "T7.32-33.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test7_38() {
		File in = new File(path + "T7.38.scm ");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public static void test7_65_70() {
		File in = new File(path + "T7.65-70.scm");
		try {
			batch(in, out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private static void batch(File in, File out2) {
		Batch batch = new Batch(in, out);
		batch.process(in, out);
	}

}

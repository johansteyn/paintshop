package johansteyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PaintshopTest extends TestCase {
    public PaintshopTest(String testName) {
        super(testName);
		System.out.println("PaintshopTest (testName=" + testName + ")");
    }

    public static Test suite() {
		System.out.println("PaintshopTest.suite");
        return new TestSuite(PaintshopTest.class);
    }

    public void testPaintshop() {
		System.out.println("PaintshopTest.testPaintshop");
        assertTrue(true);
    }
}


package johansteyn;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

// TODO: Invalid tests (file not found, parse - width, token, etc.)
// TODO: Find ALL possible solutions...
public class PaintshopTest extends TestCase {
	private static ClassLoader classLoader;

	public PaintshopTest(String testName) {
		super(testName);
		classLoader = PaintshopTest.class.getClassLoader();
	}

	public void test1a() throws IOException, ParseException {
		test("test-1a.txt", "G");
	}

	public void test1b() throws IOException, ParseException {
		test("test-1b.txt", "M");
	}

	public void test1c() throws IOException, ParseException {
		test("test-1c.txt", "No solution");
	}

	public void test2a() throws IOException, ParseException {
		test("test-2a.txt", "M M");
	}

	public void test2b() throws IOException, ParseException {
		test("test-2b.txt", "G G");
	}

	public void test2c() throws IOException, ParseException {
		List<String> expectations = new ArrayList<String>();
		expectations.add("G M");
		expectations.add("M G");
		test("test-2c.txt", expectations);
	}

	public void test2d() throws IOException, ParseException {
		test("test-2d.txt", "No solution");
	}

	public void test3a() throws IOException, ParseException {
		test("test-3a.txt", "G G M");
	}

	public void test3b() throws IOException, ParseException {
		test("test-3b.txt", "M G G");
	}

	public void test3c() throws IOException, ParseException {
		test("test-3c.txt", "G G G");
	}

	public void test3d() throws IOException, ParseException {
		test("test-3d.txt", "M M G");
	}

	public void test3e() throws IOException, ParseException {
		test("test-3e.txt", "G G M");
	}

	public void test3f() throws IOException, ParseException {
		test("test-3f.txt", "M G G");
	}

	public void test5a() throws IOException, ParseException {
		test("test-5a.txt", "G G G G M");
	}

	public void test5b() throws IOException, ParseException {
		test("test-5b.txt", "G M G M G");
	}

	public void test5c() throws IOException, ParseException {
		test("test-5c.txt", "G G G M G");
	}

	public void test5d() throws IOException, ParseException {
		test("test-5d.txt", "G M G G G");
	}

	public void test16() throws IOException, ParseException {
		test("test-16.txt", "G M G M G M G M G G G G G G G G");
	}

	public void test20() throws IOException, ParseException {
		test("test-20.txt", "G M G M G M G M G M G G G G G G G G G G");
	}

	public void test24() throws IOException, ParseException {
		test("test-24.txt", "G M G M G M G M G M G M G G G G G G G G G G G G");
	}

	public void test28() throws IOException, ParseException {
		test("test-28.txt", "G M G M G M G M G M G M G M G G G G G G G G G G G G G G");
	}

	public void test32() throws IOException, ParseException {
		test("test-32.txt", "G M G M G M G M G M G M G M G M G G G G G G G G G G G G G G G G");
	}

	public void test48() throws IOException, ParseException {
		test("test-48.txt", "G M G M G M G M G M G M G M G M G M G M G M G M G G G G G G G G G G G G G G G G G G G G G G G G");
	}

	public void test64() throws IOException, ParseException {
		test("test-64.txt", "G M G M G M G M G M G M G M G M G M G M G M G M G M G M G M G M G G G G G G G G G G G G G G G G G G G G G G G G G G G G G G G G");
	}

	private void test(String filename, String expectation) throws IOException, ParseException {
		List<String> expectations = new ArrayList<String>();
		expectations.add(expectation);
		test(filename, expectations);
	}

	private void test(String filename, List<String> expectations) throws IOException, ParseException {
		System.out.println("  Testing input file: " + filename);
		URL url = classLoader.getResource(filename);
		assertNotNull("File not found: " + filename, url);
		String filepath = url.getFile();
		Paintshop paintshop = new Paintshop(filepath);
		String solution = paintshop.solve();
		System.out.println("    solution: " + solution);
		System.out.print("    expected: ");
		boolean first = true;
		for (String expectation : expectations) {
			if (first) {
				System.out.println(expectation);
				first = false;
			} else {
				System.out.println("           or " + expectation);
			}
		}
		boolean solved = false;
		for (String expectation : expectations) {
			if (solution.equals(expectation)) {
				solved = true;
				break;
			}
		}
		assertTrue(solved);
	}
}


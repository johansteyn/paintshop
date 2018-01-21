package johansteyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PaintshopTest {
	@Test(expected = IOException.class)
	public void testFileNotFound() throws IOException, ParseException {
		test("xyz", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidWidth1() throws IOException, ParseException {
		test("invalid-width-1.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidWidth2() throws IOException, ParseException {
		test("invalid-width-2.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidWidth3() throws IOException, ParseException {
		test("invalid-width-3.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidWidth4() throws IOException, ParseException {
		test("invalid-width-4.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidToken() throws IOException, ParseException {
		test("invalid-token.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidValue() throws IOException, ParseException {
		test("invalid-value.txt", "");
	}

	@Test(expected = ParseException.class)
	public void testInvalidMatte() throws IOException, ParseException {
		test("invalid-matte.txt", "");
	}

	@Test
	public void test1a() throws IOException, ParseException {
		test("test-1a.txt", "G");
	}

	@Test
	public void test1b() throws IOException, ParseException {
		test("test-1b.txt", "M");
	}

	@Test
	public void test1c() throws IOException, ParseException {
		test("test-1c.txt", "No solution");
	}

	@Test
	public void test2a() throws IOException, ParseException {
		test("test-2a.txt", "M M");
	}

	@Test
	public void test2b() throws IOException, ParseException {
		test("test-2b.txt", "G G");
	}

	@Test
	public void test3a() throws IOException, ParseException {
		test("test-3a.txt", "G G M");
	}

	@Test
	public void test5a() throws IOException, ParseException {
		test("test-5a.txt", "G G G G M");
	}

	@Test
	public void test5b() throws IOException, ParseException {
		test("test-5b.txt", "G M G M G");
	}

	@Test
	public void test5c() throws IOException, ParseException {
		test("test-5c.txt", "G G G M G");
	}

	@Test
	public void test5d() throws IOException, ParseException {
		test("test-5d.txt", "G M G G G");
	}

	@Test
	public void test16() throws IOException, ParseException {
		test("test-16.txt", "G M G M G M G M G G G G G G G G");
	}

	@Test
	public void test20() throws IOException, ParseException {
		test("test-20.txt", "G M G M G M G M G M G G G G G G G G G G");
	}

	@Test
	public void test24() throws IOException, ParseException {
		test("test-24.txt", "G M G M G M G M G M G M G G G G G G G G G G G G");
	}

	@Test
	public void test28() throws IOException, ParseException {
		test("test-28.txt", "G M G M G M G M G M G M G M G G G G G G G G G G G G G G");
	}

	@Test
	public void test32() throws IOException, ParseException {
		test("test-32.txt", "G M G M G M G M G M G M G M G M G G G G G G G G G G G G G G G G");
	}

	@Test
	public void test48() throws IOException, ParseException {
		test("test-48.txt", "G M G M G M G M G M G M G M G M G M G M G M G M G G G G G G G G G G G G G G G G G G G G G G G G");
	}

	@Test
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
		Paintshop paintshop = new Paintshop("src/test/resources/" + filename);
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
		Assert.assertTrue(solved);
	}
}


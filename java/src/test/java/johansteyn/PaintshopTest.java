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
		test("test-1c.txt", null);
	}

	@Test
	public void test2a() throws IOException, ParseException {
		test("test-2a.txt", "MM");
	}

	@Test
	public void test2b() throws IOException, ParseException {
		test("test-2b.txt", "GG");
	}

	@Test
	public void test3a() throws IOException, ParseException {
		test("test-3a.txt", "GGM");
	}

	@Test
	public void test5a() throws IOException, ParseException {
		test("test-5a.txt", "GGGGM");
	}

	@Test
	public void test5b() throws IOException, ParseException {
		test("test-5b.txt", "GMGMG");
	}

	@Test
	public void test5c() throws IOException, ParseException {
		test("test-5c.txt", "GGGMG");
	}

	@Test
	public void test5d() throws IOException, ParseException {
		test("test-5d.txt", "GMGGG");
	}

	@Test
	public void test16() throws IOException, ParseException {
		test("test-16.txt", "GMGMGMGMGGGGGGGG");
	}

	@Test
	public void test20() throws IOException, ParseException {
		test("test-20.txt", "GMGMGMGMGMGGGGGGGGGG");
	}

	@Test
	public void test24() throws IOException, ParseException {
		test("test-24.txt", "GMGMGMGMGMGMGGGGGGGGGGGG");
	}

	@Test
	public void test28() throws IOException, ParseException {
		test("test-28.txt", "GMGMGMGMGMGMGMGGGGGGGGGGGGGG");
	}

	@Test
	public void test32() throws IOException, ParseException {
		test("test-32.txt", "GMGMGMGMGMGMGMGMGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test48() throws IOException, ParseException {
		test("test-48.txt", "GMGMGMGMGMGMGMGMGMGMGMGMGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test64() throws IOException, ParseException {
		test("test-64.txt", "GMGMGMGMGMGMGMGMGMGMGMGMGMGMGMGMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_10() throws IOException, ParseException {
		test("test-100-10.txt", "MMMMMMMMMMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_20() throws IOException, ParseException {
		test("test-100-20.txt", "MMMMMMMMMMGGGGGMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_30() throws IOException, ParseException {
		test("test-100-30.txt", "MMMMMMMMMMGGMGGMGGGGGGMGGGGMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_40() throws IOException, ParseException {
		test("test-100-40.txt", "MMMMMMMMMMGGMGGMGGGGGGMGGGGMGGGGMGGGMMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_50() throws IOException, ParseException {
		test("test-100-50.txt", "MMMMMMMMMMGGMGGMGGGGGGMGGMGMGGGGMGGGMMGGGGMGGMGMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_60() throws IOException, ParseException {
		test("test-100-60.txt", "MMMMMMMMMMGGMGMMGGGGGGMGGMGMGGGGMGMGMMGGGGMGMMGMGGGGMGMMGMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_70() throws IOException, ParseException {
		test("test-100-70.txt", "MMMMMMMMMMGGMGMMMGGGGGMGGMMMGGGGMGMGMMGGGGMGMMMMGGGGMGMMGMGGGGMMGGMMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_80() throws IOException, ParseException {
		test("test-100-80.txt", "MMMMMMMMMMGGMGMMMGGGGGMGGMMMGMGGMGMGMMGGGGMGMMMMGMGGMGMMGMGGGGMMGGMMGGGGMGGMMMGMGGGGGGGGGGGGGGGGGGGG");
	}

	@Test
	public void test100_90() throws IOException, ParseException {
		test("test-100-90.txt", "MMMMMMMMMMGGMGMMMGGGGGMGMMMMGGGGMGMGMMGGGGMGMMMMGGGGMGMMMMGGGGMMGGMMGGGGMGGMMMGGGGMGMMMMGGGGGGGGGGGG");
	}

	@Test
	public void test100_100() throws IOException, ParseException {
		test("test-100-100.txt", "MMMMMMMMMMGGMGMMMMGMGGMGMMMMGMGGMGMGMMGMGGMGMMMMGMGGMGMMMMGMGGMMGGMMGMGGMGGMMMGMGGMGMMMMGMGGMMGMMMGM");
	}

	private void test(String filename, String expectation) throws IOException, ParseException {
		System.out.println("  Testing input file: " + filename);
		Paintshop paintshop = new Paintshop("src/test/resources/" + filename);
		String solution = paintshop.solve();
		System.out.println("    solution: " + solution);
		System.out.println("    expected: " + expectation);
		if (expectation != null) {
			Assert.assertTrue(expectation.equals(solution));
		} else {
			Assert.assertTrue(solution == null);
		}
	}
}


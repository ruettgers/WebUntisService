package de.weg.WebUntis.resources;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.weg.WebUntis.converter.ausdrucke.PasswordGenerator;

public class PasswordGeneratorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGenerateStringInt() {
	        System.out.println(PasswordGenerator.generate(PasswordGenerator.alphabet, 28));

	        System.out.println(PasswordGenerator.generate("ABC", 2));
	        System.out.println(PasswordGenerator.generate("ABC", 3));
	        System.out.println(PasswordGenerator.generate("abcABC", 10));

	        String someDigits = "0123";
	        String someSpecial = "!%&*+-@";
	        System.out.println(PasswordGenerator.generate(10, someDigits, someSpecial, PasswordGenerator.alphabet));


	        // Invalid input:
	        //System.out.println(generate("", 3));
	        //System.out.println(generate(4));
	        //System.out.println(generate(4, "X", "", "Y"));
	        //System.out.println(generate(4, "X", null, "Y"));
	}

	@Test
	public final void testGenerateIntStringArray() {
		fail("Not yet implemented");
	}

}

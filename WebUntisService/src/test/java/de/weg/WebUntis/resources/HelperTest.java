package de.weg.WebUntis.resources;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.weg.WebUntis.converter.schueler.SpaltenToken;

public class HelperTest {

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
	public final void testUmlauteErsetzen() {
		StringBuffer expected = new StringBuffer();
		for (String t:Helper.REPLACE_UMLAUTE) expected.append(t);
		assertEquals(Helper.umlauteErsetzen(Helper.FIND_UMLAUTE),expected.toString());
		assertEquals(Helper.umlauteErsetzen("öaäaüaÄaÖaÜaß"),"oeaaeaueaAeaOeaUeass");
		assertEquals(Helper.umlauteErsetzen(" ")," ");
		assertEquals(Helper.umlauteErsetzen(""),"");
		assertEquals(Helper.umlauteErsetzen("1234567890abcdefghijklmnopqrstuvwxyz"),"1234567890abcdefghijklmnopqrstuvwxyz");
		
	}
	@Test
	public final void testSonderzeichenErsetzen() {
		StringBuffer expected = new StringBuffer();
		for (String t:Helper.REPLACE_SONDERZEICHEN) expected.append(t);
		assertEquals(Helper.sonderzeichenErsetzen(Helper.FIND_SONDERZEICHEN),expected.toString());
		assertEquals(Helper.sonderzeichenErsetzen(" ")," ");
		assertEquals(Helper.sonderzeichenErsetzen(""),"");
		assertEquals(Helper.sonderzeichenErsetzen("1234567890abcdefghijklmnopqrstuvwxyz"),"1234567890abcdefghijklmnopqrstuvwxyz");
		
	}
	@Test
	public final void testSonderzeichenEntfernen() {
		assertEquals(Helper.sonderzeichenEntfernen(Helper.FIND_ENTFERNEN),"");
		assertEquals(Helper.sonderzeichenEntfernen(" ")," ");
		assertEquals(Helper.sonderzeichenEntfernen(""),"");
		assertEquals(Helper.sonderzeichenEntfernen("1234567890abcdefghijklmnopqrstuvwxyz"),"1234567890abcdefghijklmnopqrstuvwxyz");
		
	}

	@Test
	public final void testUserNameGenerator() {
		assertEquals(Helper.userNameGenerator("Nachname", "Vorname"),"NachnameVor");
		assertEquals(Helper.userNameGenerator("Nachname", "Vo"),"NachnameVo");
		assertEquals(Helper.userNameGenerator("Nachname", "Vö"),"NachnameVoe");
		assertEquals(Helper.userNameGenerator("Nachname", "Vöä"),"NachnameVoe");
		assertEquals(Helper.userNameGenerator("Nachname", "Voß"),"NachnameVos");
		assertEquals(Helper.userNameGenerator("Nach-name", "Voß"),"NachnameVos");
		assertEquals(Helper.userNameGenerator("Nachname", "Voè"),"NachnameVoe");
		
	}

	
	@Test
	public final void testStringToCalendar() {
		String dStr1 = "31.12.1999";
		Calendar c1 =  Helper.stringToCalendar(dStr1);
		assertEquals(c1.get(Calendar.DATE), 31);
		// Beachte: Dezember ist Monat 11, daher besser mit Token arbeiten
		assertEquals(c1.get(Calendar.MONTH), Calendar.DECEMBER);
		assertEquals(c1.get(Calendar.YEAR), 1999);
		
		String dStrOut= Helper.dfDateDD_MM_YYYY.format(c1.getTime());
		assertEquals(dStr1, dStrOut);
		
		
	}
	@Test
	public final void PLZOrtToPLZ_Ort() {
		String poStr1 = "79206 Breisach";
		Map<SpaltenToken, String> m1 = Helper.PLZOrtToPLZ_Ort(poStr1);
		assertEquals(m1.get(SpaltenToken.PLZ), "79206");
		assertEquals(m1.get(SpaltenToken.Ort), "Breisach");
		assertEquals(m1.get(SpaltenToken.PLZ)+" "+m1.get(SpaltenToken.Ort), poStr1);
		
		String poStr2 = "79100 Freiburg i.Br.";
		Map<SpaltenToken, String> m2 = Helper.PLZOrtToPLZ_Ort(poStr2);
		assertEquals(m2.get(SpaltenToken.PLZ), "79100");
		assertEquals(m2.get(SpaltenToken.Ort), "Freiburg i.Br.");
		assertEquals(m2.get(SpaltenToken.PLZ)+" "+m2.get(SpaltenToken.Ort), poStr2);
		
		String poStr3 = "79100 Freiburg i. Br.";
		Map<SpaltenToken, String> m3 = Helper.PLZOrtToPLZ_Ort(poStr3);
		assertEquals(m3.get(SpaltenToken.PLZ), "79100");
		assertEquals(m3.get(SpaltenToken.Ort), "Freiburg i. Br.");
		assertEquals(m3.get(SpaltenToken.PLZ)+" "+m3.get(SpaltenToken.Ort), poStr3);

		// Beachte: doppelte Leerzeichen werden eliminiert
		String poStr4 = "     79206    Breisach     ";
		Map<SpaltenToken, String> m4 = Helper.PLZOrtToPLZ_Ort(poStr4);
		assertEquals("79206",m4.get(SpaltenToken.PLZ));
		assertEquals("Breisach",m4.get(SpaltenToken.Ort));
		

	}

	
}

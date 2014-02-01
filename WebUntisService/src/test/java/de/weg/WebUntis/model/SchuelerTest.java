package de.weg.WebUntis.model;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.weg.WebUntis.resources.Helper;

public class SchuelerTest {

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
	public final void testGenerateDependendValues() {
		String fName = "Familienname";
		String vName = "Vorname";
		String standard = Helper.userNameGenerator(fName, vName);
		int standardExtension = 2;
		
		Schueler s = new Schueler();
		s.setFamilienname(fName);
		s.setVorname(vName);
				
		s.generateDependendValues();
		assertTrue("BenutzerName:"+s.getKurzname()+" ist Standard:"+standard,s.getKurzname().equals(standard));
		assertTrue("Leere Bemerkung", s.getBemerkung()==null);
		
		s = new Schueler();
		s.setFamilienname("Familienname");
		s.setVorname("Vorname");

		s.generateDependendValues();
		assertFalse("BenutzerName:"+s.getKurzname()+" ist nicht Standard",s.getKurzname().equals(standard));
		assertTrue("BenutzerName:"+s.getKurzname()+"  ist Standard mit Zahl",s.getKurzname().equals(standard+standardExtension));
		assertFalse("Keine leere Bemerkung", s.getBemerkung()==null);
		assertFalse("Keine leere Bemerkung", s.getBemerkung().isEmpty());

		s = new Schueler();
		s.setFamilienname("Familienname");
		s.setVorname("Vorname");

		s.generateDependendValues();
		assertFalse("BenutzerName:"+s.getKurzname()+"  ist nicht Standard",s.getKurzname().equals(standard));
		assertFalse("BenutzerName:"+s.getKurzname()+"  ist nicht Standard mit Zahl +",s.getKurzname().equals(standard+standardExtension));
		assertTrue("BenutzerName:"+s.getKurzname()+"  ist Standard mit Zahl + + ",s.getKurzname().equals(standard+(standardExtension+1)));
		
		
	}

	@Test
	public final void testCompareTo() {
		Schueler s1 = new Schueler();
		s1.setFamilienname("Familienname");
		s1.setVorname("Vorname");
		Calendar c1 = Calendar.getInstance();
		c1.set(2000, 12, 1);
		s1.setGeburtsdatum(c1);
		assertTrue("Schüler ist nicht gleich zu sich selbst!",s1.compareTo(s1)==0);
		assertFalse("Schüler ist nicht gleich zu sich selbst!",s1.compareTo(s1)<0);
		assertFalse("Schüler ist nicht gleich zu sich selbst!",s1.compareTo(s1)>0);
		
		Schueler s2 = new Schueler();
		s2.setFamilienname("Familienname");
		s2.setVorname("Vorname");
		s2.setGeburtsdatum(c1);
		assertTrue("Zwei gleiche Schüler sind nicht gleich!",s1.compareTo(s2)==0);
		
		Calendar c2 = Calendar.getInstance();
		c2.set(2000, 12, 2);
		s2.setGeburtsdatum(c2);
		assertTrue("Geburtsdatum:S1 kleiner S2 gefordert!",s1.compareTo(s2)<0);
		Calendar c3 = Calendar.getInstance();
		c3.set(2000, 11, 31);
		s2.setGeburtsdatum(c3);
		assertTrue("Geburtsdatum:S1 größer S2 gefordert!",s1.compareTo(s2)>0);
		s1.setGeburtsdatum(c3);
		s2.setGeburtsdatum(c1);
		assertTrue("Geburtsdatum:S1 kleiner S2 gefordert!",s1.compareTo(s2)<0);
		assertTrue("Geburtsdatum:S2 größer S1 gefordert!",s2.compareTo(s1)>0);
		assertTrue("Geburtsdatum:S2 ungleich S1 gefordert!",s1.compareTo(s2)!=0);


		Schueler s3 = new Schueler();
		s3.setFamilienname("Familienname");
		s3.setVorname("Vorname");
		s3.setGeburtsdatum(c1);
		Schueler s4 = new Schueler();
		s4.setFamilienname("Familienname1");
		s4.setVorname("Vorname");
		s4.setGeburtsdatum(c1);
		assertTrue("Familienname:S3 kleiner S4 gefordert!",s3.compareTo(s4)<0);
		assertTrue("Familienname:S4 größer S3 gefordert!",s4.compareTo(s3)>0);
		assertTrue("Familienname:S4 ungleich S3 gefordert!",s3.compareTo(s4)!=0);
		
		Schueler s5 = new Schueler();
		s5.setFamilienname("Familienname");
		s5.setVorname("Vorname");
		s5.setGeburtsdatum(c1);
		Schueler s6 = new Schueler();
		s6.setFamilienname("Familienname");
		s6.setVorname("Vorname1");
		s6.setGeburtsdatum(c1);
		assertTrue("Vorname: S5 kleiner S6 gefordert!",s5.compareTo(s6)<0);
		assertTrue("Vorname:S5 größer S6 gefordert!",s6.compareTo(s5)>0);
		assertTrue("Vorname:S5 ungleich S6 gefordert!",s5.compareTo(s6)!=0);

	}

	@Test
	public final void testEqualsObject() {
		Schueler s1 = new Schueler();
		s1.setSchluesselExtern("1");
		assertTrue("Zu sich selbst identisch erwartet!",s1.equals(s1));
		
		Schueler s2 = new Schueler();
		s2.setSchluesselExtern("1");
		assertTrue("Objekte mit selben Externen Schlüssel identisch erwartet!",s1.equals(s2));
		assertTrue("Objekte mit selben Externen Schlüssel gegenseitig identisch erwartet!",s2.equals(s1));
		
		Schueler s3 = new Schueler();
		s3.setSchluesselExtern("3");
		assertFalse("Objekte mit unterschiedlichem Externen Schlüssel nicht identisch erwartet!",s1.equals(s3));
		assertFalse("Objekte mit unterschiedlichem Externen Schlüssel gegenseiig nicht identisch erwartet!",s3.equals(s1));
		assertFalse("Objekte mit unterschiedlichem Externen Schlüssel nicht identisch erwartet!",s2.equals(s3));
		
		
		Schueler s5 = new Schueler();
		s5.setFamilienname("Familienname");
		s5.setVorname("Vorname");
		Calendar c1 = Calendar.getInstance();
		c1.set(2000, 11, 1);
		s5.setGeburtsdatum(c1);
		s5.setSchluesselExtern("1");
		
		Schueler s6 = new Schueler();
		s6.setFamilienname("Familienname6");
		s6.setVorname("Vorname6");
		Calendar c2 = Calendar.getInstance();
		c2.set(2000, 11, 5);
		s6.setGeburtsdatum(c2);
		s6.setSchluesselExtern("1");
		
		assertTrue("Objekte mit selben Externen Schlüssel identisch erwartet auch wenn restliche daten verschieden!",s5.equals(s6));

		Schueler s7 = new Schueler();
		s7.setFamilienname("Familienname");
		s7.setVorname("Vorname");
		Calendar c3 = Calendar.getInstance();
		c3.set(2000, 11, 5);
		s7.setGeburtsdatum(c3);
		Schueler s8 = new Schueler();
		s8.setFamilienname(s7.getFamilienname());
		s8.setVorname(s7.getVorname());
		s8.setGeburtsdatum(s7.getGeburtsdatum());
		assertTrue("Objekte ohne Externen Schlüssel nach compareTo identisch erwartet!",s7.equals(s8));

		Schueler s9 = new Schueler();
		s9.setFamilienname(s7.getFamilienname()+"a");
		s9.setVorname(s7.getVorname());
		s9.setGeburtsdatum(s7.getGeburtsdatum());
		assertFalse("Objekte ohne Externen Schlüssel nach compareTo nichtidentisch erwartet auch wenn restliche daten verschieden!",s7.equals(s9));
		s9.setFamilienname(s7.getFamilienname());
		s9.setVorname(s7.getVorname()+"a");
		s9.setGeburtsdatum(s7.getGeburtsdatum());
		assertFalse("Objekte ohne Externen Schlüssel nach compareTo nichtidentisch erwartet auch wenn restliche daten verschieden!",s7.equals(s9));
		s9.setFamilienname(s7.getFamilienname());
		s9.setVorname(s7.getVorname());
		Calendar c4 = Calendar.getInstance();
		c4.set(Calendar.YEAR, s7.getGeburtsdatum().get(Calendar.YEAR));
		c4.set(Calendar.MONTH, s7.getGeburtsdatum().get(Calendar.MONTH));
		c4.set(Calendar.DATE, s7.getGeburtsdatum().get(Calendar.DATE));
		s9.setGeburtsdatum(c4);
		assertTrue("Objekte ohne Externen Schlüssel nach compareTo identisch erwartet!"+"\n s7="+s7+"\n s9="+s9,s7.equals(s9));
		c4.add(Calendar.YEAR, 1);
		assertFalse("Objekte ohne Externen Schlüssel nach compareTo nichtidentisch erwartet auch wenn restliche daten verschieden!",s7.equals(s9));

		
		

	}

	@Test
	public final void testToString() {
		// TODO: fail("Not yet implemented");
	}

	@Test
	public final void testIsValid() {
		Schueler s1 = new Schueler();
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s1.isValid());
		Schueler s2 = new Schueler();
		s2.setFamilienname("Familienname");
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s1.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s2.isValid());
		Schueler s3 = new Schueler();
		s1.setVorname("Vorname");
		s2.setVorname("Vorname");
		s3.setVorname("Vorname");
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s1.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s2.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s3.isValid());
		Schueler s4 = new Schueler();
		s1.setSchluesselExtern("1");
		s2.setSchluesselExtern("2");
		s3.setSchluesselExtern("3");
		s4.setSchluesselExtern("4");
		assertTrue("Gültiger Schueler ist ungültig: Fehler",s2.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s1.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s3.isValid());
		assertTrue("Ungültiger Schueler ist gültig: Fehler",!s4.isValid());
	}

	@Test
	public final void testGetVorname() {
		String vorname = "Vorname";
		Schueler s1 = new Schueler();
		s1.setVorname(vorname);
		assertEquals(s1.getVorname(),vorname);
		
	}

	@Test
	public final void testSetVorname() {
		String vorname = "Vorname";
		Schueler s1 = new Schueler();
		s1.setVorname(vorname);
		assertEquals(s1.getVorname(),vorname);
	}

	@Test
	public final void testGetStrasse() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setStrasse(str);
		assertEquals(s1.getStrasse(),str);
	}

	@Test
	public final void testSetStrasse() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setStrasse(str);
		assertEquals(s1.getStrasse(),str);
	}

	@Test
	public final void testGetPLZ() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setPLZ(str);
		assertEquals(s1.getPLZ(),str);
	}

	@Test
	public final void testSetPLZ() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setPLZ(str);
		assertEquals(s1.getPLZ(),str);
	}

	@Test
	public final void testGetStadt() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setStadt(str);
		assertEquals(s1.getStadt(),str);
	}

	@Test
	public final void testSetStadt() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setStadt(str);
		assertEquals(s1.getStadt(),str);
	}

	@Test
	public final void testGetEMail() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setEMail(str);
		assertEquals(s1.getEMail(),str);
	}

	@Test
	public final void testSetEMail() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setEMail(str);
		assertEquals(s1.getEMail(),str);
	}

	@Test
	public final void testGetTelefonnummer() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setTelefonnummer(str);
		assertEquals(s1.getTelefonnummer(),str);
	}

	@Test
	public final void testSetTelefonnummer() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setTelefonnummer(str);
		assertEquals(s1.getTelefonnummer(),str);
	}

	@Test
	public final void testGetKurzname() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setKurzname(str);
		assertEquals(s1.getKurzname(),str);
	}

	@Test
	public final void testSetKurzname() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setKurzname(str);
		assertEquals(s1.getKurzname(),str);
	}

	@Test
	public final void testGetGeburtsdatum() {
		fail("Not yet implemented");
	}
	@Test
	public final void testSetGeburtsdatum() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetFamilienname() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setFamilienname(str);
		assertEquals(s1.getFamilienname(),str);
	}

	@Test
	public final void testSetFamilienname() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setFamilienname(str);
		assertEquals(s1.getFamilienname(),str);
	}

	@Test
	public final void testGetGeschlecht() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setGeschlecht(str);
		assertEquals(s1.getGeschlecht(),str);
	}

	@Test
	public final void testSetGeschlecht() {
		//TODO: nur m oder w 
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setGeschlecht(str);
		assertEquals(s1.getGeschlecht(),str);
	}


	@Test
	public final void testGetKlasse() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setKlasse(str);
		assertEquals(s1.getKlasse(),str);
	}

	@Test
	public final void testSetKlasse() {
		//TODO: nur erlaubt Klassen - prüfe gegen eine Liste von Klassen - konvertiere in WebUntis Klassen (ALias) gegenüber Sekretariatsklassen
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setKlasse(str);
		assertEquals(s1.getKlasse(),str);
	}

	@Test
	public final void testGetText() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setText(str);
		assertEquals(s1.getText(),str);
	}

	@Test
	public final void testSetText() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setText(str);
		assertEquals(s1.getText(),str);
	}

	@Test
	public final void testIsSchulpflicht() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetSchulpflicht() {
		fail("Not yet implemented");
	}

	@Test
	public final void testIsVolljaehrig() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetVolljaehrig() {
		fail("Not yet implemented");
	}

	@Test
	public final void testIsAktiv() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetAktiv() {
		fail("Not yet implemented");
	}

	@Test
	public final void testIsAttestpflicht() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetAttestpflicht() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetSchluesselIntern() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSchluesselIntern(str);
		assertEquals(s1.getSchluesselIntern(),str);
	}

	@Test
	public final void testSetSchluesselIntern() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSchluesselIntern(str);
		assertEquals(s1.getSchluesselIntern(),str);
	}

	@Test
	public final void testGetSchluesselExtern() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSchluesselExtern(str);
		assertEquals(s1.getSchluesselExtern(),str);
	}

	@Test
	public final void testSetSchluesselExtern() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSchluesselExtern(str);
		assertEquals(s1.getSchluesselExtern(),str);
	}

	@Test
	public final void testGetBenutzergruppe() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setBenutzergruppe(str);
		assertEquals(s1.getBenutzergruppe(),str);
	}

	@Test
	public final void testSetBenutzergruppe() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setBenutzergruppe(str);
		assertEquals(s1.getBenutzergruppe(),str);
	}

	@Test
	public final void testGetPasswort() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setPasswort(str);
		assertEquals(s1.getPasswort(),str);
	}

	@Test
	public final void testSetPasswort() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setPasswort(str);
		assertEquals(s1.getPasswort(),str);
	}

	@Test
	public final void testGetSprache() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSprache(str);
		assertEquals(s1.getSprache(),str);
	}

	@Test
	public final void testSetSprache() {
		String str = "irgendwas";
		Schueler s1 = new Schueler();
		s1.setSprache(str);
		assertEquals(s1.getSprache(),str);
	}

	@Test
	public final void testSetAustrittsdatum() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetAustrittsdatum() {
		fail("Not yet implemented");
	}


	@Test
	public final void testSetEintrittsdatum() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetEintrittsdatum() {
		fail("Not yet implemented");
	}

}

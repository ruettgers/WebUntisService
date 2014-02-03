package de.weg.WebUntis.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import de.weg.WebUntis.converter.ausdrucke.PasswordGenerator;
import de.weg.WebUntis.db.UsersCache;
import de.weg.WebUntis.resources.Helper;

/**
 * @author Rüttgers
 */
public class Schueler implements Comparable<Schueler> {

	public static final Logger log = Logger.getLogger(Schueler.class.getName());

	public static String TOKEN = "Schueler"; //$NON-NLS-1$

	public int compareTo(Schueler obj) {
		int cmp;
		// Familienname
		cmp = Helper.getCollator().compare(this.getFamilienname(),
				obj.getFamilienname());
		if (cmp != 0) {
			return cmp;
		}
		// Vorname
		cmp = Helper.getCollator().compare(this.getVorname(), obj.getVorname());
		if (cmp != 0) {
			return cmp;
		}
		// Geburtsdatum
		cmp = this.getGeburtsdatum().compareTo(obj.getGeburtsdatum());

		return cmp;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Schueler)) {
			return false;
		}
		Schueler element = (Schueler) obj;
		if (this.getSchluesselExtern() != null) {
			return this.getSchluesselExtern().equals(
					element.getSchluesselExtern());
		} else {
			return this.compareTo(element) == 0;
		}
	}

	@Override
	public String toString() {
		StringBuffer st = new StringBuffer("WebUntis-Schueler:");
		st.append(this.getSchluesselExtern());
		st.append(":");
		st.append(this.getKurzname());
		st.append(":");
		st.append(this.getKlasse());
		st.append(":");
		st.append(this.getVorname());
		st.append(" ");
		st.append(this.getFamilienname());
		st.append(":");
		st.append(Helper.formatDatumStandard(this.getGeburtsdatum()));
		st.append(";");
		return st.toString();
	}

	public boolean isValid() {
		if (this.getFamilienname() == null || this.getFamilienname().isEmpty()) {
			return false;
		}
		if (this.getVorname() == null || this.getVorname().isEmpty()) {
			return false;
		}
		if (this.getSchluesselExtern() == null
				|| this.getSchluesselExtern().isEmpty()) {
			return false;
		}
		return true;
	}

	public void setDefaultValues() {
		this.setAktiv(true);
		this.setAttestpflicht(false);
		this.setBenutzergruppe("Schüler");
		this.setVolljaehrig(false);
		this.setSchulpflicht(true);

	}

	/**
	 * 
	 * Kurzname anhand des User Generators <br>
	 * Password generieren <br>
	 */
	public void generateDependendValues() {
		if (this.getKurzname() == null || this.getKurzname().isEmpty()) {
			generiereBenutzername();

		}
		if (this.getPasswort() == null || this.getPasswort().isEmpty()) {
			this.setPasswort(PasswordGenerator.generate(8,
					PasswordGenerator.alphabet));
		}
	}

	private void generiereBenutzername() {
		
		// Standardversuch
		String kn = Helper.userNameGenerator(this.getFamilienname(),
				this.getVorname());
		
		// Prüfe auf Sonderzeichen, leerZeichen und so weiter:
		if (!Helper.userNameIsValid(kn)) {
			log.warning("ACHTUNG: Benutzername " + kn
					+ " enthält unerlaubte Zeichen. Regel aufstellen.");
			log.warning("ACHTUNG: Benutzername-Alt " + kn
					+ " entferne Sonderzeichen.");
			String knneu = Helper.sonderzeichenNichtGefundenEntfernen(kn);
			log.warning("ACHTUNG: Benutzername-Neu " + knneu
					+ " entferne Sonderzeichen.");

			// Bemerkungsfeld setzen, dient der Kontrolle in der Excelliste
			this.addBemerkung("Name mit Sonderzeichen: mit:" + kn + " ohne:"
					+ knneu);
			kn = knneu;

		}
		
		// Prüfe, ob der Name schon verhanden ist und erzeuge gegebenefalls einen neuen Namen, diesen in den Namenscach einordnen für weitere folgende Versuche
		int startExtension = 2;
		int i = startExtension;
		String nkn = kn;
		UsersCache uc = UsersCache.getInstance();
		while (uc.containsName(nkn))
		{
			nkn = kn + i;
			i++;
		}
		if (i>startExtension)
		{
			this.addBemerkung("StandardName bereits vorhanden. Neuer Name:"+nkn);
		}

		uc.insertDummyName(nkn);

		this.setKurzname(nkn);
	}


	private String bemerkung;

	public void setBemerkung(String string) {
		this.bemerkung = string;

	}

	public String getBemerkung() {
		return this.bemerkung;
	}
	private void addBemerkung(String neuerTeil) {
		String bemerkung = this.getBemerkung();
		if (bemerkung == null) {
			bemerkung = "";
		} else {
			bemerkung = bemerkung + ":";
		}
		this.setBemerkung(bemerkung + neuerTeil);

	}

	/**
	 * @uml.property name="Vorname"
	 */
	private String vorname1;

	/**
	 * Getter of the property <tt>vorname</tt>
	 * 
	 * @return Returns the vorname.
	 * @uml.property name="Vorname"
	 */
	public String getVorname() {
		return vorname1;
	}

	/**
	 * Setter of the property <tt>vorname</tt>
	 * 
	 * @param vorname
	 *            The vorname to set.
	 * @uml.property name="Vorname"
	 */
	public void setVorname(String vorname) {
		vorname1 = vorname;
	}

	/**
	 * @uml.property name="Strasse"
	 */
	private String strasse;

	/**
	 * Getter of the property <tt>Strasse</tt>
	 * 
	 * @return Returns the strasse.
	 * @uml.property name="Strasse"
	 */
	public String getStrasse() {
		return strasse;
	}

	/**
	 * Setter of the property <tt>Strasse</tt>
	 * 
	 * @param Strasse
	 *            The strasse to set.
	 * @uml.property name="Strasse"
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	/**
	 * @uml.property name="PLZ"
	 */
	private String plz;

	/**
	 * Getter of the property <tt>PLZ</tt>
	 * 
	 * @return Returns the plz.
	 * @uml.property name="PLZ"
	 */
	public String getPLZ() {
		return plz;
	}

	/**
	 * Setter of the property <tt>PLZ</tt>
	 * 
	 * @param PLZ
	 *            The plz to set.
	 * @uml.property name="PLZ"
	 */
	public void setPLZ(String plz) {
		this.plz = plz;
	}

	/**
	 * @uml.property name="Stadt"
	 */
	private String stadt;

	/**
	 * Getter of the property <tt>Stadt</tt>
	 * 
	 * @return Returns the stadt.
	 * @uml.property name="Stadt"
	 */
	public String getStadt() {
		return stadt;
	}

	/**
	 * Setter of the property <tt>Stadt</tt>
	 * 
	 * @param Stadt
	 *            The stadt to set.
	 * @uml.property name="Stadt"
	 */
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	/**
	 * @uml.property name="EMail"
	 */
	private String eMail;

	/**
	 * Getter of the property <tt>EMail</tt>
	 * 
	 * @return Returns the eMail.
	 * @uml.property name="EMail"
	 */
	public String getEMail() {
		return eMail;
	}

	/**
	 * Setter of the property <tt>EMail</tt>
	 * 
	 * @param EMail
	 *            The eMail to set.
	 * @uml.property name="EMail"
	 */
	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * @uml.property name="Telefonnummer"
	 */
	private String telefonnummer;

	/**
	 * Getter of the property <tt>Telefonnummer</tt>
	 * 
	 * @return Returns the telefonnummer.
	 * @uml.property name="Telefonnummer"
	 */
	public String getTelefonnummer() {
		return telefonnummer;
	}

	/**
	 * Setter of the property <tt>Telefonnummer</tt>
	 * 
	 * @param Telefonnummer
	 *            The telefonnummer to set.
	 * @uml.property name="Telefonnummer"
	 */
	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	/**
	 * @uml.property name="Kurzname"
	 */
	private String kurzname;

	/**
	 * Getter of the property <tt>Username</tt>
	 * 
	 * @return Returns the Username.
	 * @uml.property name="Kurzname"
	 */
	public String getKurzname() {
		return kurzname;
	}

	/**
	 * Setter of the property <tt>kuerzel</tt>
	 * 
	 * @param username
	 *            The Username to set.
	 * @uml.property name="Kurzname"
	 */
	public void setKurzname(String kurzname) {
		this.kurzname = kurzname;
	}

	/**
	 * @uml.property name="Geburtsdatum"
	 */
	private Calendar geburtsdatum;

	/**
	 * Getter of the property <tt>Geburtsdatum</tt>
	 * 
	 * @return Returns the geburtsdatum.
	 * @uml.property name="Geburtsdatum"
	 */
	public Calendar getGeburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Setter of the property <tt>Geburtsdatum</tt> Nur Datum . Zeit wird auf 0
	 * gesetzt.
	 * 
	 * @param Geburtsdatum
	 *            The geburtsdatum to set.
	 * @uml.property name="Geburtsdatum"
	 */
	public void setGeburtsdatum(Calendar geburtsdatum) {
		geburtsdatum.set(Calendar.HOUR, 0);
		geburtsdatum.set(Calendar.MINUTE, 0);
		geburtsdatum.set(Calendar.SECOND, 0);
		geburtsdatum.set(Calendar.MILLISECOND, 0);
		this.geburtsdatum = geburtsdatum;
		this.calculateVolljaehrig();

	}

	public void setGeburtsdatum(int tag, int monat, int jahr) {
		Calendar c1 = Calendar.getInstance();
		c1.set(jahr, monat, tag);
		this.setGeburtsdatum(c1);
	}

	public void setGeburtsdatum(String str) {
		Calendar c1 = Helper.stringToCalendar(str);
		if (c1 != null) {
			this.setGeburtsdatum(c1);
		}
	}

	/**
	 * @uml.property name="Familienname"
	 */
	private String familienname;

	/**
	 * Getter of the property <tt>name</tt>
	 * 
	 * @return Returns the name.
	 * @uml.property name="Familienname"
	 */
	public String getFamilienname() {
		return familienname;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * 
	 * @param name
	 *            The name to set.
	 * @uml.property name="Familienname"
	 */
	public void setFamilienname(String familienname) {
		this.familienname = familienname;
	}

	/**
	 * @uml.property name="Geschlecht"
	 */
	private String geschlecht;

	/**
	 * Getter of the property <tt>Geschlecht</tt>
	 * 
	 * @return Returns the geschlecht.
	 * @uml.property name="Geschlecht"
	 */
	public String getGeschlecht() {
		return geschlecht;
	}

	/**
	 * Setter of the property <tt>Geschlecht</tt> <br>
	 * TODO: nur m oder w akzeptieren
	 * 
	 * @param Geschlecht
	 *            The geschlecht to set.
	 * @uml.property name="Geschlecht"
	 */
	public void setGeschlecht(String geschlecht) {
		// TODO: nur m oder w akzeptieren
		this.geschlecht = geschlecht;
	}

	/**
	 * @uml.property name="Klasse"
	 */
	private String klasse;

	/**
	 * Getter of the property <tt>Klasse</tt>
	 * 
	 * @return Returns the klasse.
	 * @uml.property name="Klasse"
	 */
	public String getKlasse() {
		return klasse;
	}

	/**
	 * Setter of the property <tt>Klasse</tt> <br>
	 * TODO: nur erlaubt Klassen prüfe gegen eine Liste von Klassen konvertiere
	 * in WebUntis Klassen (ALias) gegenüber Sekretariatsklassen
	 * 
	 * @param Klasse
	 *            The klasse to set.
	 * @uml.property name="Klasse"
	 */
	public void setKlasse(String klasse) {
		this.klasse = klasse;
	}

	/**
	 * @uml.property name="Text"
	 */
	private String text;

	/**
	 * Getter of the property <tt>Text</tt>
	 * 
	 * @return Returns the text.
	 * @uml.property name="Text"
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter of the property <tt>Text</tt>
	 * 
	 * wird für die Konfession verwendet: berücksichtigt nicht Abwahl oder
	 * Wechsel des Unterrichts nach Ethik
	 * 
	 * @param Text
	 *            The text to set.
	 * @uml.property name="Text"
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @uml.property name="Schulpflicht"
	 */
	private boolean schulpflicht;

	/**
	 * Getter of the property <tt>Schulpflicht</tt>
	 * 
	 * @return Returns the schulpflicht.
	 * @uml.property name="Schulpflicht"
	 */
	public boolean isSchulpflicht() {
		return schulpflicht;
	}

	/**
	 * Setter of the property <tt>Schulpflicht</tt>
	 * 
	 * @param Schulpflicht
	 *            The schulpflicht to set.
	 * @uml.property name="Schulpflicht"
	 */
	public void setSchulpflicht(boolean schulpflicht) {
		this.schulpflicht = schulpflicht;
	}

	/**
	 * @uml.property name="Volljaehrig"
	 */
	private boolean volljaehrig;

	/**
	 * Getter of the property <tt>Volljaehrig</tt>
	 * 
	 * @return Returns the volljaehrig.
	 * @uml.property name="Volljaehrig"
	 */
	public boolean isVolljaehrig() {
		return volljaehrig;
	}

	/**
	 * Setter of the property <tt>Volljaehrig</tt>
	 * 
	 * @param Volljaehrig
	 *            The volljaehrig to set.
	 * @uml.property name="Volljaehrig"
	 */
	public void setVolljaehrig(boolean volljaehrig) {
		this.volljaehrig = volljaehrig;
	}

	public int age() {
		GregorianCalendar cal = new GregorianCalendar();
		int y, d, a;

		y = cal.get(Calendar.YEAR);
		d = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(this.getGeburtsdatum().getTime());
		a = y - cal.get(Calendar.YEAR);
		if (d < cal.get(Calendar.DAY_OF_YEAR)) {
			--a;
		}
		return (a);
	}

	private void calculateVolljaehrig() {

		if (age() >= 18) {
			this.setVolljaehrig(true);
		} else {
			this.setVolljaehrig(false);
		}

	}

	/**
	 * @uml.property name="Aktiv"
	 */
	private boolean aktiv;

	/**
	 * Getter of the property <tt>Aktiv</tt>
	 * 
	 * @return Returns the aktiv.
	 * @uml.property name="Aktiv"
	 */
	public boolean isAktiv() {
		return aktiv;
	}

	/**
	 * Setter of the property <tt>Aktiv</tt>
	 * 
	 * @param Aktiv
	 *            The aktiv to set.
	 * @uml.property name="Aktiv"
	 */
	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	/**
	 * @uml.property name="Attestpflicht"
	 */
	private boolean attestpflicht;

	/**
	 * Getter of the property <tt>isAttestpflicht</tt>
	 * 
	 * @return Returns the isAttestpflicht.
	 * @uml.property name="Attestpflicht"
	 */
	public boolean isAttestpflicht() {
		return attestpflicht;
	}

	/**
	 * Setter of the property <tt>isAttestpflicht</tt>
	 * 
	 * @param isAttestpflicht
	 *            The isAttestpflicht to set.
	 * @uml.property name="Attestpflicht"
	 */
	public void setAttestpflicht(boolean attestpflicht) {
		this.attestpflicht = attestpflicht;
	}

	/**
	 * @uml.property name="SchluesselIntern"
	 */
	private String schluesselIntern;

	/**
	 * Getter of the property <tt>SchluesselIntern</tt>
	 * 
	 * @return Returns the schluesselIntern.
	 * @uml.property name="SchluesselIntern"
	 */
	public String getSchluesselIntern() {
		return schluesselIntern;
	}

	/**
	 * Setter of the property <tt>SchluesselIntern</tt>
	 * 
	 * @param SchluesselIntern
	 *            The schluesselIntern to set.
	 * @uml.property name="SchluesselIntern"
	 */
	public void setSchluesselIntern(String schluesselIntern) {
		this.schluesselIntern = schluesselIntern;
	}

	/**
	 * @uml.property name="SchluesselExtern"
	 */
	private String schluesselExtern;

	/**
	 * Getter of the property <tt>ExterneI</tt>
	 * 
	 * @return Returns the externeI.
	 * @uml.property name="SchluesselExtern"
	 */
	public String getSchluesselExtern() {
		return schluesselExtern;
	}

	/**
	 * Setter of the property <tt>ExterneI</tt>
	 * 
	 * @param ExterneI
	 *            The externeI to set.
	 * @uml.property name="SchluesselExtern"
	 */
	public void setSchluesselExtern(String schluesselExtern) {
		this.schluesselExtern = schluesselExtern;
	}

	/**
	 * @uml.property name="Benutzergruppe"
	 */
	private String benutzergruppe;

	/**
	 * Getter of the property <tt>Benutzergruppe</tt> <br>
	 * TODO: Gegen eine Liste der WebUntis erlaubten Benutzergruppen checken
	 * 
	 * @return Returns the benutzergruppe.
	 * @uml.property name="Benutzergruppe"
	 */
	public String getBenutzergruppe() {
		return benutzergruppe;
	}

	/**
	 * Setter of the property <tt>Benutzergruppe</tt>
	 * 
	 * @param Benutzergruppe
	 *            The benutzergruppe to set.
	 * @uml.property name="Benutzergruppe"
	 */
	public void setBenutzergruppe(String benutzergruppe) {
		this.benutzergruppe = benutzergruppe;
	}

	/**
	 * @uml.property name="Passwort"
	 */
	private String passwort;

	/**
	 * Getter of the property <tt>Passwort</tt>
	 * 
	 * @return Returns the passwort.
	 * @uml.property name="Passwort"
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * Setter of the property <tt>Passwort</tt>
	 * 
	 * @param Passwort
	 *            The passwort to set.
	 * @uml.property name="Passwort"
	 */
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	/**
	 * @uml.property name="Sprache"
	 */
	private String sprache;

	/**
	 * Getter of the property <tt>Sprache</tt>
	 * 
	 * @return Returns the sprache.
	 * @uml.property name="Sprache"
	 */
	public String getSprache() {
		return sprache;
	}

	/**
	 * Setter of the property <tt>Sprache</tt>
	 * 
	 * @param Sprache
	 *            The sprache to set.
	 * @uml.property name="Sprache"
	 */
	public void setSprache(String sprache) {
		this.sprache = sprache;
	}

	/**
	 * @uml.property name="Eintrittsdatum"
	 */
	private Calendar eintrittsdatum;

	/**
	 * Getter of the property <tt>Eintrittsdatum</tt>
	 * 
	 * @return Returns the eintrittsdatum.
	 * @uml.property name="Eintrittsdatum"
	 */
	public Calendar getEintrittsdatum() {
		return eintrittsdatum;
	}

	/**
	 * Setter of the property <tt>Eintrittsdatum</tt>
	 * 
	 * @param Eintrittsdatum
	 *            The eintrittsdatum to set.
	 * @uml.property name="Eintrittsdatum"
	 */
	public void setEintrittsdatum(Calendar eintrittsdatum) {
		this.eintrittsdatum = eintrittsdatum;
	}

	public void setEintrittsdatum(int tag, int monat, int jahr) {
		Calendar c1 = Calendar.getInstance();
		c1.set(jahr, monat, tag);
		this.setEintrittsdatum(c1);
	}

	public void setEintrittsdatum(String str) {
		Calendar c1 = Helper.stringToCalendar(str);
		this.setEintrittsdatum(c1);
	}

	/**
	 * @uml.property name="Austrittsdatum"
	 */
	private Calendar austrittsdatum;

	/**
	 * Getter of the property <tt>Austrittsdatum</tt>
	 * 
	 * @return Returns the austrittsdatum.
	 * @uml.property name="Austrittsdatum"
	 */
	public Calendar getAustrittsdatum() {
		return austrittsdatum;
	}

	/**
	 * Setter of the property <tt>Austrittsdatum</tt>
	 * 
	 * @param Austrittsdatum
	 *            The austrittsdatum to set.
	 * @uml.property name="Austrittsdatum"
	 */
	public void setAustrittsdatum(Calendar austrittsdatum) {
		this.austrittsdatum = austrittsdatum;
	}

	public void setAustrittsdatum(int tag, int monat, int jahr) {
		Calendar c1 = Calendar.getInstance();
		c1.set(jahr, monat, tag);
		this.setAustrittsdatum(c1);
	}

	public void setAustrittsdatum(String str) {
		Calendar c1 = Helper.stringToCalendar(str);
		this.setAustrittsdatum(c1);
	}

}

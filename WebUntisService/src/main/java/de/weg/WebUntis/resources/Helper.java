package de.weg.WebUntis.resources;

import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.weg.WebUntis.converter.schueler.SpaltenToken;
import de.weg.WebUntis.model.Schueler;

/**
 * @author Rüttgers
 */
public class Helper {
	private static final Logger log = Logger.getLogger(Helper.class.getName());

	public static final String EMPTY_STRING = "";
	public static final String J = "J";
	public static final String N = "N";

	public static final Locale ISP_LOCALE = Locale.GERMAN;
	private static Collator collatorSecondary = Collator
			.getInstance(ISP_LOCALE);
	static {
		// eventuell wegen St und Sch eigene CollationRule entwerfen oder eigene
		// CollatorKlasse und compareTo überschreiben
		collatorSecondary.setStrength(Collator.SECONDARY);
	}

	public static Collator getCollator() {
		return collatorSecondary;
	}

	/**
	 * @uml.property name="collatorPrimary"
	 */
	private static Collator collatorPrimary = Collator.getInstance(ISP_LOCALE);
	static {
		// eventuell wegen St und Sch eigene CollationRule entwerfen oder eigene
		// CollatorKlasse und compareTo überschreiben
		collatorPrimary.setStrength(Collator.PRIMARY);
	}

	/**
	 * @return
	 * @uml.property name="collatorPrimary"
	 */
	public static Collator getCollatorPrimary() {
		return collatorPrimary;
	}

	final static String FIND_UMLAUTE = "äÄöÖüÜß";
	final static String REPLACE_UMLAUTE[] = { "ae", "Ae", "oe", "Oe", "ue",
			"Ue", "ss" };

	public static final String umlauteErsetzen(final String str) {
		final Pattern pattern = Pattern.compile("([" + FIND_UMLAUTE + "])");

		String result = str;
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			StringBuffer buffer = new StringBuffer(str.length());
			do {
				m.appendReplacement(buffer,
						REPLACE_UMLAUTE[FIND_UMLAUTE.indexOf(m.group())]);
			} while (m.find());
			m.appendTail(buffer);
			result = buffer.toString();
		}
		return result;
	}

	final static String FIND_SONDERZEICHEN = "êéèûúùâáàîíìôóòýçÊÉÈÛÚÙÂÁÀÎÍÌÔÓÒÝÇ";
	final static String REPLACE_SONDERZEICHEN[] = { "e", "e", "e", "u", "u",
			"u", "a", "a", "a", "i", "i", "i", "o", "o", "o", "y", "c", "E",
			"E", "E", "U", "U", "U", "A", "A", "A", "I", "I", "I", "O", "O",
			"O", "Y", "C" };

	public static final String sonderzeichenErsetzen(final String str) {
		final Pattern pattern = Pattern.compile("([" + FIND_SONDERZEICHEN
				+ "])");

		String result = str;
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			StringBuffer buffer = new StringBuffer(str.length());
			do {
				m.appendReplacement(buffer,
						REPLACE_SONDERZEICHEN[FIND_SONDERZEICHEN.indexOf(m
								.group())]);
			} while (m.find());
			m.appendTail(buffer);
			result = buffer.toString();
		}
		return result;
	}

	final static String FIND_ENTFERNEN = "-" + "_" + " " + "/" + "," + "."
			+ ";" + ":" + "´" + "`" + "^";

	public static final String sonderzeichenEntfernen(final String str) {
		final Pattern pattern = Pattern.compile("([" + FIND_ENTFERNEN + "])");

		String result = str;
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			StringBuffer buffer = new StringBuffer(str.length());
			do {
				m.appendReplacement(buffer, "");
			} while (m.find());
			m.appendTail(buffer);
			result = buffer.toString();
		}
		return result;
	}

	final static String FIND_ERLAUBT = "a-zA-Z";

	public static final String sonderzeichenNichtGefundenEntfernen(
			final String str) {
		final Pattern pattern = Pattern.compile("([^" + FIND_ERLAUBT + "])");

		String result = str;
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			StringBuffer buffer = new StringBuffer(str.length());
			do {
				m.appendReplacement(buffer, "");
			} while (m.find());
			m.appendTail(buffer);
			result = buffer.toString();
		}
		return result;
	}

	public static final String userNamePreparator(final String string) {
		return sonderzeichenErsetzen(umlauteErsetzen(sonderzeichenEntfernen(string)));
	}

	// keine Sonderzeichen. nur ascii erlaubt
	public static boolean userNameIsValid(String un) {
		return un.matches("[a-zA-Z]*");
	}

	public static final int AnzahlBuchstabenImNachnamen = 3;

	public static final String userNameGenerator(final String nachname,
			final String vorname) {
		StringBuffer nachnameErsetzt = new StringBuffer(
				userNamePreparator(nachname));
		StringBuffer vornameErsetzt = new StringBuffer(
				userNamePreparator(vorname));
		if (vornameErsetzt.length() > AnzahlBuchstabenImNachnamen) {
			vornameErsetzt = vornameErsetzt.delete(AnzahlBuchstabenImNachnamen,
					vornameErsetzt.length());
		}
		StringBuffer result = nachnameErsetzt.append(vornameErsetzt);
		return result.toString();
	}

	public static final String dfDate_DD_MM_YYYY_PATTERN = "dd.MM.yyyy";
	/**
	 * Pattern ist "dd.MM.yyyy" <br>
	 * DateFormat zum Parsen von Sekretariatsdatum und ausgegeben für WebUntis <br>
	 * Bsp.:21.03.1995
	 */
	public static final DateFormat dfDateDD_MM_YYYY = new SimpleDateFormat(
			dfDate_DD_MM_YYYY_PATTERN);

	public static final String dfDate_DD_MM_YYYY_HH_MM_PATTERN = "dd.MM.yyyy HH:MM";
	public static final DateFormat dfDateDD_MM_YYYY_HH_MM = new SimpleDateFormat(
			dfDate_DD_MM_YYYY_HH_MM_PATTERN);

	public static final Calendar stringToCalendar(String string) {
		Calendar c1 = null;
		if (!(string == null || string.isEmpty())) {
			try {
				Date date = dfDateDD_MM_YYYY.parse(string);
				c1 = Calendar.getInstance();
				c1.setTime(date);
			} catch (ParseException e) {
				log.log(Level.SEVERE, "String=" + string
						+ " doesnt match expected Format:"
						+ dfDate_DD_MM_YYYY_PATTERN, e);
			}
		}
		if (c1 == null) {
			Schueler.log.log(Level.FINE, "No Data or Datestring:" + string
					+ " doesnt match format");
		}
		return c1;

	}

	/**
	 * Trennt einen String bei dem PLZ und Ort zusammengefasst sind.Dabei wird
	 * die PLZ als der erste von einem Leerzeichen getrennte Teil des
	 * Stringsaufgefasst. Führende Leerzeichen und doppelte Leerzeichen werden
	 * eliminiert.
	 * 
	 * @param string
	 * @return Map<String><String> mit PLZ und Ort als <Key><Value>-Paar <br>
	 *         Key für PLZ ist durch PLZToken definiert <br>
	 *         Key für Ort ist durch ORTToken definiert
	 */
	public static final Map<SpaltenToken, String> PLZOrtToPLZ_Ort(String string) {
		// Auftrennen
		String[] strA = string.trim().split(" ");
		// Struktur schaffen um die Teile zu füllen
		Map<SpaltenToken, String> ret = new java.util.HashMap<SpaltenToken, String>();
		// PLZ
		ret.put(SpaltenToken.PLZ, strA[0].trim());
		// Ort
		StringBuffer ort = new StringBuffer();
		if (strA.length > 1) {
			ort.append(strA[1].trim());
			for (int i = 2; i < strA.length; i++) {
				ort.append(" ").append(strA[i].trim());
			}
		}
		ret.put(SpaltenToken.Ort, ort.toString().trim());

		return ret;
	}

	/**
	 * @param strValue
	 * @return
	 */
	public static boolean isBooleanString(String strValue) {
		return strValue.equals("Y") || strValue.endsWith("y")
				|| strValue.endsWith("j") || strValue.endsWith("J");
	}

	/**
	 * @param calendar
	 * @return
	 */
	public static String formatDatumStandard(Calendar calendar) {
		if (calendar != null) {
			return dfDateDD_MM_YYYY.format(calendar.getTime());
		}
		return Helper.EMPTY_STRING;
	}

	public static String formatBoolean(boolean schulpflicht) {
		if (schulpflicht) {
			return Helper.J;
		} else {
			return Helper.N;
		}
	}

	public static boolean isNoValue(String str) {
		return (str == null || str.isEmpty());
	}

	public static String klassenKonvrter(String klasse) {
		String nklasse = klasse;
		Map<String, String> map = getKlassMap(); 
		for(Map.Entry<String, String> e : map.entrySet())
		{
			if (nklasse.equals(e.getKey()))
			{
				nklasse = e.getValue();
				log.info("Klassenkonvertierung:"+e.getKey()+" nach "+e.getValue()+"   ergibt:"+nklasse);
				continue;
			}
		}

		return nklasse;
	}

	private static Map<String, String> getKlassMap() {
		// TODO als Property oder in db
		Map<String, String> kmap = new HashMap<String, String>();

		kmap.put("BKI/1", "BKI1");
		kmap.put("BKI/2", "BKI2");
		kmap.put("BKI/3", "BKI3");
		kmap.put("BKII1", "BKII1");
		kmap.put("BKII2", "BKII2");
		kmap.put("FH1", "FH1");
		kmap.put("FH2", "FH2");
		kmap.put("W1/1", "W1-1");
		kmap.put("W1/2", "W1-2");
		kmap.put("W2/1", "W2-1");
		kmap.put("W2/2", "W2-2");
		kmap.put("W2/3", "W2-3");
		kmap.put("WAG 10", "G10");
		kmap.put("WAG 8", "G8");
		kmap.put("WAG 9", "G9");
		kmap.put("WG 11/1", "11-1");
		kmap.put("WG 11/2", "11-2");
		kmap.put("WG 11/3", "11-3");
		kmap.put("WG 11/4", "11-4");
		kmap.put("WG 11/5", "11-5");
		kmap.put("WG 12/1", "12-1");
		kmap.put("WG 12/2", "12-2");
		kmap.put("WG 12/3", "12-3");
		kmap.put("WG 12/4", "12-4");
		kmap.put("WG 12/5", "12-5");
		kmap.put("WG 13/1", "13-1");
		kmap.put("WG 13/2", "13-2");
		kmap.put("WG 13/3", "13-3");
		kmap.put("WG 13/4", "13-4");
		kmap.put("WG 13/5", "13-5");
		
		
		kmap.put("1KB3S", "1KB3V");
		kmap.put("1KB3", "1KB3V");
		kmap.put("2VF1W", "2VF1");
		kmap.put("1BA1W", "1BA1");
		kmap.put("2TK1W", "2TK1");
		kmap.put("1EFKI", "1EF");
		kmap.put("2VF2S", "2VF2");
		kmap.put("1KB3W", "1KB3");
		kmap.put("1VF2S", "1VF2");
		kmap.put("2KK2W", "2KK2");
		kmap.put("1VF1W", "1VF1");
		kmap.put("1EFKG", "1EF");
		kmap.put("1VF2W", "1VF2");
		kmap.put("1LO1W", "1LO1");
		kmap.put("1LO2W", "1LO2");
		kmap.put("1VF1S", "1VF1");
		kmap.put("2KK1W", "2KK1");

		return kmap;
	}
}

package de.weg.WebUntis.converter.schueler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.weg.WebUntis.converter.AbstractSchreiber;
import de.weg.WebUntis.model.Schueler;
import de.weg.WebUntis.resources.Helper;
import de.weg.WebUntis.resources.WUProperties;

public class SchreiberSchuelerCSV extends AbstractSchreiber {
	private static final Logger log = Logger
			.getLogger(SchreiberSchuelerCSV.class.getName());
	
	private static String VALUE_SEPERATOR = ";";
	private static String LINE_SEPERATOR = "\n";

	public void exportData(Properties einstellungen, List<?> data) {
		try {
			// prepare
			BufferedWriter bw = erzeugeWriter(einstellungen);
			
			// export
			// kopfzeile
			schreibeHeader(bw);
			// daten der liste
			schreibeDaten(bw, data);
			
			// finish
			if (bw != null) {
				bw.close();
			}

		} catch (IOException e) {
			log.log(Level.SEVERE, "FEHLER:", e);
		}
	}

	protected BufferedWriter erzeugeWriter(Properties einstellungen) throws IOException {
		String fileName = einstellungen.getProperty(WUProperties.ExportFileName);
		File file = new File(fileName);

		log.info("File:open:" + file.getAbsolutePath());
		file.delete();
		log.info("File:delete:" + file.getAbsolutePath());
		file.createNewFile();
		log.info("File:recreate:" + file.getAbsolutePath());
		
		FileWriter fw = new FileWriter(file);
		log.info("FileWriter ok");
		BufferedWriter bw = new BufferedWriter(fw);
		log.info("BufferedWriter ok" );

		return bw;
	}

	private void schreibeDaten(BufferedWriter bw, List<?> data) throws IOException {

			// daten
			for (Object obj : data)
			{
				if (!(obj instanceof Schueler))
				{
					log.warning("Skipping listentry because: Schueler expected but found:"
							+ obj);
					continue;
				}
				schreibeSchueler((Schueler) obj, bw);
				//Abschluss der Zeile
				schreibeZeilenAbschluss(bw);


			}

	}

	private void schreibeSchueler(Schueler sch, BufferedWriter bw)
			throws IOException {
		log.warning("Schueler daten schreiben");
		
		// Die einzelnen Werte in derselben Reiehnfolge wie die Headerdaten:
//		SCHLUESSELEXTERN
		schreibeWert(bw, sch.getSchluesselExtern());
//		KURZNAME
		schreibeWert(bw, sch.getKurzname());
//		PASSWORT
		schreibeWert(bw, sch.getPasswort());
//		FAMILIENNAME
		schreibeWert(bw, sch.getFamilienname());
//		VORNAME
		schreibeWert(bw, sch.getVorname());
//		GEBURTSDATUM
		schreibeWert(bw, Helper.dfDateDD_MM_YYYY.format(sch.getGeburtsdatum().getTime()));
//		PLZ
		schreibeWert(bw, sch.getPLZ());
//		ORT
		schreibeWert(bw, sch.getStadt());
//		STRASSE
		schreibeWert(bw, sch.getStrasse());
//		TELEFONNUMMER
		schreibeWert(bw, sch.getTelefonnummer());
//		KLASSE
		schreibeWert(bw, sch.getKlasse());
//		GESCHLECHT
		schreibeWert(bw, sch.getGeschlecht());
//		TEXT
		schreibeWert(bw, sch.getText());
//		EINTRITTSDATUM
		schreibeWert(bw, Helper.formatDatumStandard(sch.getEintrittsdatum()));
//		AUSTRITTSDATUM
		schreibeWert(bw, Helper.formatDatumStandard(sch.getAustrittsdatum()));		
//		SCHULPFLICHT
		schreibeWert(bw, Helper.formatBoolean(sch.isSchulpflicht()));		
//		VOLLJ�HRIG
		schreibeWert(bw, Helper.formatBoolean(sch.isVolljaehrig()));		
//		BENUTZERGRUPPE
		schreibeWert(bw, sch.getBenutzergruppe());	
		// EMail
		schreibeWert(bw, sch.getEMail());
		// Bemerkung
		schreibeWert(bw, sch.getBemerkung());
	}

	/**
	 * @param bw
	 * @param strV
	 * @throws IOException
	 */
	private void schreibeWert(BufferedWriter bw, String strV)
			throws IOException {
		if (strV==null)
		{
			strV=Helper.EMPTY_STRING;
		}
		bw.append(strV);
		bw.append(VALUE_SEPERATOR);
	}

	/**
	 * TODO: wie kann man sicherstellen,dass die Reiehnfolge der Headertoken wie die der restlichen Daten geschrieben werden.
	 * Idee: �ber ENUM und Map f�r die Schuler anstelle von eigener Class.  
	 * @param bw
	 * @throws IOException
	 */
	private void schreibeHeader(BufferedWriter bw) throws IOException {
		log.warning("Header daten schreiben");
		schreibeHeaderWert(bw, SpaltenToken.SchuelerNr );
		schreibeHeaderWert(bw, SpaltenToken.Anmeldenamen);
		schreibeHeaderWert(bw, SpaltenToken.Kennwort);
		schreibeHeaderWert(bw, SpaltenToken.Familienname);
		schreibeHeaderWert(bw, SpaltenToken.Vorname);
		schreibeHeaderWert(bw, SpaltenToken.GebDat);
		schreibeHeaderWert(bw, SpaltenToken.PLZ);
		schreibeHeaderWert(bw, SpaltenToken.Ort);
		schreibeHeaderWert(bw, SpaltenToken.Strasse);
		schreibeHeaderWert(bw, SpaltenToken.Telefon);
		schreibeHeaderWert(bw, SpaltenToken.Klasse);
		schreibeHeaderWert(bw, SpaltenToken.Geschlecht);
		schreibeHeaderWert(bw, SpaltenToken.Konfession);
		schreibeHeaderWert(bw, SpaltenToken.DatumEinschulung);
		schreibeHeaderWert(bw, SpaltenToken.DatumEntlassung);
		schreibeHeaderWert(bw, SpaltenToken.Schulpflicht);
		schreibeHeaderWert(bw, SpaltenToken.Volljaehrig);
		schreibeHeaderWert(bw, SpaltenToken.Benutzergruppe);
		schreibeHeaderWert(bw, SpaltenToken.EMail);
		schreibeHeaderWert(bw, SpaltenToken.Bemerkung);
		//Abschluss der Zeile
		schreibeZeilenAbschluss(bw);

	}

	private void schreibeHeaderWert(BufferedWriter bw, SpaltenToken spaltenToken) throws IOException {
		schreibeWert(bw,SpalteWU.getMapping().get(spaltenToken).getSpaltenName());
		
	}

	/**
	 * @param bw
	 * @throws IOException
	 */
	private void schreibeZeilenAbschluss(BufferedWriter bw) throws IOException {
		bw.append(LINE_SEPERATOR);
	}

}

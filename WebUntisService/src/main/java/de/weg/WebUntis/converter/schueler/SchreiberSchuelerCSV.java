package de.weg.WebUntis.converter.schueler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
			Writer bw = erzeugeWriter(einstellungen);
			
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

	protected Writer erzeugeWriter(Properties einstellungen) throws IOException {
		String fileName = einstellungen.getProperty(WUProperties.StammdatenFile);
		File file = new File(fileName);
		String encoding = einstellungen.getProperty(WUProperties.ExportFileEncoding);

		log.info("File:open:" + file.getAbsolutePath());
		file.delete();
		log.info("File:delete:" + file.getAbsolutePath());
		file.createNewFile();
		log.info("File:recreate:" + file.getAbsolutePath());
		
		FileWriter fw = new FileWriter(file);
		log.info("FileWriter ok");
		
		
		log.info("Prepare for encoding with "+encoding);
		Writer bw  = new OutputStreamWriter(new FileOutputStream(file), encoding);
		log.info("BufferedWriter ok" );

		return bw;
	}

	private void schreibeDaten(Writer bw, List<?> data) throws IOException {

			// daten
		    int c = 0;
		    int e = 0;
			for (Object obj : data)
			{
		
				if (!(obj instanceof Schueler))
				{
					e++;
					log.warning("Skipping listentry because: Schueler expected but found:"
							+ obj);
					continue;
				}
				c++;
				schreibeSchueler((Schueler) obj, bw);
				//Abschluss der Zeile
				schreibeZeilenAbschluss(bw);


			}
			log.info("=====================================================================================================================================");
			log.info("===============================================                STAMMDATEN              ==============================================");
			log.info("Liste umfasste:" + data.size()  + "-----------"+"Geschrieben Elemente:"+c+ "-----------"+"Fehlerhaft Elemente:"+e);
			log.info("=====================================================================================================================================");
			log.info("=====================================================================================================================================");


	}

	private void schreibeSchueler(Schueler sch, Writer bw)
			throws IOException {
		log.fine("Schueler daten schreiben");
		
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
//		VOLLJÄHRIG
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
	private void schreibeWert(Writer bw, String strV)
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
	 * Idee: Über ENUM und Map für die Schuler anstelle von eigener Class.  
	 * @param bw
	 * @throws IOException
	 */
	private void schreibeHeader(Writer bw) throws IOException {
		log.fine("Header daten schreiben");
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

	private void schreibeHeaderWert(Writer bw, SpaltenToken spaltenToken) throws IOException {
		schreibeWert(bw,SpalteWU.getMapping().get(spaltenToken).getSpaltenName());
		
	}

	/**
	 * @param bw
	 * @throws IOException
	 */
	private void schreibeZeilenAbschluss(Writer bw) throws IOException {
		bw.append(LINE_SEPERATOR);
	}

}

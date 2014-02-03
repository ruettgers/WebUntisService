package de.weg.WebUntis.converter.ausdrucke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.weg.WebUntis.converter.schueler.SpalteWU;
import de.weg.WebUntis.converter.schueler.SpaltenToken;
import de.weg.WebUntis.model.Schueler;
import de.weg.WebUntis.resources.Helper;
import de.weg.WebUntis.resources.WUProperties;

public class ExportZuPassword {

	private static final Logger log = Logger.getLogger(ExportZuPassword.class
			.getName());

	public static void main(String[] args) {
		List<Properties> einstellungen = getFileList();
		for (Properties prop : einstellungen) {
			List<?> schueler = leseDaten(prop);
			schreibeDaten(prop, schueler);
		}

	}

	public static List<Properties> getFileList() {

		List<Properties> propertyList = new ArrayList<Properties>();
		// String filedir =
		// "P:/Data/workspace/webtools/WebUntis-ImportKonverter";
		String filedir = ".";
		String filedirim = filedir
				+ "/"
				+ WUProperties.getInstance().getProperty(
						WUProperties.Default_FileDirExport);
		String filedirex = filedir
				+ "/"
				+ WUProperties.getInstance().getProperty(
						WUProperties.Default_FileDirExport);

		Properties p;
		p = new Properties();
		p.put(WUProperties.ImportFileName, filedirim + "/" + WUProperties.getInstance().getProperty(WUProperties.ExportFileName));
		p.put(WUProperties.ExportFileName, filedirex + "/" + WUProperties.getInstance().getProperty(WUProperties.PasswordFileNameName));
		propertyList.add(p);
		
		

		return propertyList;
	}

	public static List<?> leseDaten(Properties einstellungen) {

		List<Schueler> result = new java.util.LinkedList<Schueler>();
		// Open File

		String fileName = einstellungen
				.getProperty(WUProperties.ImportFileName);

		BufferedReader br = null;
		try {
			log.log(Level.INFO, "File to open:" + fileName);
			//
			br = oeffneFile(fileName);
			//
			verarbeiteFile(br, result);
			//
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "FEHLER:", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "FEHLER:", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.log(Level.SEVERE, "FEHLER:", e);
				}
			}
		}

		log.log(Level.INFO, "Dateneingelsen.");
		return result;

	}

	/**
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	private static BufferedReader oeffneFile(String fileName)
			throws FileNotFoundException {
		BufferedReader br;
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		br = new BufferedReader(fr);
		return br;
	}

	/**
	 * @param br
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private static void verarbeiteFile(BufferedReader br, List<Schueler> result)
			throws IOException {

		String line = null;
		// header
		if ((line = br.readLine()) != null) {
			checkHeader(line);
		}
		// data
		while ((line = br.readLine()) != null) {
			Schueler sch = leseZeile(line);
			result.add(sch);
		}
	}

	/**
	 * @param line
	 */
	private static void checkHeader(String line) {
		// Prüfe erste Zeile auf die notwendigen Header:
		String[] header = line.split(";");
		// TODO prüfung und fehlermeldung mit throw....
	}

	/**
	 * @param line
	 * @return
	 */
	private static Schueler leseZeile(String zeile) {
		Schueler element = new Schueler();
		String[] zeilendaten = zeile.split(";");

		log.log(Level.FINE, "Extracting values");
		// durchlaufe die Zeile und extrahiere die relevanten zellen
		String strValue;
		// Familienname
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Familienname);
		element.setFamilienname(strValue);
		// Vorname
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Vorname);
		element.setVorname(strValue);
		// Geburtsdatum
		strValue = leseStringZelle(zeilendaten, SpaltenToken.GebDat);
		element.setGeburtsdatum(strValue);
		// Plz
		strValue = leseStringZelle(zeilendaten, SpaltenToken.PLZ);
		element.setPLZ(strValue);
		// Ort
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Ort);
		element.setStadt(strValue);
		// Strasse
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Strasse);
		element.setStrasse(strValue);
		// Klasse
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Klasse);
		element.setKlasse(strValue);
		// Kurzname
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Anmeldenamen);
		element.setKurzname(strValue);
		// Passwort
		strValue = leseStringZelle(zeilendaten, SpaltenToken.Kennwort);
		element.setPasswort(strValue);

		return element;
	}

	/**
	 * @param row
	 * @param SpalteWU.getMapping().get(familienname).g
	 * @return
	 */
	private static String leseStringZelle(String[] line, SpaltenToken token) {
		String strValue = "";
		int cellno = SpalteWU.getMapping().get(token).getSpaltenNummer();
		strValue = line[cellno];
		return strValue;
	}


	private static Font kopfFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
	private static Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,	Font.NORMAL);
	private static Font textBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,	Font.UNDERLINE);
	private static Font benutzerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLDITALIC);
	private static Font passwortFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.ITALIC);

	public static void schreibeDaten(Properties einstellungen,
			List<?> schuelerliste) {
		try {
			// prepare
			Document pdf = erzeugeWriter(einstellungen);

			// daten der liste
			schreibeDaten(pdf, schuelerliste);

			// finish
			if (pdf != null) {
				pdf.close();
			}

		} catch (IOException e) {
			log.log(Level.SEVERE, "FEHLER:", e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "FEHLER:", e);
		}
	}

	protected static Document erzeugeWriter(Properties einstellungen)
			throws IOException, DocumentException {
		String fileName = einstellungen
				.getProperty(WUProperties.ExportFileName);
		File file = new File(fileName);

		log.info("File:open:" + file.getAbsolutePath());
		file.delete();
		log.info("File:delete:" + file.getAbsolutePath());
		file.createNewFile();
		log.info("File:recreate:" + file.getAbsolutePath());

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		log.info("PDF instanz bereit");
		return document;
	}

	private static void schreibeDaten(Document pdf, List<?> data)
			throws IOException, DocumentException {

		// daten
		for (Object obj : data) {
			if (!(obj instanceof Schueler)) {
				log.warning("Skipping listentry because: Schueler expected but found:"
						+ obj);
				continue;
			}
			// Text
			schreibeHeader(pdf, data);
			// Schuelerdaten
			schreibeSchueler((Schueler) obj,pdf);
			// Abschluss der Zeile
			schreibeDocumentenAbschluss(pdf);

		}

	}

	private static void schreibeDocumentenAbschluss(Document pdf) {
	    pdf.newPage();
		
	}

	private static void schreibeHeader(Document document, List<?> data)
			throws IOException, DocumentException {
		
		//Metadaten
		  document.addTitle("WebUntisAccount am WEG");
		    document.addSubject("Daten zum WebUntisAccount am WEG");
		    document.addKeywords("WebUntis, WEG");
		    document.addAuthor("webuntis@weg-freiburg.de");
		    document.addCreator("Rü");
		    
		    // Einführungstext
		    Paragraph preface = new Paragraph();
		    // We add one empty line
		    addEmptyLine(preface, 1);
		    // Lets write a big header
		    preface.add(new Paragraph("Anmeldaten zum WebUntisAccount am WEG vom "  + Helper.dfDateDD_MM_YYYY_HH_MM.format(new Date()), kopfFont));
		    addEmptyLine(preface, 2);
		    preface.add(new Paragraph("WICHTIG:Kennwort nach der Anmeldung in WebUntis unbedingt ändern!",
			        redFont));
		    preface.add(new Paragraph("Die Anleitung dazu finden Sie im seperaten Handzettel.",textFont));
		    addEmptyLine(preface, 1);
			
		    document.add(preface);
		    
	}

	private static void schreibeSchueler(Schueler sch, Document pdf)
			throws IOException, DocumentException {
		log.fine("Schueler daten schreiben");
	    Paragraph schueler = new Paragraph();
	    schueler.add(new Paragraph("Benutzername:"+ sch.getKurzname(), benutzerFont));
	    schueler.add(new Paragraph("Kennwort:"+ sch.getPasswort(), passwortFont));
	    addEmptyLine(schueler, 1);
	    schueler.add(new Paragraph("Ihre persönlichen Daten zur Überprüfung:", textBold));
	    schueler.add(new Paragraph(sch.getVorname() + " "+sch.getFamilienname(), textFont));
	    schueler.add(new Paragraph(sch.getPLZ() + " "+sch.getStadt(), textFont));
	    schueler.add(new Paragraph(sch.getStrasse(), textFont));
	    schueler.add(new Paragraph("Geburtsdatum:"+schreibeCalendar(sch.getGeburtsdatum()), textFont));
	    schueler.add(new Paragraph("Klasse:"+sch.getKlasse(), textFont));
	    
	    pdf.add(schueler);
	}

	private static String schreibeCalendar(Calendar calendar)
			 {
		String strV = "";
		if (calendar != null) {
			strV = Helper.dfDateDD_MM_YYYY.format(calendar.getTime());
		}
		return strV;
	}


	  private static void addEmptyLine(Paragraph paragraph, int number) {
		    for (int i = 0; i < number; i++) {
		      paragraph.add(new Paragraph(" "));
		    }
		  }
}

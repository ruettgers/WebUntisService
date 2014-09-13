package de.weg.WebUntis.converter.schueler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import de.weg.WebUntis.converter.AbstractLeser;
import de.weg.WebUntis.converter.Spalte;
import de.weg.WebUntis.model.Schueler;
import de.weg.WebUntis.resources.Helper;
import de.weg.WebUntis.resources.WUProperties;

public abstract class LeserSchuelerXLS extends AbstractLeser {

	static final Logger log = Logger.getLogger( LeserSchuelerXLS.class.getName() );
	

	protected static Map<SpaltenToken, Spalte> SRTM=null;
	public Map<SpaltenToken, Spalte> getMapping()
	{
		if (null == SRTM) 
		{
			SRTM = new HashMap<SpaltenToken, Spalte>();
			initMapping();
		}
		return SRTM;
	}

	abstract protected void initMapping();

	public List<?> loadData(Properties einstellungen) {

		// pen File
		String fileName = einstellungen.getProperty(WUProperties.ImportFile);
		String sheetNoStr = einstellungen.getProperty(WUProperties.SheetNummer);
		int sheetNo = Integer.parseInt(sheetNoStr);
		log.log(Level.INFO,"File to open:"+fileName);
		
		List<Schueler> result = new java.util.LinkedList<Schueler>();
		try {
			// File oeffnen
			FileInputStream inp = new FileInputStream(fileName);
			log.log(Level.INFO,"InputStream open with Bytes="+inp.available());
			// Daten lesen
			
			 handleFile(inp,result,sheetNo);
			// File schliessen
			inp.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * @param inp
	 * @param result 
	 * @param sheetNo 
	 * @return 
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private void handleFile(InputStream inp, List<Schueler> result, int sheetNo) throws IOException,
			InvalidFormatException {
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet1 = wb.getSheetAt(sheetNo-1);
		checkHeader(sheet1);
		leseDaten(sheet1, result);
	}

	/**
	 * @param sheet1
	 */
	private void checkHeader(Sheet sheet1) {
		// Prüfe erste Zeile auf die notwendigen Header:
		Row header = sheet1.getRow(sheet1.getTopRow());
		// TODO prüfung und fehlermeldung mit throw....
	}

	/**
	 * @param sheet1
	 * @param result 
	 * @return 
	 */
	private void leseDaten(Sheet sheet1, List<Schueler> result) {
		int c = 0;
		for (int i = sheet1.getTopRow() + 1; i <= sheet1.getLastRowNum(); i++) {
			c++;
			log.log(Level.FINE, "Handle row:"+i);
			Row row = sheet1.getRow(i);
			Schueler element = leseZeile(row);
			
			log.log(Level.FINE, "Row:"+i+" Add to list:"+element);	
			result.add(element);
		}
		log.info("=====================================================================================================================================");
		log.info("=====================================================================================================================================");
		log.info("Zeieln in File:"+sheet1.getLastRowNum() + "-----------"+"Behandelte Zeilen:"+c  + "-----------"+"Erzeugte Elemente:"+result.size());
		log.info("=====================================================================================================================================");
		log.info("=====================================================================================================================================");
		
	}

	/**
	 * @param row
	 * @return
	 */
	protected Schueler leseZeile(Row row) {
		Schueler element = new Schueler();
		element.setDefaultValues();	
	
		log.log(Level.FINE, "Extracting cells:"+row.getRowNum());	
		// durchlaufe die Zeile und extrahiere die relevanten zellen 
		String strValue;
		// Schluesselextern
		strValue = leseStringZelle(row, SpaltenToken.SchuelerNr);
		element.setSchluesselExtern(strValue);
		// Familienname
		strValue = leseStringZelle(row, SpaltenToken.Familienname);
		element.setFamilienname(strValue);
		// Vorname
		strValue = leseStringZelle(row, SpaltenToken.Vorname);
		element.setVorname(strValue);
		// Geburtsdatum
		strValue = leseStringZelle(row, SpaltenToken.GebDat);
		element.setGeburtsdatum(strValue);
		// Geschlecht
		strValue = leseStringZelle(row, SpaltenToken.Geschlecht);
		element.setGeschlecht(strValue);
		// Plz und Ort
		strValue = leseStringZelle(row, SpaltenToken.PLZOrt);
		Map values = Helper.PLZOrtToPLZ_Ort(strValue);
		element.setPLZ((String)values.get(SpaltenToken.PLZ));
		element.setStadt((String)values.get(SpaltenToken.Ort));
		// Strasse
		strValue = leseStringZelle(row, SpaltenToken.Strasse);
		element.setStrasse(strValue);
		// Klasse
		strValue = leseStringZelle(row, SpaltenToken.Klasse);
		// TODO: nur erlaubt Klassen - prüfe gegen eine Liste von Klassen -
		// konvertiere in WebUntis Klassen (ALias) gegenüber Sekretariatsklassen
		String nklasse = Helper.klassenKonvrter(strValue);
		element.setKlasse(nklasse);
		// Eintrittsdatum
		strValue = leseStringZelle(row, SpaltenToken.DatumEinschulung);
		element.setEintrittsdatum(strValue);
		// Austrittsdatum
		strValue = leseStringZelle(row, SpaltenToken.DatumEntlassung);
		element.setAustrittsdatum(strValue);
		// Text = Religion
		strValue = leseStringZelle(row, SpaltenToken.Konfession);
		element.setText(strValue);
		// Volljaehrig 
		// errechnen
		//strValue = leseStringZelle(row, SchuelerMappingSPlan.VOLLJAEHRIG);
		//element.setVolljaehrig( Helper.isBooleanString(strValue) );
		strValue = leseStringZelle(row, SpaltenToken.EMail);
		element.setEMail(strValue);
	
		return element;
	}


	/**
	 * @param row
	 * @param cellToken 
	 * @return
	 */
	String leseStringZelle(Row row, SpaltenToken cellToken) {
		String strValue="";
		int cellno = this.getMapping().get(cellToken).getSpaltenNummer();
		Cell cell = row.getCell(cellno);
		log.log(Level.FINE, "Cell:"+cell);	
		if(isValid(cell))
		{
			strValue = cell.getStringCellValue();
		}
		return strValue;
	}

	public static boolean isValid(Cell cell) {
		return cell!=null;
	}

}

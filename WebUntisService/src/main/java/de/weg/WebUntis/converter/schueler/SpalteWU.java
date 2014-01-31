package de.weg.WebUntis.converter.schueler;

import java.util.HashMap;
import java.util.Map;

import de.weg.WebUntis.converter.Spalte;

public class SpalteWU {
	
	protected static Map<SpaltenToken, Spalte> SRTM=null;
	
	public static Map<SpaltenToken, Spalte> getMapping()
	{
		if (null == SRTM) 
		{
			SRTM = new HashMap<SpaltenToken, Spalte>();
			initMapping();
		}
		return SRTM;
	}

	protected static void initMapping() 	
	{
		SRTM.put(SpaltenToken.SchuelerNr,new Spalte("SchuelerNr",1-1));
		SRTM.put(SpaltenToken.Anmeldenamen,new Spalte("Kurzname",2-1));
		SRTM.put(SpaltenToken.Kennwort,new Spalte("Passwort",3-1));
		SRTM.put(SpaltenToken.Familienname,new Spalte("Name",4-1));
		SRTM.put(SpaltenToken.Vorname,new Spalte("Vorname",5-1));
		SRTM.put(SpaltenToken.GebDat,new Spalte("GebDat",6-1));
		SRTM.put(SpaltenToken.PLZ,new Spalte("PLZ",7-1));
		SRTM.put(SpaltenToken.Ort,new Spalte("Ort",8-1));
		SRTM.put(SpaltenToken.Strasse,new Spalte("Strasse",9-1));
		SRTM.put(SpaltenToken.Telefon,new Spalte("Telefon",10-1));
		SRTM.put(SpaltenToken.Klasse,new Spalte("Klasse",11-1));
		SRTM.put(SpaltenToken.Geschlecht,new Spalte("Geschlecht",12-1));
		SRTM.put(SpaltenToken.Konfession,new Spalte("Konfession",13-1));
		SRTM.put(SpaltenToken.DatumEinschulung,new Spalte("DatumEinschl",14-1));
		SRTM.put(SpaltenToken.DatumEntlassung,new Spalte("DatEntlassung",15-1));
		SRTM.put(SpaltenToken.Schulpflicht,new Spalte("PflichtSch",16-1));
		SRTM.put(SpaltenToken.Volljaehrig,new Spalte("VolljahrJN",17-1));
		SRTM.put(SpaltenToken.Benutzergruppe,new Spalte("Benutzergruppe",18-1));
		SRTM.put(SpaltenToken.EMail,new Spalte("EMail",19-1));
		SRTM.put(SpaltenToken.Bemerkung,new Spalte("Bemerkung",20-1));

	}

}

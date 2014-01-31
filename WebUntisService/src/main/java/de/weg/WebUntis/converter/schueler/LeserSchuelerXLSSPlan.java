package de.weg.WebUntis.converter.schueler;

import de.weg.WebUntis.converter.Spalte;

public class LeserSchuelerXLSSPlan extends LeserSchuelerXLS {


	@Override
	protected void initMapping() {
		SRTM.put(SpaltenToken.SchuelerNr,new Spalte("SchuelerNr",1-1));
		SRTM.put(SpaltenToken.Familienname,new Spalte("Name",3-1));
		SRTM.put(SpaltenToken.Vorname,new Spalte("Vorname",4-1));
		SRTM.put(SpaltenToken.GebDat,new Spalte("GebDat",6-1));
		SRTM.put(SpaltenToken.Strasse,new Spalte("Strasse",9-1));
		// Besonderheit: zwei Werte sind hier verborgen und müssen auseinandergenommen werden:
		SRTM.put(SpaltenToken.PLZOrt,new Spalte("PLZOrt",10-1));
		SRTM.put(SpaltenToken.Telefon,new Spalte("Telefon",11-1));
		SRTM.put(SpaltenToken.Klasse,new Spalte("Klasse",16-1));
		SRTM.put(SpaltenToken.Geschlecht,new Spalte("Geschlecht",20-1));
		SRTM.put(SpaltenToken.Konfession,new Spalte("Konfession",22-1));
		SRTM.put(SpaltenToken.DatumEinschulung,new Spalte("DatumEinschl",51-1));
		SRTM.put(SpaltenToken.DatumEntlassung,new Spalte("DatEntlassung",52-1));
		SRTM.put(SpaltenToken.Schulpflicht,new Spalte("PflichtSch",69-1));
		
	}



}
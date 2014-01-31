package de.weg.WebUntis.converter.schueler;

import de.weg.WebUntis.converter.Spalte;

public class LeserSchuelerXLSSVP extends LeserSchuelerXLS {



	@Override
	protected void initMapping() 	
	{
		SRTM.put(SpaltenToken.SchuelerNr,new Spalte("Schueler_id",1-1));
		SRTM.put(SpaltenToken.Familienname,new Spalte("Name",182-1));
		SRTM.put(SpaltenToken.Vorname,new Spalte("Vorname",240-1));
		SRTM.put(SpaltenToken.GebDat,new Spalte("Geburtstag",147-1));
		SRTM.put(SpaltenToken.Strasse,new Spalte("Strasse",218-1));
		// Besonderheit: zwei Werte sind hier verborgen und müssen auseinandergenommen werden:
		SRTM.put(SpaltenToken.PLZOrt,new Spalte("PLZOrt",188-1));
		SRTM.put(SpaltenToken.Telefon,new Spalte("Telefon",222-1));
		SRTM.put(SpaltenToken.Klasse,new Spalte("Klasse",168-1));
		SRTM.put(SpaltenToken.Geschlecht,new Spalte("Geschlecht",151-1));
		SRTM.put(SpaltenToken.Konfession,new Spalte("Religion",194-1));
		SRTM.put(SpaltenToken.DatumEinschulung,new Spalte("Schuleintritt_am",202-1));
		SRTM.put(SpaltenToken.Schulpflicht,new Spalte("Schulpflicht",204-1));

	}


}
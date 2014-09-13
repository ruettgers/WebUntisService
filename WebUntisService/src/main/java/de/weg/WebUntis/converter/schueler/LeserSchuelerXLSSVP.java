package de.weg.WebUntis.converter.schueler;

import de.weg.WebUntis.converter.Spalte;

public class LeserSchuelerXLSSVP extends LeserSchuelerXLS {



	@Override
	protected void initMapping() 	
	{
		SRTM.put(SpaltenToken.SchuelerNr,new Spalte("Schueler_id",1-1));
		SRTM.put(SpaltenToken.Familienname,new Spalte("Name",186-1));
		SRTM.put(SpaltenToken.Vorname,new Spalte("Vorname",244-1));
		SRTM.put(SpaltenToken.GebDat,new Spalte("Geburtstag",148-1));
		SRTM.put(SpaltenToken.Strasse,new Spalte("Strasse",222-1));
		// Besonderheit: zwei Werte sind hier verborgen und müssen auseinandergenommen werden:
		SRTM.put(SpaltenToken.PLZOrt,new Spalte("PLZOrt",192-1));
		SRTM.put(SpaltenToken.Telefon,new Spalte("Telefon",226-1));
		SRTM.put(SpaltenToken.EMail,new Spalte("EMail",87-1));
		SRTM.put(SpaltenToken.Klasse,new Spalte("Klasse",172-1));
		SRTM.put(SpaltenToken.Geschlecht,new Spalte("Geschlecht",152-1));
		SRTM.put(SpaltenToken.Konfession,new Spalte("Religion",223-1));
		SRTM.put(SpaltenToken.DatumEinschulung,new Spalte("Schuleintritt_am",206-1));
		SRTM.put(SpaltenToken.DatumEntlassung,new Spalte("Schulabgang_am",203-1));
		SRTM.put(SpaltenToken.Schulpflicht,new Spalte("Schulpflicht",208-1));

	}


}
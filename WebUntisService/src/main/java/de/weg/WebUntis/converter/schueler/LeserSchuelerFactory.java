package de.weg.WebUntis.converter.schueler;

import java.util.logging.Logger;

import de.weg.WebUntis.converter.Leser;
import de.weg.WebUntis.converter.Verwaltungsart;

public class LeserSchuelerFactory {
	private static final Logger log = Logger.getLogger(LeserSchuelerFactory.class
			.getName());
	private LeserSchuelerFactory() {}

	public static Leser getInstance(String artToken) {
		// SchuelerMappingArt art = SchuelerMappingArt.valueOf(artToken);
		log.info("MappingArt:"+ artToken);

		Verwaltungsart art = Verwaltungsart.valueOf(artToken);
		switch (art) {
		case SVP:
			return new LeserSchuelerXLSSVP();			
		case SPlan:
			return new LeserSchuelerXLSSPlan();
		}
		
		StringBuffer strB = new StringBuffer();
		for (Verwaltungsart artv : Verwaltungsart.values())
		{
			strB = strB.append(artv.name());
			strB = strB.append(",");

		}
				
		throw new java.lang.IllegalArgumentException(
				"Token des Lesers bzw. Mappings ist nicht definiert. Propertydatei überprüfen, ob eine der in der Klasse SchuelerMappingArt(" + strB.toString()+ " definierten Werte verwendet wird.");

	}

}

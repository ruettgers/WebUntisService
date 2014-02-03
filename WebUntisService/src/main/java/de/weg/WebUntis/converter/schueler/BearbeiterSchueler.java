package de.weg.WebUntis.converter.schueler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.weg.WebUntis.converter.AbstractBearbeiter;
import de.weg.WebUntis.converter.Bearbeiter;
import de.weg.WebUntis.model.Schueler;

public class BearbeiterSchueler extends AbstractBearbeiter implements
		Bearbeiter {
	private static final Logger log = Logger.getLogger(BearbeiterSchueler.class
			.getName());

	public void bearbeite(List<?> liste) {

		
		log.log(Level.INFO, "Liste umfasst "+liste.size());
		int oki = 0;
		int fehler =0;
		for (Object obj : liste) {
			
			if (!(obj instanceof Schueler)) {
				fehler++;
				log.warning("Skipping list entry because: Schueler expected but found:"
						+ obj);
				continue;
			}

			Schueler schueler = (Schueler) obj;
			if (!schueler.isValid()) {
				fehler++;
				log.log(Level.WARNING, "Invalid Schueler:" + schueler);
				// TODO remove from list?
				continue;
			}
			oki++;
			log.log(Level.FINE, "Generate dependend values");
			schueler.generateDependendValues();

		}
		log.log(Level.INFO, "============================BearbeiterSchueler---BearbeiterSchueler======================================");
		log.log(Level.INFO, "===============================================================================================");
		log.log(Level.INFO, "Liste umfasst "+liste.size() +"davon error:"+fehler + " ok:"+oki);
		log.log(Level.INFO, "===============================================================================================");
		log.log(Level.INFO, "===============================================================================================");


	}

}

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

		for (Object obj : liste) {
			if (!(obj instanceof Schueler)) {
				log.warning("Skipping list entry because: Schueler expected but found:"
						+ obj);
				continue;
			}

			Schueler schueler = (Schueler) obj;
			if (!schueler.isValid()) {
				log.log(Level.WARNING, "Invalid Schueler:" + schueler);
				log.log(Level.INFO,
						"Invalid Schueler will not be handled further.");
				log.log(Level.INFO,
						"????????????????Maybe you want to remove schueler from list?????????.");
				// TODO remove from list?
				continue;
			}
			log.log(Level.INFO, "Generate dependend values");
			schueler.generateDependendValues();

		}

	}

}

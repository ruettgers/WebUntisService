package de.weg.WebUntis;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import de.weg.WebUntis.converter.Bearbeiter;
import de.weg.WebUntis.converter.Leser;
import de.weg.WebUntis.converter.Schreiber;
import de.weg.WebUntis.converter.ausdrucke.ExportZuPassword;
import de.weg.WebUntis.converter.schueler.LeserSchuelerFactory;
import de.weg.WebUntis.converter.schueler.BearbeiterSchueler;
import de.weg.WebUntis.converter.schueler.SchreiberSchuelerCSV;
import de.weg.WebUntis.converter.schueler.SchreiberUserCSV;
import de.weg.WebUntis.resources.WUProperties;

public class ImportSchuelerStart {
	private static final Logger log = Logger.getLogger(ImportSchuelerStart.class
			.getName());

	public static void main(String[] args) {

		// set properties
		List<Properties> arbeitsListe = WUProperties.getPropertyList();
		for (Properties einstellung : arbeitsListe) {
			log.info("Einstellungen:"+ einstellung);
			// import
			Leser leser = LeserSchuelerFactory.getInstance(einstellung.getProperty(WUProperties.MappingArt));
			List<?> schueler = leser.loadData(einstellung);

			// bearbeiten
			Bearbeiter bearbeiter = new BearbeiterSchueler();
			bearbeiter.bearbeite(schueler);

			// export stammdaten
			Schreiber schreiberStamm = new SchreiberSchuelerCSV();
			schreiberStamm.exportData(einstellung, schueler);
			// export user
			Schreiber schreiberUser = new SchreiberUserCSV();
			schreiberUser.exportData(einstellung, schueler);
		}
		
		List<Properties> einstellungen = ExportZuPassword.getFileList();
		for (Properties prop : einstellungen) {
			List<?> schueler = ExportZuPassword.leseDaten(prop);
			ExportZuPassword.schreibeDaten(prop, schueler);
		}

	}

}

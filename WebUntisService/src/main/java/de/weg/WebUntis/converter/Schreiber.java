package de.weg.WebUntis.converter;

import java.util.List;
import java.util.Properties;

public interface Schreiber {
	public  void exportData(Properties einstellungen, List<?> ergebnis);

}

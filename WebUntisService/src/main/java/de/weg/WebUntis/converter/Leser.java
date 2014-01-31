package de.weg.WebUntis.converter;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.weg.WebUntis.converter.schueler.SpaltenToken;

public interface Leser {

	public List<?> loadData(Properties einstellungen);
	public Map<SpaltenToken, Spalte> getMapping();


}

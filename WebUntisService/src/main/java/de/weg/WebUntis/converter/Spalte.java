package de.weg.WebUntis.converter;

public class Spalte {

	private final String spaltenName;
	private final int spaltenNummer;

	public Spalte(String spaltenName, int spaltenNummer) {
		this.spaltenName = spaltenName;
		this.spaltenNummer = spaltenNummer;
	}

	public String getSpaltenName() {
		return spaltenName;
	}

	public int getSpaltenNummer() {
		return spaltenNummer;
	}

}

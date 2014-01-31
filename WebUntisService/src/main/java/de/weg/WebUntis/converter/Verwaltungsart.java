package de.weg.WebUntis.converter;

import de.weg.WebUntis.converter.schueler.LeserSchuelerXLSSPlan;
import de.weg.WebUntis.converter.schueler.LeserSchuelerXLSSVP;

public enum Verwaltungsart {

	SVP(LeserSchuelerXLSSVP.class.getClass()),
	SPlan(LeserSchuelerXLSSPlan.class.getClass());

	private final Class<?> leser;

	private Verwaltungsart(Class<?> leser) {
		this.leser = leser;
	}

	public Class<?> getLeser() {
		return leser;
	}
}
package de.weg.WebUntis.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {

	/**
	 * Ein String, der den Usernamen nach der Erzeugung eines Objektes<br>
	 * dieser Klasse enthalten wird.
	 */
	private final String user;

	/**
	 * Ein String, der das Passwort nach der Erzeugung eines Objektes<br>
	 * dieser Klasse enthalten wird.
	 */
	private final String password;

	/**
	 * Der Konstruktor erzeugt ein MailAuthenticator Objekt<br>
	 * aus den beiden Parametern user und passwort.
	 * 
	 * @param user
	 *            String, der Username fuer den Mailaccount.
	 * @param password
	 *            String, das Passwort fuer den Mailaccount.
	 */
	public MailAuthenticator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	/**
	 * Diese Methode gibt ein neues PasswortAuthentication Objekt zurueck.
	 * 
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.user, this.password);
	}
}

package de.weg.WebUntis.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

public class MailCheck {

	// singletons
	static Session session = null;
	static String mailText = null;
	static Properties properties = null;

	public static String getDefaultText() {

		if (mailText == null) {
			StringBuffer mailB = new StringBuffer();
			mailB.append("Lieber WebUntis Benutzer,\n\n");
			mailB.append("Das WebUntisTeam vom WEG prüft, ob EMails an die von Ihnen gegebene Adresse versendet werden können.\n");
			mailB.append("An diese Adresse wird WebUntis gegebenenfalls ein EMail zum Zurücksetzten des Kennworts versenden, falls Sie dies über WebUntis anfordern.\n\n");
			mailB.append("Viele Grüße vom WebUntis Team am WEG.\n");
			mailText = mailB.toString();
		}

		return mailText;

	}

	public static Session getDefaultSession() {

		if (session == null) {
			Authenticator auth = new MailAuthenticator(
					getDefaultProperties().getProperty("mail.user"),
					getDefaultProperties().getProperty("mail.kennung"));
			session = Session.getInstance(getDefaultProperties(), auth);
		}

		return session;

	}

	protected static Properties getDefaultProperties() {
		if (properties == null) {
			properties = new Properties();

			// set smtp and imap to be our default
			// transport and store protocols, respectively
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.store.protocol", "imap");
			// Authentifizierung aktivieren
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.imap.auth", "true");
			// SSLv2
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.imap.ssl.enable", "true");
			properties.put("mail.smtp.socketFactory.port", "465");
			properties.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			// oder --- SSLv3/TLSv1
			// properties.put("mail.smtp.starttls.enable", "true");
			//
			properties
					.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
			properties.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");
			properties.put("mail.smtp.host", "mbox1.belwue.de");
			properties.put("mail.imap.host", "mbox1.belwue.de");
			String strFrom = "webuntis@weg-freiburg.de";
			properties.put("mail.from", strFrom);
			properties.put("mail.user", strFrom);
			properties.put("mail.bounced.head", "webuntis");
			properties.put("mail.bounced.tail", "@weg-freiburg.de");
			properties.put("mail.kennung", "LickRuett");
			properties.put("mail.debug", Boolean.TRUE.toString());
		}

		return properties;
	}

	public static void checkSendEMail(String toStr) {

		Message message = new MimeMessage(getDefaultSession());
		String folderName = null;
		try {

			message.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
			message.setHeader("Content-Transfer-Encoding", "quoted-printable");
			message.setSentDate(new Date());

			Address[] addresses = InternetAddress.parse(toStr);
			message.setRecipients(Message.RecipientType.TO, addresses);

			message.setSubject("WEG:WebUntis:EMailCheck:" + toStr);

			message.setContent(getDefaultText(), "text/plain; charset=utf-8");
			message.saveChanges();

			Transport.send(message);

			// TODO copy to send folder
			folderName = "EMailCheckSend";

		} catch (MessagingException mex) {
			System.out.println("\n--Exception handling in "
					+ MailCheck.class.toString());
			handleEMailError(mex);

			// TODO copy to error folder
			folderName = "EMailCheckError";

		} finally {
			try {
				copyToCheckEMailSend(message, folderName);
			} catch (MessagingException e) {
				System.out
						.println("\n--Exception handling for copying message "
								+ MailCheck.class.toString());
				handleEMailError(e);
			}
		}
	}

	/**
	 * Save a copy of the message
	 * 
	 * @throws MessagingException
	 */
	private static void copyToCheckEMailSend(Message message, String folderName)
			throws MessagingException {

		// Get a Store object
		Store store = getDefaultSession().getStore("imap");
		store.connect(getDefaultProperties().getProperty("mail.user"),
				getDefaultProperties().getProperty("mail.kennung"));

		// Get record Folder. Create if it does not exist.
		Folder folder = store.getFolder(folderName);
		if (folder == null) {
			System.err.println("Can't get record folder with name "
					+ folderName);
			System.exit(1);
		}
		if (!folder.exists()) {
			folder.create(Folder.HOLDS_MESSAGES);
		}

		Message[] msgs = new Message[1];
		msgs[0] = message;
		folder.appendMessages(msgs);

		System.out.println("Mail was recorded successfully to " + folderName);
	}

	/**
	 * @param mex
	 */
	private static void handleEMailError(MessagingException mex) {
		mex.printStackTrace();
		System.out.println();
		Exception ex = mex;
		do {
			if (ex instanceof SendFailedException) {
				SendFailedException sfex = (SendFailedException) ex;
				Address[] invalid = sfex.getInvalidAddresses();
				if (invalid != null) {
					System.out.println("    ** Invalid Addresses");
					for (int i = 0; i < invalid.length; i++)
						System.out.println("         " + invalid[i]);
				}
				Address[] validUnsent = sfex.getValidUnsentAddresses();
				if (validUnsent != null) {
					System.out.println("    ** ValidUnsent Addresses");
					for (int i = 0; i < validUnsent.length; i++)
						System.out.println("         " + validUnsent[i]);
				}
				Address[] validSent = sfex.getValidSentAddresses();
				if (validSent != null) {
					System.out.println("    ** ValidSent Addresses");
					for (int i = 0; i < validSent.length; i++)
						System.out.println("         " + validSent[i]);
				}
			}
			System.out.println();
			if (ex instanceof MessagingException)
				ex = ((MessagingException) ex).getNextException();
			else
				ex = null;
		} while (ex != null);
	}

	public static void checkBouncedEMail() {

		// Get a Store object
		Folder folder = null;
		Store store = null;
		try {
			store = getDefaultSession().getStore("imap");
			store.connect(getDefaultProperties().getProperty("mail.user"),
					getDefaultProperties().getProperty("mail.kennung"));
			// Get record Folder. Create if it does not exist.
			String inbox = "INBOX";
			folder = store.getFolder(inbox);
			if (folder == null) {
				System.err
						.println("Can't get record folder with name " + inbox);
				System.exit(1);
			}
			if (!folder.exists()) {
				folder.create(Folder.HOLDS_MESSAGES);
			}
			folder.open(Folder.READ_WRITE);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SearchTerm term = null;
		String subjectToken = "Returned mail:";
		term = new SubjectTerm(subjectToken);
		try {

			Message[] msgs = folder.search(term);
			System.out.println("FOUND " + msgs.length + " MESSAGES");
			for (Message message : msgs) {
				copyToCheckEMailSend(message, "EMailCheckBounced");
				folder.setFlags(new Message[] { message }, new Flags(
						Flags.Flag.DELETED), true);
				System.out.println("Moved " + message);
			}

			// Dump out the Flags of the moved messages, to insure that
			// all got deleted
			for (int i = 0; i < msgs.length; i++) {
				if (!msgs[i].isSet(Flags.Flag.DELETED))
					System.out.println("Message # " + msgs[i] + " not deleted");
			}

		} catch (MessagingException e) {
			System.out
					.println("\n--Exception handling for copying bounced message "
							+ MailCheck.class.toString());
			handleEMailError(e);
		}

		finally {
			try {
				if (null != folder) {
					folder.close(true);// remove deleted messages
				}
				if (null != store) {
					store.close();
				}

			} catch (MessagingException e) {
				System.out.println("\n--Exception handling closing store. "
						+ MailCheck.class.toString());
				handleEMailError(e);
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// MailCheck.checkSendEMail("dirk.ruettgers@hotmail.de");
		// MailCheck.checkSendEMail("murksmurksmurksmursk@hotmail.de");
		MailCheck.checkBouncedEMail();
	}

}

package ma.cimr.agmbackend.email;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendEmail(String to, String firstName, String subject, String temporaryPassword,
			EmailTemplateName emailTemplateName)
			throws MessagingException;
}

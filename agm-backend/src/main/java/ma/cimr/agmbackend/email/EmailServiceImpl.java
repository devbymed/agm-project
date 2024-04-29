package ma.cimr.agmbackend.email;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	@Async
	public void sendEmail(String to, String subject, String firstName, String temporaryPassword,
			EmailTemplateName emailTemplateName)
			throws MessagingException {

		String templateName = (emailTemplateName == null) ? "new_user" : emailTemplateName.getName();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED, UTF_8.name());

		Map<String, Object> properties = new HashMap<>();
		properties.put("firstName", firstName);
		properties.put("temporaryPassword", temporaryPassword);

		Context context = new Context();
		context.setVariables(properties);

		helper.setFrom("info@cimr.ma");
		helper.setTo(to);
		helper.setSubject(subject);

		String template = templateEngine.process(templateName, context);
		helper.setText(template, true);
		mailSender.send(message);
	}
}

package project.source.services.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
            String to,
            String username,
            String emailTemplate,
            String confirmationUrl,
            String verifyCode,
            String subject
    ) throws MessagingException {
        String templateName = (emailTemplate != null) ? emailTemplate : "confirm-email";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", verifyCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("phamduykhanh271098@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        log.info("Email template: {}", template);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }
}

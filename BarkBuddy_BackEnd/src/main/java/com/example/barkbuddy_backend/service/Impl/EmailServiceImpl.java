package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAdoptionRequestEmail(String ownerEmail, Long petId, String message, String adopterEmail, String dogName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("barkbuddyinf@gmail.com"); // must match configured username
            helper.setTo(ownerEmail);
            helper.setSubject("💛 New Adoption Request for Pet #" + petId);

            String acceptUrl = "http://localhost:8080/api/adoption/respond?requestId="  + "&action=ACCEPT";
            String rejectUrl = "http://localhost:8080/api/adoption/respond?requestId="  + "&action=REJECT";

            String body = "<div style='font-family:Arial, sans-serif; color:#333;'>"
                    + "<h2 style='color:#1E90FF;'>You have a new adoption request!</h2>"
                    + "<p>Your pet <strong>" + escapeHtml(dogName) + "</strong> (ID: " + petId + ") has received an adoption request.</p>"
                    + "<p><strong>Message from adopter (" + escapeHtml(adopterEmail) + "):</strong></p>"
                    + "<div style='padding:12px; border-left:4px solid #1E90FF; background:#f8f9ff;'>"
                    + "<p>" + (message == null || message.isBlank() ? "<em>(no message)</em>" : escapeHtml(message)) + "</p>"
                    + "</div>"
                    + "<p>You can contact the adopter directly at: <strong>" + escapeHtml(adopterEmail) + "</strong></p>"
                    + "<p style='margin-top:12px'>Respond to this request:</p>"
                    + "<div style='margin-top:12px;'>"
                    + "<a href='" + acceptUrl + "' style='text-decoration:none; margin-right:10px;'>"
                    + "<button style='background-color:#1cc88a; color:#fff; border:none; padding:10px 20px; border-radius:5px; cursor:pointer;'>Accept</button>"
                    + "</a>"
                    + "<a href='" + rejectUrl + "' style='text-decoration:none;'>"
                    + "<button style='background-color:#e74a3b; color:#fff; border:none; padding:10px 20px; border-radius:5px; cursor:pointer;'>Reject</button>"
                    + "</a>"
                    + "</div>"
                    + "<p style='margin-top:12px'>Thank you,<br/><strong>MeowMate Team</strong></p>"
                    + "</div>";

            helper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // log and rethrow as runtime to be handled by caller or global exception handler
            throw new RuntimeException("Failed to send adoption email: " + e.getMessage(), e);
        }
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;");
    }
}

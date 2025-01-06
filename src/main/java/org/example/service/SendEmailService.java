package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.example.dto.JobApplyDto;
import org.example.dto.SendEmailDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
@AllArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(SendEmailDto dto) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        /*message.setFrom(new InternetAddress("larrensquare@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, "services@larrensquare.com");
        message.setSubject(dto.getSubject());

        String htmlContent = "<p>"+ dto.getMessage() +"</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);*/

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set the sender's email address
        helper.setFrom(new InternetAddress("larrensquare@gmail.com"));

        // Set multiple recipients (you can use TO, CC, or BCC)
        helper.setTo(new String[]{"services@larrensquare.com"});

        // Set the email subject
        helper.setSubject(dto.getSubject());

        // Create the HTML content
        String htmlContent = "<p><strong>Full Name:</strong> " + dto.getFullName() + "</p>" + "<p><strong>From Email:</strong> " + dto.getFromEmail() + "</p>" + "<p>" + dto.getMessage() + "</p>";

        // Set the content and mark it as HTML
        helper.setText(htmlContent, true);

        // Send the email
        mailSender.send(message);

    }

    public String sendEmailWithAttachment(JobApplyDto request) throws MessagingException {


        /*ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JobApplyDto request;
        try {
            request = mapper.readValue(jobApplyDtoStr, JobApplyDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/

        // Decode the Base64 PDF file
        byte[] pdfBytes = Base64.getDecoder().decode(request.getPdfBase64());

        // Create MIME message
        MimeMessage message = mailSender.createMimeMessage();

        // Enable multipart mode
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set the sender's email address
        helper.setFrom(new InternetAddress("larrensquare@gmail.com"));

        // Set recipient, subject, and body
        helper.setTo(new String[]{"services@larrensquare.com"});
        helper.setSubject("Job Application Details For " + request.getJobTitle());

        // Build the HTML body with the provided fields
        String htmlBody = "<html>" +
                "<body>" +
                "<h3>Job Application Details</h3>" +
                "<p><strong>Job Title:</strong> " + request.getJobTitle() + "</p>" +
                "<p><strong>Name:</strong> " + request.getName() + "</p>" +
                "<p><strong>Email:</strong> " + request.getEmail() + "</p>" +
                "<p><strong>Phone:</strong> " + request.getPhone() + "</p>" +
                "<p><strong>Address:</strong> " + request.getAddress() + "</p>" +
                "<p><strong>LinkedIn:</strong> " + request.getLinkedin() + "</p>" +
                "<p><strong>Experience:</strong> " + request.getExperience() + "</p>" +
                "</body>" +
                "</html>";

        // Set the body as HTML
        helper.setText(htmlBody, true);

        /*// Convert MultipartFile to ByteArrayResource and attach it
        if (!multipartFile.isEmpty()) {
            ByteArrayResource attachment = null;
            try {
                attachment = new ByteArrayResource(multipartFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            helper.addAttachment(multipartFile.getOriginalFilename(), attachment);
        }*/

        // Create an InputStreamSource for the attachment
        InputStreamSource pdfSource = new ByteArrayResource(pdfBytes);

        // Add the PDF as an attachment
        helper.addAttachment(request.getFileName()+".pdf", pdfSource);



        // Send the email
        mailSender.send(message);

        return "Thank you for taking the time to apply for the "+request.getJobTitle()+ " position";

    }


}

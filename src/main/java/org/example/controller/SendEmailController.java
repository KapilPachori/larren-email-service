package org.example.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.JobApplyDto;
import org.example.dto.SendEmailDto;
import org.example.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
@Slf4j
public class SendEmailController {


    private SendEmailService sendEmailService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> sendMail(HttpServletRequest request, @RequestBody SendEmailDto sendEmailDto) throws MessagingException {
        String origin = request.getHeader("Origin");
        System.out.println("origin request " + request.getHeader("Origin"));
        sendEmailService.sendEmail(sendEmailDto);
        return new ResponseEntity<>("Mail Sent Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/applyJob", produces = "application/json", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> sendMailWithAttachment(HttpServletRequest request, @RequestBody JobApplyDto jobApplyDto) throws MessagingException {
        log.info("Received request: " + jobApplyDto);
        String origin = request.getHeader("Origin");
        System.out.println("origin request " + request.getHeader("Origin"));
        String message = sendEmailService.sendEmailWithAttachment(jobApplyDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}

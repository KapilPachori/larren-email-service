package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDto {

    private String subject;
    private String fullName;
    private String fromEmail;
    private String message;
    private String serviceFlag;

}

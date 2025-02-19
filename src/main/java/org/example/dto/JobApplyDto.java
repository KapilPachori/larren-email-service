package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplyDto {

    private String jobTitle;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String linkedin;
    private String experience;
    private byte[] fileContent;
    private String fileName;
    private String pdfBase64;


}

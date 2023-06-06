package com.bookitaka.NodeulProject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @NotBlank @Email
    private String requestEmail;

    @NotBlank
    private String requestName;

    private String requestPhone;
    private String requestBookisbn;
    @NotBlank
    private String requestBooktitle;
    private String requestBookauthor;
    private String requestBookpublisher;
    private String requestContent;
    private String requestHopedate;

}

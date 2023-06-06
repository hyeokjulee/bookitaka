package com.bookitaka.NodeulProject.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RequestDto {

    @NotBlank @Email
    private String requestEmail;

    @NotBlank
    private String requestName;

    private String requestPhone;
    private String requestBookisbn;
    @NotBlank
    private String requestBooktitle;
    private String requestBookwriter;
    private String requestBookpublisher;
    private String requestContent;

}

package com.bookitaka.NodeulProject.member.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerController {

  @Bean
  public ErrorAttributes errorAttributes() {
    // Hide exception field in the return object
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
      }
    };
  }

  @ExceptionHandler(CustomException.class)
  public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
    log.info("================handleCustomException - CustomException");
    res.sendError(ex.getHttpStatus().value(), ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
    log.info("================handleAccessDeniedException - AccessDeniedException");
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<List<ObjectError>> handleBindException(BindException ex) {
    log.info("================handleBindException - BindException");
    // 바인딩 예외 처리 로직을 구현합니다.
    BindingResult bindingResult = ex.getBindingResult();
    List<ObjectError> errors = bindingResult.getAllErrors();
    // 에러 처리 로직을 구현합니다.
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ObjectError>> handleValidationException(MethodArgumentNotValidException ex) {
    log.info("================handleValidationException - MethodArgumentNotValidException");
    // 바인딩 예외 처리 로직을 구현합니다.
    BindingResult bindingResult = ex.getBindingResult();
    List<ObjectError> errors = bindingResult.getAllErrors();
    // 에러 처리 로직을 구현합니다.
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(Exception.class)
  public void handleException(HttpServletResponse res) throws IOException {
    log.info("================handleException - Exception");
    res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
  }

}

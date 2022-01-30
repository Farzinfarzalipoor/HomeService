package ir.maktab.homeservice.advice;

import ir.maktab.homeservice.controller.dto.Error;
import ir.maktab.homeservice.controller.dto.ResponseTemplate;
import ir.maktab.homeservice.services.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = {ServiceNotFoundException.class, SubServiceNotFoundException.class,
            CustomerNotFoundException.class, ExpertNotFoundException.class, UserNotFoundException.class,
            FileNotFoundException.class, OfferNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<ResponseTemplate> notFound() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("resource not fount")
                .errors(Arrays.asList(Error.builder().code(404).message("not found.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = IllegalParameterException.class)
    public void illegalParameter() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("parameters are illegal.")
                .errors(Arrays.asList(Error.builder().code(400).message("parameters are illegal.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidBeginRequestException.class)
    public void invalidBeginRequest() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("cannot begin request.")
                .errors(Arrays.asList(Error.builder().code(400).message("cannot begin request.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidFinishOrderException.class)
    public void invalidFinishRequest() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("cannot finish request.")
                .errors(Arrays.asList(Error.builder().code(400).message("cannot finish request.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidFileFormatException.class)
    public void invalidFile() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("file format is not supported.")
                .errors(Arrays.asList(Error.builder().code(406).message("not supported.").build()))
                .build();
        ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(value = InvalidOfferRegisterException.class)
    public void invalidOfferRegister() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("offer has already been selected.")
                .errors(Arrays.asList(Error.builder().code(400).message("offer has already been selected.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidOfferSelectionException.class)
    public void invalidOfferSelection() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("cannot select offer for this request.")
                .errors(Arrays.asList(Error.builder().code(401).message("not authorized.").build()))
                .build();
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = InvalidOrderEvaluationException.class)
    public void invalidEvaluation() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("cannot evaluate this request yet.")
                .errors(Arrays.asList(Error.builder().code(400).message("request is not done.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidOrderStatusException.class)
    public void invalidStatus() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("request is not done yet.")
                .errors(Arrays.asList(Error.builder().code(400).message("request is not done.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = InvalidExpertOfferException.class)
    public void invalidSpecialist() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("cannot add offers for this request.")
                .errors(Arrays.asList(Error.builder().code(401).message("don't have the assistance.").build()))
                .build();
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = LowBasePriceException.class)
    public void lowerThanBasePrice() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("price is lower than the base price.")
                .errors(Arrays.asList(Error.builder().code(400).message("price is low.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = OrderSettlementException.class)
    public void notEnoughCredit() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("you don't have enough credit.")
                .errors(Arrays.asList(Error.builder().code(400).message("credit is low.").build()))
                .build();
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    public void unauthenticated() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("you are not authenticated.")
                .errors(Arrays.asList(Error.builder().code(401).message("unauthenticated.").build()))
                .build();
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = UnauthorizedCustomerException.class)
    public void unauthorized() {
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("you are not authorized.")
                .errors(Arrays.asList(Error.builder().code(401).message("unauthorized.").build()))
                .build();
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

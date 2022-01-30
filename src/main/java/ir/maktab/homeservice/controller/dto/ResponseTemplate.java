package ir.maktab.homeservice.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseTemplate <T> {
    int code;
    String message;
    List<Error> errors;
    T data;
}

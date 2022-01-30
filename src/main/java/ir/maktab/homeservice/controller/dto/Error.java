package ir.maktab.homeservice.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {
    int code;
    String message;
}

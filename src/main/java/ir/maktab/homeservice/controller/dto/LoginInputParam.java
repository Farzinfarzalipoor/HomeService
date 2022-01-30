package ir.maktab.homeservice.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInputParam {
    @NotNull(message = "email should not be null")
    String email;

    @NotNull(message = "password should not be null")
    String password;
}

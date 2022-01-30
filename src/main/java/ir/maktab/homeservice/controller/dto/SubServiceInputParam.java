package ir.maktab.homeservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
public class SubServiceInputParam {
    @NotNull
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;
}

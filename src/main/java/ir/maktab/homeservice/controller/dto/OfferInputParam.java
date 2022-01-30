package ir.maktab.homeservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class OfferInputParam {
    @NotNull
    private Long specialistId;

    @NotNull
    private Double price;

    @NotNull
    private Double executionPeriod;

    @NotNull
    private Date beginning;
}

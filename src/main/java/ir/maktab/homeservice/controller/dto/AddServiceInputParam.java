package ir.maktab.homeservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
public class AddServiceInputParam {
    @NotNull
    private Long serviceId;
}

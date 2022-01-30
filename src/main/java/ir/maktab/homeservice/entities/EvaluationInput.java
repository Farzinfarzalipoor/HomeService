package ir.maktab.homeservice.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class EvaluationInput {
    Double points;

    String comment;
}

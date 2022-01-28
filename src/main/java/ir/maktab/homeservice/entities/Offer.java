package ir.maktab.homeservice.entities;

import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.users.Expert;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Expert expert;

    @NotNull
    private Date submitDate;

    @NotNull
    private Double price;

    @NotNull
    private Double executionPeriod;

    @NotNull
    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(expert, offer.expert) && Objects.equals(submitDate, offer.submitDate) && Objects.equals(price, offer.price) && Objects.equals(executionPeriod, offer.executionPeriod) && Objects.equals(startDate, offer.startDate) && Objects.equals(order, offer.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expert, submitDate, price, executionPeriod, startDate, order);
    }
}

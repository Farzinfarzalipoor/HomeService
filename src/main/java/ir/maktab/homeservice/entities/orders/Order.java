package ir.maktab.homeservice.entities.orders;

import ir.maktab.homeservice.entities.Offer;
import ir.maktab.homeservice.entities.services.SubService;
import ir.maktab.homeservice.entities.users.Customer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "subservice_id")
    private SubService subService;

    @NotNull
    private Long customerId;

    @NotNull
    private Long subServiceId;

    @NotNull
    private Double offeredPrice;

    @NotNull
    private String description;

    @NotNull
    private Date registerDate;

    @NotNull
    private Date executionDate;

    @NotNull
    private String address;

    @NotNull
    private OrderStatus status;

    @OneToMany
    @JoinColumn(name = "order_id")
    @Builder.Default
    private Set<Offer> offers = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer selectedOffer;

    private Double points;

    private String comment;

    public void addOffer(Offer offer) {
        offers.add(offer);
        offer.setOrder(this);
    }

    public void removeOffer(Offer offer) {
        offers.remove(offer);
        offer.setOrder(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(subService, order.subService) && Objects.equals(offeredPrice, order.offeredPrice) && Objects.equals(description, order.description) && Objects.equals(registerDate, order.registerDate) && Objects.equals(executionDate, order.executionDate) && Objects.equals(address, order.address) && status == order.status && Objects.equals(offers, order.offers) && Objects.equals(selectedOffer, order.selectedOffer) && Objects.equals(points, order.points) && Objects.equals(comment, order.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, subService, offeredPrice, description, registerDate, executionDate, address, status, offers, selectedOffer, points, comment);
    }
}
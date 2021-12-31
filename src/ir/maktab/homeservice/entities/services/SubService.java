package ir.maktab.homeservice.entities.services;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "customer_id"})})
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Service service;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubService that = (SubService) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(basePrice, that.basePrice) && Objects.equals(description, that.description) && Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, basePrice, description, service);
    }
}

package ir.maktab.homeservice.entities.services;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "service_id")
    @Builder.Default
    private Set<SubService> subService = new HashSet<>();

    public void addSubService(SubService subService) {
        this.subService.add(subService);
        subService.setService(this);
    }

    public void removeSubService(SubService subService) {
        this.subService.remove(subService);
        subService.setService(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id) && Objects.equals(title, service.title) && Objects.equals(subService, service.subService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subService);
    }
}
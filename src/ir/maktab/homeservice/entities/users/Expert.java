package ir.maktab.homeservice.entities.users;

import ir.maktab.homeservice.entities.Comment;
import ir.maktab.homeservice.entities.Offer;
import ir.maktab.homeservice.entities.services.Service;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Blob;
import java.util.*;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends User {

    @Lob
    private Blob photoURL;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "expert_service")
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "expert")
    @JoinColumn(name = "comment_id")
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }

    public void addComments(Comment comment){
        comments.add(comment);
    }

    public void removeComments(Comment comment){
        comments.remove(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expert expert = (Expert) o;
        return Objects.equals(photoURL, expert.photoURL) && Objects.equals(services, expert.services) && Objects.equals(comments, expert.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoURL, services, comments);
    }
}

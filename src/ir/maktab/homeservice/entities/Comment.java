package ir.maktab.homeservice.entities;

import ir.maktab.homeservice.entities.users.Expert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Expert expert;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(text, comment.text) && Objects.equals(expert, comment.expert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, expert);
    }
}

package com.github.guninigor75.social_media.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Long candidate;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public enum Status{
        CONSIDERATION,
        ACCEPTED;
//        REJECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invite invite = (Invite) o;
        return Objects.equals(id, invite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", candidate=" + candidate +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}

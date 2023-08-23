package com.github.guninigor75.social_media.entity.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String mediaType;

    @Column(nullable = false)
    private long fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Post post;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

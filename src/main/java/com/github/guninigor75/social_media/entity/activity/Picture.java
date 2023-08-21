package com.github.guninigor75.social_media.entity.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Фото родительский класс для Avatar и Picture
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Путь к файлу */
    @Column(nullable = false)
    private String filePath;

    /** mediaType файла*/
    @Column(nullable = false)
    private String mediaType;

    /*** Размер файла */
    @Column(nullable = false)
    private long fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
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

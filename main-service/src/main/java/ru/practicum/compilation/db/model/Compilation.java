package ru.practicum.compilation.db.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Data
@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    Boolean pinned;
    String title;

    public Compilation() {

    }
}

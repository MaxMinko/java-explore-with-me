package ru.practicum.compilation.db.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.persistence.*;



@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
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

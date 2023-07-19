package ru.practicum.event.db.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.db.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String text;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User author;
    LocalDateTime created;
    @Column(name = "is_Edited")
    Boolean isEdited;

    public Comment() {

    }


}

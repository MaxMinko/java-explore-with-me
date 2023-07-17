package ru.practicum.event.db.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.db.model.Category;
import ru.practicum.user.db.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String annotation;
    @ManyToOne()
    Category category;
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    Float lat;
    Float lon;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    String title;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @ManyToOne()
    User user;
    String state;

    public Event() {

    }
}

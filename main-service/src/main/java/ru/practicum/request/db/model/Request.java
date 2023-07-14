package ru.practicum.request.db.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;
    LocalDateTime created;
    int event;
    int requester;
    String status;
    public Request() {

    }
}

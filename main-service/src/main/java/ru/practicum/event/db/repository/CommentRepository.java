package ru.practicum.event.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.db.model.Comment;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByEventId(int id);

    void deleteByIdAndEventId(int commentId, int eventId);
}

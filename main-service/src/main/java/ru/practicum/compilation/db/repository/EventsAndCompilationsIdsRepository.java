package ru.practicum.compilation.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.db.model.EventsAndCompilationsIds;

import java.util.List;

public interface EventsAndCompilationsIdsRepository extends JpaRepository<EventsAndCompilationsIds, Integer> {
    void deleteByCompilationId(int compId);

    List<EventsAndCompilationsIds> findByCompilationIdIn(Iterable<Integer> compilationId);
}

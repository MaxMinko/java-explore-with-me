package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.PostStatic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface StatRepository extends JpaRepository<PostStatic, Integer> {

    @Query(value = "select  COUNT(id) " +
            "from stat " +
            "where uri=?3 " +
            "AND created between ?1 and ?2",
            nativeQuery = true)
    int findStatic(LocalDateTime start, LocalDateTime end, String uri);

    @Query(value = "select  COUNT(DISTINCT ip) " +
            "from stat " +
            "where uri=?3 " +
            "AND created between ?1 and ?2",
            nativeQuery = true)
    int findUniqStatic(LocalDateTime start, LocalDateTime end, String uri);
}

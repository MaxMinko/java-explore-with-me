package ru.practicum.compilation.db.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilation.db.model.Compilation;


import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into eventIdsForCompilation (compilation_id,event_id) " +
            "VALUES (?1, unnest(ARRAY[?2]) )", nativeQuery = true)
    void addEventsId(int compilationId, List<Integer> eventId);

    @Query(value = "select event_id from eventIdsForCompilation where compilation_id=?", nativeQuery = true)
    List<Integer> getEventsId(int compilationId);

    @Modifying
    @Query(value = "delete from eventIdsForCompilation where compilation_id=?", nativeQuery = true)
    void delete(int compilationId);


    @Query(value = "select * from compilations where pinned=?1", nativeQuery = true)
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    @Modifying
    @Query(value = "select event_id from eventidsforcompilation where  compilation_id=?", nativeQuery = true)
    List<Integer> findCompilationEvents(int compId);

    @Modifying
    @Query(value =
            "select compilation_id,event_id from compilations " +
             " INNER JOIN eventidsforcompilation e on compilations.id = e.compilation_id " +
              "  where compilation_id  = ANY(ARRAY[?])", nativeQuery = true)
    List<String> findCompilationsEvents(List<Integer> ids);

}

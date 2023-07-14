package ru.practicum.compilation.db.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilation.db.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into eventIdsForCompilation (compilation_id,event_id) values (?1,?2)"
            , nativeQuery = true)
    void addEventsId(int compilationId, int eventId);

    @Query(value = "select event_id from eventIdsForCompilation where compilation_id=?", nativeQuery = true)
    List<Integer> getEventsId(int compilationId);

    @Modifying
    @Query(value = "delete from eventIdsForCompilation where compilation_id=?", nativeQuery = true)
    void delete(int compilationId);


    @Query(value = "select * from compilations where pinned=?1", nativeQuery = true)
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    @Modifying
    @Query(value = "select event_id from eventidsforcompilation where compilation_id=?", nativeQuery = true)
    List<Integer> findCompilationEvents(int compId);


}

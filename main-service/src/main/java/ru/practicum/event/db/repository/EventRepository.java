package ru.practicum.event.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.db.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findByUserId(int userId,Pageable pageable);

    Event findByUserIdAndId(int userId,int id);

    @Query(value = "select e from Event e where e.user.id IN :users AND e.state IN :states AND " +
            "e.category.id IN :categories AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> getEvents(@Param("users")List<Integer> users,
                          @Param("states") List<String> states,
                          @Param("categories") List<Integer> categories,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd,
                           Pageable pageable);

    @Modifying
    @Query(value = "SELECT requests.id, requests.created," +
            "       requests.event, requests.requester, " +
            "       events.state " +
            "FROM requests " +
            "JOIN events  on requests.event = events.id  " +
            "WHERE  requests.event=?1",nativeQuery = true)
  List<String>  getRequest(int eventId);


    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE id=?1",nativeQuery = true)
    Event getEvent(int id);

    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE id=?2 AND user_id=?1",nativeQuery = true)
    Event getUserEvent(int userId, int eventId);






    @Query(value = "select e from Event as e " +
            "where (lower(e.annotation) like lower(concat('%',:text,'%')) or " +
            "lower(e.description) like lower(concat('%',:text,'%'))) AND " +
            "e.category.id =:categories AND e.paid = :paid AND " +
            "e.eventDate BETWEEN :rangeStart AND :rangeEnd AND e.state= 'PUBLISHED' " +
            "order by e.eventDate desc" )
    List<Event> getEventsWithFilterSortEventDate(@Param("text") String text,
                                                 @Param("categories") List<Integer> ids,
                                                 @Param("paid")Boolean paid,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                  Pageable pageable
                                                 );
    @Query(value = "select e from Event as e " +
            "where e.category.id =:categories AND e.state= 'PUBLISHED'" )
    List<Event> findByCategoryId(@Param("categories") List<Integer> categories,Pageable pageable);

    @Query(value = "select e from Event as e " +
            "where (lower(e.annotation) like lower(concat('%',:text,'%')) or " +
            "lower(e.description) like lower(concat('%',:text,'%'))) AND " +
            "e.category.id =:categories AND e.paid = :paid AND " +
            "e.state= 'PUBLISHED' AND e.eventDate BETWEEN :rangeStart AND :rangeEnd ")

    List<Event> getEventsWithFilterSortViews(String text,
                                             List<Integer> categories,
                                             Boolean paid,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Pageable pageable);


    @Query(value = "select e from Event as e " +
            "where (lower(e.annotation) like lower(concat('%',:text,'%')) or " +
            "lower(e.description) like lower(concat('%',:text,'%'))) AND " +
            "e.category.id =:categories AND e.paid = :paid AND " +
            "e.state= 'PUBLISHED' AND e.eventDate>=now()")
    List<Event> getEvents(String text,
                         List<Integer> categories,
                         Boolean paid,
                         Pageable pageable);




}




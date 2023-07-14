package ru.practicum.request.db.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.db.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query(value = "SELECT * " + "FROM requests " + "WHERE requester=?1 ", nativeQuery = true)
    List<Request> findByUserId(int userId);

    @Query(value = "SELECT * " + "FROM requests " + "WHERE requests.id=?2 AND requests.requester=?1 ",
            nativeQuery = true)
    Request getRequest(int userId, int requestId);

    @Query(value = "SELECT * " + "FROM requests " + "WHERE requests.event=?2 AND requests.requester=?1 ",
            nativeQuery = true)
    Request getRequestForAdd(int userId, int eventId);

    @Query(value = "SELECT COUNT(id) " + "FROM requests " + "WHERE event=?1 AND status LIKE 'CONFIRMED'",
            nativeQuery = true)
    Integer findRequestForOneEvent(int eventId);


    @Query(value = "select r from Request r where r.id IN :requestIds")
    List<Request> findRequests(@Param("requestIds") List<Integer> requestIds);

    @Modifying
    @Query(value = "select count (r.id) from  Request r where r.event IN :eventsId and r.status" + " LIKE 'CONFIRMED' group by r.id")
    List<Integer> findConfirmedRequestsForEvents(List<Integer> eventsId);
}

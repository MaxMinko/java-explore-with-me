package ru.practicum.user.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.db.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);

    @Query(value = "select u from User u where u.id IN :usersId")
    Page<User> findAllByIds(@Param("usersId") List<Integer> usersId, Pageable pageable);
}

package ru.practicum.category.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.db.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);
}

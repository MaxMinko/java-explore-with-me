package ru.practicum.category.service;

import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.category.web.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    List<CategoryDto> getAllCategory(int from, int size);

    CategoryDto getCategory(int catId);
}

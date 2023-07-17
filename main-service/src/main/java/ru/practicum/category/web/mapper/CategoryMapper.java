package ru.practicum.category.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.db.model.Category;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.category.web.dto.NewCategoryDto;

@UtilityClass
public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static Category categoryDtoToCategory(int catId, CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setId(catId);
        return category;
    }
}

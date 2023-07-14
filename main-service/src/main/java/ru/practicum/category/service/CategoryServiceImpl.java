package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.db.model.Category;
import ru.practicum.category.db.repository.CategoryRepository;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.category.web.dto.NewCategoryDto;
import ru.practicum.category.web.mapper.CategoryMapper;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryValidationException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper
                .NewCategoryDtoToCategory(newCategoryDto)));
    }

    @Transactional
    @Override
    public void deleteCategory(int catId) {
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findByName(categoryDto.getName());
        if (category != null && category.getName().equals(categoryDto.getName()) && category.getId() != catId) {
            throw new CategoryValidationException("Нельзя изменить название категории на уже существующее");
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper
                .CategoryDtoToCategory(catId, categoryDto)));
    }

    @Override
    public List<CategoryDto> getAllCategory(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).stream().map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(int catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена.")));
    }
}

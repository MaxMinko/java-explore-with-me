package ru.practicum.category.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.category.web.dto.NewCategoryDto;

@RestController
@RequestMapping(value = "/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CategoryDto> addCategory(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable("catId") int catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Validated @RequestBody CategoryDto categoryDto, @PathVariable("catId") int catId) {
        return categoryService.updateCategory(catId, categoryDto);
    }
}

package com.helloworld.dreamshopsbackend.service.category;

import com.helloworld.dreamshopsbackend.model.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoriesById(Long id);

}

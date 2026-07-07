package com.helloworld.dreamshopsbackend.service.category;

import com.helloworld.dreamshopsbackend.exception.category.ResourceAlreadyExistException;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Category;
import com.helloworld.dreamshopsbackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository repository;


    @Override
    public Category getCategoryById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found!")
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !repository.existsByName(c.getName())).map(repository :: save).orElseThrow(
                () -> new ResourceAlreadyExistException(category.getName() + " is already existed")
        );
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(
                C -> {
                    C.setName(category.getName());
                    C.setProducts(category.getProducts());
                    return repository.save(C);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoriesById(Long id) {
        Category category = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        repository.deleteById(id);

    }
}

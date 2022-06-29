package io.petbook.pbboard.infrastructure.board.category;


import io.petbook.pbboard.common.exception.EntityNotFoundException;
import io.petbook.pbboard.domain.board.category.Category;
import io.petbook.pbboard.domain.board.category.CategoryReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryReaderImpl implements CategoryReader {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getList() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    public List<Category> getListIsDeleted() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAllIsDeleted().forEach(categories::add);
        return categories;
    }

    @Override
    public Category getEntity(String token) {
        return categoryRepository
                .findByToken(token)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Category getDeleteEntity(String token) {
        return categoryRepository
                .findByTokenIsDeleted(token)
                .orElseThrow(EntityNotFoundException::new);
    }
}

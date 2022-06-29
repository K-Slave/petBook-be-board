package io.petbook.pbboard.infrastructure.board.category;

import io.petbook.pbboard.domain.board.category.Category;
import io.petbook.pbboard.domain.board.category.CategoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryStoreImpl implements CategoryStore {
    private final CategoryRepository categoryRepository;

    @Override
    public Category store(Category category) {
        return categoryRepository.save(category);
    }
}

package io.petbook.pbboard.domain.board.category;

import java.util.List;

public interface CategoryService {
    List<CategoryInfo.Main> getCategoryInfoList();
    List<CategoryInfo.Main> getCategoryInfoIsDeleted();
    CategoryInfo.Main getCategoryInfo(String token);
    CategoryInfo.Main createCategoryInfo(CategoryCommand.Main command);
    CategoryInfo.Main modifyCategoryInfo(CategoryCommand.Modifier command);
    CategoryInfo.DeleteProcStatus deleteCategoryInfo(String token);
    CategoryInfo.Main restoreCategoryInfo(String token);
    CategoryInfo.Main enableCategoryInfo(String token);
    CategoryInfo.Main disableCategoryInfo(String token);
}

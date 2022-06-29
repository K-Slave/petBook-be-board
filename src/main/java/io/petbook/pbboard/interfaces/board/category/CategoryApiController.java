package io.petbook.pbboard.interfaces.board.category;

import io.petbook.pbboard.common.response.CommonResponse;
import io.petbook.pbboard.domain.board.category.CategoryCommand;
import io.petbook.pbboard.domain.board.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// TODO: Command 객체 -> Request 객체 재구성 (다음 주 부터 진행 예정)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping
    public CommonResponse getCategories() {
        return CommonResponse.success(categoryService.getCategoryInfoList());
    }

    @GetMapping("{token}")
    public CommonResponse getCategory(@PathVariable String token) {
        return CommonResponse.success(categoryService.getCategoryInfo(token));
    }

    @GetMapping("/deleted")
    public CommonResponse getCategoriesIsDeleted() {
        return CommonResponse.success(categoryService.getCategoryInfoIsDeleted());
    }

    @PostMapping
    public CommonResponse createCategory(@RequestBody CategoryCommand.Main request) {
        return CommonResponse.success(categoryService.createCategoryInfo(request));
    }

    @PutMapping
    public CommonResponse modifyCategory(@RequestBody CategoryCommand.Modifier request) {
        return CommonResponse.success(categoryService.modifyCategoryInfo(request));
    }

    @PutMapping("restore/{token}")
    public CommonResponse restoreCategory(@PathVariable String token) {
        return CommonResponse.success(categoryService.restoreCategoryInfo(token));
    }

    @PutMapping("enable/{token}")
    public CommonResponse enableCategory(@PathVariable String token) {
        return CommonResponse.success(categoryService.enableCategoryInfo(token));
    }

    @PutMapping("disable/{token}")
    public CommonResponse disableCategory(@PathVariable String token) {
        return CommonResponse.success(categoryService.disableCategoryInfo(token));
    }

    @DeleteMapping("{token}")
    public CommonResponse deleteCategory(@PathVariable String token) {
        return CommonResponse.success(categoryService.deleteCategoryInfo(token));
    }
}

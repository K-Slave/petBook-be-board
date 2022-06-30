package io.petbook.pbboard.interfaces.board.category;

import io.petbook.pbboard.common.response.CommonResponse;
import io.petbook.pbboard.domain.board.category.CategoryCommand;
import io.petbook.pbboard.domain.board.category.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "카테고리 목록 조회", notes = "카테고리 목록을 가져옵니다.")
    public CommonResponse getCategories() {
        return CommonResponse.success(categoryService.getCategoryInfoList());
    }

    @GetMapping("/deleted")
    @ApiOperation(value = "삭제된 카테고리 목록 조회", notes = "삭제된 카테고리 목록을 가져옵니다.")
    public CommonResponse getCategoriesIsDeleted() {
        return CommonResponse.success(categoryService.getCategoryInfoIsDeleted());
    }

    @GetMapping("{token}")
    @ApiOperation(value = "카테고리 상세 정보 조회", notes = "카테고리 내 게시물 및 키워드 정보 등을 가져옵니다.")
    public CommonResponse getCategory (
        @PathVariable
        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345") String token
    ) {
        return CommonResponse.success(categoryService.getCategoryInfo(token));
    }

    @PostMapping
    @ApiOperation(value = "카테고리 생성", notes = "관리자가 새로운 카테고리 정보를 생성합니다.")
    public CommonResponse createCategory(@RequestBody CategoryCommand.Main request) {
        return CommonResponse.success(categoryService.createCategoryInfo(request));
    }

    @PutMapping
    @ApiOperation(value = "카테고리 수정", notes = "관리자가 등록한 카테고리 정보를 수정합니다.")
    public CommonResponse modifyCategory(@RequestBody CategoryCommand.Modifier request) {
        return CommonResponse.success(categoryService.modifyCategoryInfo(request));
    }

    @PutMapping("restore/{token}")
    @ApiOperation(value = "카테고리 복구", notes = "이미 삭제된 카테고리 정보에 대해 복구합니다.")
    public CommonResponse restoreCategory (
        @PathVariable
        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345") String token
    ) {
        return CommonResponse.success(categoryService.restoreCategoryInfo(token));
    }

    @PutMapping("enable/{token}")
    @ApiOperation(value = "카테고리 공개 처리", notes = "관리자가 등록한 카테고리 정보를 공개 처리합니다.")
    public CommonResponse enableCategory (
        @PathVariable
        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345") String token
    ) {
        return CommonResponse.success(categoryService.enableCategoryInfo(token));
    }

    @PutMapping("disable/{token}")
    @ApiOperation(value = "카테고리 비공개 처리", notes = "관리자가 등록한 카테고리 정보를 비공개 처리합니다.")
    public CommonResponse disableCategory (
        @PathVariable
        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345") String token
    ) {
        return CommonResponse.success(categoryService.disableCategoryInfo(token));
    }

    @DeleteMapping("{token}")
    @ApiOperation(value = "카테고리 삭제", notes = "관리자가 등록한 카테고리 정보를 삭제합니다.")
    public CommonResponse deleteCategory (
        @PathVariable
        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345") String token
    ) {
        return CommonResponse.success(categoryService.deleteCategoryInfo(token));
    }
}

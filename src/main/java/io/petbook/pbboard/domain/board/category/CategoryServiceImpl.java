package io.petbook.pbboard.domain.board.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryReader categoryReader;
    private final CategoryStore categoryStore;

    /**
     * [Kang] 삭제 되지 않은 일반 카테고리 목록을 가져온다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfo.Main> getCategoryInfoList() {
        return categoryReader
                .getList()
                .stream()
                .map(CategoryInfo.Main::toInfo)
                .collect(Collectors.toList());
    }


    /**
     * [Kang] 카테고리 복구를 위한 이미 삭제된 카테고리 목록을 가져와 관리자가 조율할 수 있게끔 한다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfo.Main> getCategoryInfoIsDeleted() {
        return categoryReader
                .getListIsDeleted()
                .stream()
                .map(CategoryInfo.Main::toInfo)
                .collect(Collectors.toList());
    }


    /**
     * [Kang] 카테고리 단일 정보를 가져온다.
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryInfo.Main getCategoryInfo(String token) {
        Category category = categoryReader.getEntity(token);
        return CategoryInfo.Main.toInfo(category);
    }

    /**
     * [Kang] 카테고리 정보를 새로 만든다.
     */
    @Override
    @Transactional
    public CategoryInfo.Main createCategoryInfo(CategoryCommand.Main command) {
        Category category = categoryStore.store(command.toEntity());
        return CategoryInfo.Main.toInfo(category);
    }

    /**
     * [Kang] 카테고리 정보를 수정한다.
     */
    @Override
    @Transactional // 이 어노테이션 안에서 데이터 변경이 들어가도, ~~~.save() 를 하지 않더라도 데이터가 갱신되게 된다.
    public CategoryInfo.Main modifyCategoryInfo(CategoryCommand.Modifier command) {
        Category category = categoryReader.getEntity(command.getToken());
        category.modifyByCommand(command);
        category.modified();
        return CategoryInfo.Main.toInfo(category);
    }

    /**
     * [Kang] 카테고리 정보를 삭제한다.
     */
    @Override
    @Transactional
    public CategoryInfo.DeleteProcStatus deleteCategoryInfo(String token) {
        Category category = categoryReader.getEntity(token);
        category.deleted();
        return CategoryInfo.DeleteProcStatus.builder().completed(true).build();
    }

    /**
     * [Kang] 카테고리 정보를 복구한다.
     */
    @Override
    @Transactional
    public CategoryInfo.Main restoreCategoryInfo(String token) {
        Category category = categoryReader.getDeleteEntity(token);
        category.restored();
        return CategoryInfo.Main.toInfo(category);
    }

    /**
     * [Kang] 카테고리 정보 공개 여부를 설정한다.
     */
    @Override
    @Transactional
    public CategoryInfo.Main enableCategoryInfo(String token) {
        Category category = categoryReader.getEntity(token);
        category.enable();
        return CategoryInfo.Main.toInfo(category);
    }

    /**
     * [Kang] 카테고리 정보 비공개 여부를 설정한다.
     * TODO: 비공개 처리일 경우, 게시글, 댓글 등등 이에 종속된 데이터들에 대해 모두 비공개 처리한다.
     */
    @Override
    @Transactional
    public CategoryInfo.Main disableCategoryInfo(String token) {
        Category category = categoryReader.getEntity(token);
        category.disable();
        return CategoryInfo.Main.toInfo(category);
    }
}

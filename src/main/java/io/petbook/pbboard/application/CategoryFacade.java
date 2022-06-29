package io.petbook.pbboard.application;


import io.petbook.pbboard.domain.board.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryFacade {
    // 퍼사드 클래스에는 서비스들을 융합해서 사용하는 경우에 작성하는 편이 좋다.
    // 예를 들어 카테고리 정보 중 일부가 비공개로 설정된 경우, 사용자에게 알림을 따로 주거나 하는 등에 사용하면 된다.

    // 그렇지만 목록, 정보 등 여러 서비스가 굳이 필요 없는 경우에는 외부에서 단일 서비스로 불러와 사용해도 상관은 없을 거 같다.
    private final CategoryService categoryService;

}

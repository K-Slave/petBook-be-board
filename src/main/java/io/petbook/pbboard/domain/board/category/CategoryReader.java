package io.petbook.pbboard.domain.board.category;

import java.util.List;

/**
 * [Kang] Category 데이터에 대한 조회성 작업
 */
public interface CategoryReader {
    List<Category> getList();
    List<Category> getListIsDeleted();
    Category getEntity(String token);
    Category getDeleteEntity(String token);
}

package io.petbook.pbboard.interfaces.board.article;

import io.petbook.pbboard.application.ArticleFacade;
import io.petbook.pbboard.common.response.CommonResponse;
import io.petbook.pbboard.domain.board.article.ArticleCommand;
import io.petbook.pbboard.domain.board.article.ArticleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// TODO: Command 객체 -> Request 객체 재구성 (다음 주 부터 진행 예정)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleApiController {
    // [Kang] 퍼사드 객체들은 MSA 를 사용할 때 정보를 가져오는 경우 등등에 사용하는 것이 적당하다.
    // [Kang] 예를 들어 웹 크롤링 서버에게 게시물 정보를 얻어와 추가하는 등등.
    // [Kang] 그러나 단순히 사용자가 게시물을 추가하는 경우 등에는 퍼사드 객체까지 사용은 안 해도 될 거 같다. Service 에서 종결 맺어도 솔직히 상관은 크게 없을 거 같다.
    private final ArticleFacade articleFacade;
    private final ArticleService articleService;

    @GetMapping
    @ApiOperation(value = "게시물 페이징네이션 목록 조회", notes = "게시물 정보를 페이징네이션 쿼리 스트링으로 가져옵니다.")
    public CommonResponse getArticlesBriefList(ArticleCommand.Paginate paginate) {
        return CommonResponse.success(articleFacade.loadBriefList(paginate));
    }

    @GetMapping("/deleted")
    @ApiOperation(value = "삭제된 게시물 목록 조회", notes = "삭제된 게시물 목록에 대해 가져옵니다.")
    public CommonResponse getArticlesIsDeleted() {
        return CommonResponse.success(articleService.getArticleInfoIsDeleted());
    }

    @GetMapping("{token}")
    @ApiOperation(value = "게시물 상세 정보 조회", notes = "게시물 내용, 댓글, 파일 (이미지 포함) 목록 등에 대해 가져옵니다.")
    public CommonResponse getArticlesDetailView (
        @PathVariable
        @ApiParam(value = "게시물 토큰", example = "atcl_abcde12345") String token
    ) {
        return CommonResponse.success(articleFacade.loadDetailView(token));
    }

    @PostMapping
    @ApiOperation(value = "게시물 생성", notes = "사용자가 새로운 게시물 정보를 생성합니다.")
    public CommonResponse createArticle(@RequestBody ArticleCommand.Main request) {
        return CommonResponse.success(articleService.createArticleInfo(request));
    }

    @PutMapping
    @ApiOperation(value = "게시물 수정", notes = "사용자가 등록한 게시물 정보를 수정합니다.")
    public CommonResponse modifyArticle(@RequestBody ArticleCommand.Modifier modifier) {
        return CommonResponse.success(articleService.modifyArticleInfo(modifier));
    }

    @PutMapping("restore/{token}")
    @ApiOperation(value = "게시물 복구", notes = "이미 삭제된 게시물 정보에 대해 복구합니다.")
    public CommonResponse restoreArticle (
        @PathVariable
        @ApiParam(value = "게시물 토큰", example = "atcl_abcde12345") String token
    ) {
        return CommonResponse.success(articleService.restoreArticleInfo(token));
    }

    @PutMapping("enable/{token}")
    @ApiOperation(value = "게시물 공개 처리", notes = "사용자가 등록한 게시물 정보를 공개 처리합니다.")
    public CommonResponse enableCategory (
        @PathVariable
        @ApiParam(value = "게시물 토큰", example = "atcl_abcde12345") String token
    ) {
        return CommonResponse.success(articleService.enableArticleInfo(token));
    }

    @PutMapping("disable/{token}")
    @ApiOperation(value = "게시물 비공개 처리", notes = "사용자가 등록한 게시물 정보를 비공개 처리합니다.")
    public CommonResponse disableCategory (
        @PathVariable
        @ApiParam(value = "게시물 토큰", example = "atcl_abcde12345") String token
    ) {
        return CommonResponse.success(articleService.disableArticleInfo(token));
    }

    @DeleteMapping("{token}")
    @ApiOperation(value = "게시물 삭제", notes = "사용자가 등록한 게시물 정보를 삭제합니다.")
    public CommonResponse deleteArticle (
        @PathVariable
        @ApiParam(value = "게시물 토큰", example = "atcl_abcde12345") String token
    ) {
        return CommonResponse.success(articleService.deleteCategoryInfo(token));
    }
}

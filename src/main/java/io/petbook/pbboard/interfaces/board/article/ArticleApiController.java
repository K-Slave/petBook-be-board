package io.petbook.pbboard.interfaces.board.article;

import io.petbook.pbboard.application.ArticleFacade;
import io.petbook.pbboard.common.response.CommonResponse;
import io.petbook.pbboard.domain.board.article.ArticleCommand;
import io.petbook.pbboard.domain.board.article.ArticleService;
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
    public CommonResponse getArticlesBriefList(ArticleCommand.Paginate paginate) {
        return CommonResponse.success(articleFacade.loadBriefList(paginate));
    }

    @GetMapping("/deleted")
    public CommonResponse getArticlesIsDeleted() {
        return CommonResponse.success(articleService.getArticleInfoIsDeleted());
    }

    @GetMapping("{token}")
    public CommonResponse getArticlesDetailView(@PathVariable String token) {
        return CommonResponse.success(articleFacade.loadDetailView(token));
    }

    @PostMapping
    public CommonResponse createArticle(@RequestBody ArticleCommand.Main request) {
        return CommonResponse.success(articleService.createArticleInfo(request));
    }

    @PutMapping
    public CommonResponse modifyArticle(@RequestBody ArticleCommand.Modifier modifier) {
        return CommonResponse.success(articleService.modifyArticleInfo(modifier));
    }

    @PutMapping("restore/{token}")
    public CommonResponse restoreArticle(@PathVariable String token) {
        return CommonResponse.success(articleService.restoreArticleInfo(token));
    }

    @PutMapping("enable/{token}")
    public CommonResponse enableCategory(@PathVariable String token) {
        return CommonResponse.success(articleService.enableArticleInfo(token));
    }

    @PutMapping("disable/{token}")
    public CommonResponse disableCategory(@PathVariable String token) {
        return CommonResponse.success(articleService.disableArticleInfo(token));
    }

    @DeleteMapping("{token}")
    public CommonResponse deleteArticle(@PathVariable String token) {
        return CommonResponse.success(articleService.deleteCategoryInfo(token));
    }
}

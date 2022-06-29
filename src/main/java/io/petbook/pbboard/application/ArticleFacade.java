package io.petbook.pbboard.application;

import io.petbook.pbboard.domain.board.article.ArticleCommand;
import io.petbook.pbboard.domain.board.article.ArticleInfo;
import io.petbook.pbboard.domain.board.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleFacade {
    /**
     * 여기에는 각종 서비스들이 모여서, 각각 필요한 데이터들에 대해 처리 및 주입하는 과정을 취합한다.
     * 예를 들어 게시글 목록을 불러온다고 치자.
     * -> A 데이터베이스에서 게시글 목록 조회
     * -> B 데이터베이스에서 게시글 별 회원 정보 조회
     * -> A 데이터베이스와 B 데이터베이스 내용을 주입 (SeriesFactory 구성 필요)
     * <p>
     * 참고로 백엔드 로직은 내구성을 높이지 말고, 외부에서도 예외를 던져서 ControllerAdvice 같은 곳에서 처리하는 게 더 낫다.
     * 웬만하면 try catch 문은 실무에서 사용하는 것을 요즘은 꺼려하는 편이다. (아무래도 MSA 트랜잭션으로 인한 데이터 정합성을 보장하기 위함이라 할까?)
     * <p>
     * 여기에는 로직을 흐르게 하더라도, 일부분 오류가 발생 하더라도 큰 상관이 없는 로직 (쏟은 물을 주어 담을 수 없듯이) 을 작성하는게 좋다.
     * 예를 들어 사용자 가입이 완료되고, 사용자 수신 일부 정보 (이메일 같은 거) 를 활용한 API 연동을 할 때 이를 실행할 때 오류가 발생 하더라도, 사용자 가입은 이미 되어 있듯이.
     */

    private final ArticleService articleService;

    private void injectArticleInfo(ArticleInfo.Main article) {
        String token = article.getToken();

        // [Kang] 좋아요, 조회수 같은 경우는 MSA 구조로 독립 시켜 카운팅 전용 서버를 만들어 개선할 예정이다.

        // [Kang] 좋아요 수 호출
        // LikerInfo.Main likerInfo = likerInfoService.findByArticleToken(token);

        // [Kang] 조회 수 호출
        // ViewerInfo.Main viewerInfo = viewerInfoService.findByArticleToken(token);

        // [Kang] 작성자 호출
        // UserInfo.Main userInfo = userInfoService.findByToken(article.getUserToken());

        article.modifyByAccessor(
                ArticleCommand.InfoAccessor.builder()
                        .likeCount(0L)
                        .viewCount(0L)
                        .author("[작성자]")
                        .build()
        );
    }

    public ArticleInfo.Paginate loadBriefList(ArticleCommand.Paginate paginate) {
        Map<String, Object> resultMap = articleService.getArticleBriefList(paginate);
        List<ArticleInfo.Brief> articles = (List<ArticleInfo.Brief>) resultMap.get("data");
        articles.forEach(article -> injectArticleInfo(article));

        return ArticleInfo.Paginate.builder()
                .data(articles)
                .total((Long) resultMap.get("total"))
                .pages((Long) resultMap.get("pages"))
                .current((Long) resultMap.get("current"))
                .build();
    }

    public ArticleInfo.Detail loadDetailView(String token) {
        ArticleInfo.Detail article = articleService.getArticleDetailInfo(token);
        injectArticleInfo(article);
        return article;
    }
}

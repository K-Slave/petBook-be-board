package io.petbook.pbboard.application;

import io.petbook.pbboard.domain.board.article.ArticleCommand;
import io.petbook.pbboard.domain.board.article.ArticleInfo;
import io.petbook.pbboard.domain.board.article.ArticleService;
import io.petbook.pbboard.domain.board.subscriber.SubscriberCommand;
import io.petbook.pbboard.domain.board.subscriber.SubscriberService;
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

    private final SubscriberService subscriberService;

    private void injectArticleInfo(ArticleInfo.Main article) {
        String token = article.getToken();

        // [Kang] 좋아요 수 호출
        // LikerInfo.Main likerInfo = likerInfoService.findByArticleToken(token);

        // [Kang] 작성자 호출
        // UserInfo.Main userInfo = userInfoService.findByToken(article.getUserToken());

        // [Kang] 키워드 호출

        // [Kang] 파일 호출

        article.modifyByAccessor(
                ArticleCommand.InfoAccessor.builder()
                        .likeCount(0L)
                        .viewCount(subscriberService.countByArticleToken(token))
                        .author("[작성자]")
                        .build()
        );
    }

    public ArticleInfo.Paginate loadBriefList(ArticleCommand.Paginate paginate) {
        Map<String, Object> resultMap = articleService.getArticleBriefList(paginate);
        List<ArticleInfo.Brief> articles = (List<ArticleInfo.Brief>) resultMap.get("briefs");
        articles.forEach(article -> injectArticleInfo(article));

        return ArticleInfo.Paginate.builder()
                .briefs(articles)
                .total((Long) resultMap.get("total"))
                .pages((Long) resultMap.get("pages"))
                .current((Long) resultMap.get("current"))
                .build();
    }

    public List<ArticleInfo.Main> loadArticleInfoIsDeleted() {
        List<ArticleInfo.Main> articles = articleService.getArticleInfoIsDeleted();
        articles.forEach(article -> injectArticleInfo(article));
        return articles;
    }

    public ArticleInfo.Detail loadDetailView(String token) {
        ArticleInfo.Detail article = articleService.getArticleDetailInfo(token);

        if (!article.getDeleted()) {
            // [Kang] 조회수 정보 추가 (TODO: 사용자 정보 존재 시, 토큰을 가져와 주입한다.)
            subscriberService.createForAnonymous(
                SubscriberCommand.Anonymous.builder().articleToken(token).build()
            );
        }

        injectArticleInfo(article);
        return article;
    }

    public ArticleInfo.Brief createArticleInfo(ArticleCommand.Main command) {
        ArticleInfo.Brief article = articleService.createArticleInfo(command);
        injectArticleInfo(article);
        return article;
    }

    public ArticleInfo.Brief modifyArticleInfo(ArticleCommand.Modifier command) {
        ArticleInfo.Brief article = articleService.modifyArticleInfo(command);
        injectArticleInfo(article);
        return article;
    }

    public ArticleInfo.DeleteProcStatus deleteArticleInfo(String token) {
        ArticleInfo.DeleteProcStatus article = articleService.deleteArticleInfo(token);
        // [Kang] TODO: Subscribe Delete By 연동 필요할까?
        return article;
    }

    public ArticleInfo.Brief restoreArticleInfo(String token) {
        ArticleInfo.Brief article = articleService.restoreArticleInfo(token);
        injectArticleInfo(article);
        return article;
    }

    public ArticleInfo.Main enableArticleInfo(String token) {
        ArticleInfo.Main article = articleService.enableArticleInfo(token);
        injectArticleInfo(article);
        return article;
    }

    public ArticleInfo.Main disableArticleInfo(String token) {
        ArticleInfo.Main article = articleService.disableArticleInfo(token);
        injectArticleInfo(article);
        return article;
    }
}

package io.petbook.pbboard.domain.board.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberStore subscriberStore;
    private final SubscriberReader subscriberReader;


    @Override
    public SubscriberInfo.Main createForAnonymous(SubscriberCommand.Anonymous anonymous) {
        Subscriber subscriber = subscriberStore.store(anonymous.toEntity());
        return SubscriberInfo.Main.toInfo(subscriber);
    }

    @Override
    public SubscriberInfo.Main createForAccessor(SubscriberCommand.Accessor accessor) {
        Subscriber subscriber = subscriberStore.store(accessor.toEntity());
        return SubscriberInfo.Main.toInfo(subscriber);
    }

    @Override
    public long countByArticleToken(String articleToken) {
        return subscriberReader.countByArticleToken(articleToken);
    }
}

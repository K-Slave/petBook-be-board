package io.petbook.pbboard.infrastructure.board.subscriber;

import io.petbook.pbboard.domain.board.subscriber.Subscriber;
import io.petbook.pbboard.domain.board.subscriber.SubscriberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriberStoreImpl implements SubscriberStore {
    private final SubscriberRepository subscriberRepository;

    @Override
    public Subscriber store(Subscriber subscriber) {
        return subscriberRepository.save(subscriber);
    }
}

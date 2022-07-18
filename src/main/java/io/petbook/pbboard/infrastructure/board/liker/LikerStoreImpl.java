package io.petbook.pbboard.infrastructure.board.liker;


import io.petbook.pbboard.domain.board.liker.Liker;
import io.petbook.pbboard.domain.board.liker.LikerStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikerStoreImpl implements LikerStore {
    private final LikerRepository likerRepository;

    @Override
    public Liker store(Liker liker) {
        return likerRepository.save(liker);
    }
}

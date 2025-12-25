package hello.wsd.domain.favorite.service;

import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import hello.wsd.domain.favorite.dto.FavoriteStoreResponse;
import hello.wsd.domain.favorite.entity.FavoriteStore;
import hello.wsd.domain.favorite.repository.FavoriteRepository;
import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.repository.StoreRepository;
import hello.wsd.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void addFavorite(User user, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "자신의 상점은 즐겨찾기할 수 없습니다.");
        }

        if (store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "자신의 상점은 즐겨찾기할 수 없습니다.");
        }

        if (favoriteRepository.existsByUserAndStore(user, store)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        FavoriteStore favoriteStore = FavoriteStore.builder()
                .user(user)
                .store(store)
                .build();

        favoriteRepository.save(favoriteStore);
    }

    @Transactional
    public void removeFavorite(User user, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        favoriteRepository.deleteByUserAndStore(user, store);
    }

    public Long countFavorites(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return favoriteRepository.countByStore(store);
    }

    public Page<FavoriteStoreResponse> getMyFavorites(User user, Pageable pageable) {
        return favoriteRepository.findByUser(user, pageable)
                .map(favoriteStore -> FavoriteStoreResponse.from(favoriteStore.getStore()));
    }
}

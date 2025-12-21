package hello.wsd.domain.store.service;

import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import hello.wsd.common.response.PageResponse;
import hello.wsd.domain.store.dto.CreateStoreRequest;
import hello.wsd.domain.store.dto.StoreResponse;
import hello.wsd.domain.store.dto.UpdateStoreRequest;
import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.repository.StoreRepository;
import hello.wsd.domain.user.entity.Role;
import hello.wsd.domain.user.repository.UserRepository;
import hello.wsd.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createStore(User user, CreateStoreRequest request) {

        User owner = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (owner.getRole() != Role.ROLE_OWNER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Store store = request.toEntity(owner);
        Store savedStore = storeRepository.save(store);
        return savedStore.getId();
    }

    public StoreResponse getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "상점을 찾을 수 없습니다."));

        return StoreResponse.from(store);
    }

    public PageResponse<StoreResponse> getStores(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(pageable);

        Page<StoreResponse> responsePage = storePage.map(StoreResponse::from);
        return PageResponse.from(responsePage);
    }

    @Transactional
    public void updateStore(Long storeId, User user, UpdateStoreRequest request) {
        User owner = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "가게를 찾을 수 없습니다."));

        // 본인 소유 확인
        if (!Objects.equals(store.getUser().getId(), owner.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "본인 소유의 가게가 아닙니다.");
        }

        store.updateStore(
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                request.getPhoneNumber(),
                request.getIntroduction(),
                request.getOperatingHours(),
                request.getStoreCategory());
    }

    @Transactional
    public void deleteStore(Long storeId, User user) {
        User owner = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "가게를 찾을 수 없습니다."));

        // 본인 소유 확인
        if (!Objects.equals(store.getUser().getId(), owner.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "본인 소유의 가게가 아닙니다.");
        }

        storeRepository.delete(store);
    }
}

package com.rena.application.repository.settings.user;

import com.rena.application.entity.model.settings.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    Optional<UserHistory> findByUserIdAndIsActive(Long userId, Boolean isActive);

    Optional<UserHistory> findByCodeAndIsActive(Integer code, Boolean is_active);
}
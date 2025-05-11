package com.rena.application.repository.settings.user;

import com.rena.application.entity.model.settings.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    Optional<UserHistory> findByUserIdAndIsActive(Long userId, Boolean isActive);

    Optional<UserHistory> findByCodeAndIsActive(Integer code, Boolean is_active);

    @Query("SELECT uh FROM UserHistory uh WHERE uh.isActive = true and uh.userId = " +
            "(SELECT o.user.id FROM OperatorLoginLog o WHERE o.station.name = ?1 AND o.isLogin = true)")
    Optional<UserHistory> findUserHistoryForActiveOperatorByStationName(String stationName);
}
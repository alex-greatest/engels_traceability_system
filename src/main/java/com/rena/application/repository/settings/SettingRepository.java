package com.rena.application.repository.settings;

import com.rena.application.entity.model.settings.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findSettingById(Long aLong);
}
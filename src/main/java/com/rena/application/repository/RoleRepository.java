package com.rena.application.repository;

import com.rena.application.entity.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(Collection<String> names);

    Role findByName(String name);
}
package com.rena.application.repository.settings.user;

import com.rena.application.entity.model.settings.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u JOIN u.role where u.username = ?1")
    Optional<User> findByUsernameAuthorization(String username);
    @Query("select u from User u where u.username <> 'admin' and u.role.name in ?1")
    List<User> findByIs_deletedFalseAndUsernameNotAndRole_NameIn(Collection<String> names);
    @Query("SELECT u, r FROM User u JOIN u.role r WHERE u.username <> 'admin'")
    List<User> findAllActiveUsers();


    Optional<User> findByUsername(String name);
    Optional<User> findByCode(Integer code);
}
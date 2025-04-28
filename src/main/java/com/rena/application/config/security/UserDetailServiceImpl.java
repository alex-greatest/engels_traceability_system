package com.rena.application.config.security;

import com.rena.application.entity.model.settings.user.User;
import com.rena.application.repository.settings.user.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NotBlank String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь с именем %s не найден", username)));
        if (user.getRole().getName().equals("Сборщик")) {
            throw new UsernameNotFoundException(String.format("Пользователь с именем %s не найден", username));
        }
        var roles = Stream.of(new SimpleGrantedAuthority(user.getRole().getName())).toList();
        return new UserSecurity(user.getCode(), user.getUsername(), user.getPassword(), roles);
    }
}

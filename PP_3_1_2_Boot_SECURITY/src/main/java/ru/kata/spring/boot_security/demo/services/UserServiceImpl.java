package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepos;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final UserRepos userRepos;

    public UserServiceImpl(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

    // Метод для поиска пользователя по имени пользователя (username)
    public User findByUserName(String username) {
        return userRepos.findByUsername(username);
    }

    @Override
    @Transactional
// Метод для загрузки пользователя по имени пользователя (username)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя в базе данных по имени пользователя
        User user = findByUserName(username);
        // Если пользователь не найден, выбрасываем исключение UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        // Создаем объект класса User, который требуется в Spring Security, для проверки аутентификации и авторизации
        org.springframework.security.core.userdetails.User springUser =
                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                        mapRolesToAuthorities(user.getRoles()));
        return springUser;
    }

    // Метод для преобразования списка ролей пользователя в коллекцию GrantedAuthority
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

}
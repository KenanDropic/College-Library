package com.library.repository;

import com.library.entity.Role;
import com.library.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @SuppressWarnings("SpringDataRepositoryMethodParametersInspection")
    List<Role> findByUsers(User user);

    @Override
    void delete(@NotNull Role role);
}

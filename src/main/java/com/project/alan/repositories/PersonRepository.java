package com.project.alan.repositories;

import com.project.alan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface PersonRepository extends JpaRepository<User, Long> {

    Set<User> findByIdIn(Collection<Long> ids);

    Optional<User> findByUsername(String username);
}

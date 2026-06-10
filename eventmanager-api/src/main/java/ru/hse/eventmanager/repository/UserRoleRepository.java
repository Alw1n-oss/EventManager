package ru.hse.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.eventmanager.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}
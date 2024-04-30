package net.uqcloud.infs7202.project.auth.repository;

import net.uqcloud.infs7202.project.auth.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
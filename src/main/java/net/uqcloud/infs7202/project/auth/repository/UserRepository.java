package net.uqcloud.infs7202.project.auth.repository;

import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Integer> {
    boolean existsByEmail(String email);

    AuthUser findByIdAndIsActiveTrue(int id);
    AuthUser findByEmailAndIsActiveTrue(String email);
    Page<AuthUser> findAllByIsActiveTrue(Pageable page);
    Page<AuthUser> findAllByIsActiveFalse(Pageable page);

    @Modifying
    @Query("update AuthUser u set u.isActive = ?2 where u.id = ?1")
    int setUserActive(int id, boolean isActive);
}

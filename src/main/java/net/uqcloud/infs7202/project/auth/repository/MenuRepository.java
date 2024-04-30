package net.uqcloud.infs7202.project.auth.repository;

import net.uqcloud.infs7202.project.auth.repository.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
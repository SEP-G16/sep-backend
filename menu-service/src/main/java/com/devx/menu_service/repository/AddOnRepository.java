package com.devx.menu_service.repository;

import com.devx.menu_service.model.AddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddOnRepository extends JpaRepository<AddOn, Long> {
    Optional<AddOn> findByName(String name);
}

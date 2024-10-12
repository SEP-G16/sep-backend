package com.devx.order_service.repository;

import com.devx.order_service.model.AddOn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnRepository extends JpaRepository<AddOn, Long> {
}

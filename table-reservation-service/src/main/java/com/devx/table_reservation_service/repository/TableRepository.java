package com.devx.table_reservation_service.repository;

import com.devx.table_reservation_service.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {

    Optional<RestaurantTable> findByTableNo(int tableNo);
}

package com.devx.order_service.repository;

import com.devx.order_service.model.OrderItemAddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemAddOnRepository extends JpaRepository<OrderItemAddOn, Long>
{

}

package com.programmingtechie.orderservcie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.orderservcie.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

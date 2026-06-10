package com.example.mspayment.dao.repository;

import com.example.mspayment.dao.entity.CourierBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierBalanceRepository extends JpaRepository<CourierBalance, Long> {
    Optional<CourierBalance> findByCourierId(Long courierId);
}
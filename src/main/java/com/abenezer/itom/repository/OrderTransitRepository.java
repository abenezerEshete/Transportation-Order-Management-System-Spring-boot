package com.abenezer.itom.repository;

import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.OrderTransit.OrderTransit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTransitRepository extends JpaRepository<OrderTransit, Integer> {

}
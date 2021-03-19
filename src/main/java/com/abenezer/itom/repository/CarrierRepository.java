package com.abenezer.itom.repository;

import com.abenezer.itom.model.CarrierRecords.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Integer> {

}
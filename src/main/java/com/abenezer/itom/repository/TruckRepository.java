package com.abenezer.itom.repository;

import com.abenezer.itom.model.Vehicles.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TruckRepository extends JpaRepository<Truck, Integer>, JpaSpecificationExecutor<Truck> {

}
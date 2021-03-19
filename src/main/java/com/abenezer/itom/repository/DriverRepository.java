package com.abenezer.itom.repository;

import com.abenezer.itom.model.Drivers.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer>, JpaSpecificationExecutor<Driver> {

}
package com.abenezer.itom.repository;

import com.abenezer.itom.model.Vehicles.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer, Integer>, JpaSpecificationExecutor<Trailer> {

}
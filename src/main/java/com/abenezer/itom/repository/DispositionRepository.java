package com.abenezer.itom.repository;

import com.abenezer.itom.model.CustomerOrders.Disposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DispositionRepository extends JpaRepository<Disposition, Integer>, JpaSpecificationExecutor<Disposition> {

}
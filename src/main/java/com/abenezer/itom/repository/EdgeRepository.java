package com.abenezer.itom.repository;

import com.abenezer.itom.algorithms.dijkstra.model.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Integer>, JpaSpecificationExecutor<Edge> {

}
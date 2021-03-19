package com.abenezer.itom.repository;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexRepository extends JpaRepository<Vertex, Integer>, JpaSpecificationExecutor<Vertex> {

}
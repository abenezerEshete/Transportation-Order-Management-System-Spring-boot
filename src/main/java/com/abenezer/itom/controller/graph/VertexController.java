/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.graph;

import com.abenezer.itom.algorithms.dijkstra.controller.GraphRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.controller.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Vertex related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/vertex")
public class VertexController {


	private final GraphRegistryDatabase graphRegistryDatabase;

	@Autowired
	public VertexController (GraphRegistryDatabase graphRegistryDatabase) {
		this.graphRegistryDatabase = graphRegistryDatabase;
	}


	/**
	 * Handle REST get request and used to get Vertex list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getVertex () {
		List<Vertex> vertex = graphRegistryDatabase.getVertices ();
		Map<String, Object> map = Util.buildResponse (vertex);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Vertex record.
	 *
	 * @param id : id of the vertex to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getVertexById (@PathVariable int id) {
		Vertex vertex = graphRegistryDatabase.getVertexById (id);
		Map<String, Object> map = Util.buildResponse (vertex);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Vertex to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addVertex (@RequestBody @Valid Vertex vertex) {

		Vertex savedVertex = graphRegistryDatabase.addVertex (vertex);
		Map<String, Object> map = Util.buildResponse (savedVertex);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Vertex
	 *
	 * @param id : id of the vertex to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editVertex (
		@PathVariable("id") int id,
		@RequestBody @Valid Vertex vertex) {

		vertex.setId (id);
		Vertex savedVertex = graphRegistryDatabase.editVertex (vertex);
		Map<String, Object> map = Util.buildResponse (savedVertex);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Vertex record.
	 *
	 * @param id : id of the vertex to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteVertex (@PathVariable int id) {
		graphRegistryDatabase.deleteVertex (id);
	}




}

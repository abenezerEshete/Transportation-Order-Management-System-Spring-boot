/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.graph;

import com.abenezer.itom.algorithms.dijkstra.controller.GraphRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Edge;
import com.abenezer.itom.controller.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Customer Order related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/edge")
public class EdgeController {


	private final GraphRegistryDatabase graphRegistryDatabase;

	@Autowired
	public EdgeController (GraphRegistryDatabase graphRegistryDatabase) {
		this.graphRegistryDatabase = graphRegistryDatabase;
	}


	/**
	 * Handle REST get request and used to get Edge list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getEdge () {
		List<Edge> edge = graphRegistryDatabase.getEdges ();
		Map<String, Object> map = Util.buildResponse (edge);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Edge record.
	 *
	 * @param id : id of the edge to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getEdgeById (@PathVariable int id) {
		Edge edge = graphRegistryDatabase.getEdgeById (id);
		Map<String, Object> map = Util.buildResponse (edge);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Edge to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addEdge (@RequestBody @Valid Edge edge) {

		Edge savedEdge = graphRegistryDatabase.addEdge (edge);
		Map<String, Object> map = Util.buildResponse (savedEdge);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Edge
	 *
	 * @param id : id of the edge to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editEdge (
		@PathVariable("id") int id,
		@RequestBody @Valid Edge edge) {

		edge.setId (id);
		Edge savedEdge = graphRegistryDatabase.editEdge (edge);
		Map<String, Object> map = Util.buildResponse (savedEdge);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Edge record.
	 *
	 * @param id : id of the edge to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEdge (@PathVariable int id) {
		graphRegistryDatabase.deleteEdge (id);
	}




}

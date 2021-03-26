/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.Vehicles;

import com.abenezer.itom.Service.Vehicles.TrailerDatabaseRegistry;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.Vehicles.Trailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Trailer related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/trailer")
public class TrailerController {


	private final TrailerDatabaseRegistry trailerDatabaseRegistry;

	@Autowired
	public TrailerController (TrailerDatabaseRegistry trailerDatabaseRegistry) {
		this.trailerDatabaseRegistry = trailerDatabaseRegistry;
	}


	/**
	 * Handle REST get request and used to get Trailer list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getTrailer () {
		List<Trailer> trailers = trailerDatabaseRegistry.getTrailers ();
		Map<String, Object> map = Util.buildResponse (trailers);
		System.out.println (" map:---------"+map.keySet ());
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Trailer record.
	 *
	 * @param id : id of the trailer to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getTrailerById (@PathVariable int id) {
		Trailer trailer = trailerDatabaseRegistry.getTrailerOfOrder (id);
		Map<String, Object> map = Util.buildResponse (trailer);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Trailer to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid Trailer trailer) {

		Trailer savedTrailer = trailerDatabaseRegistry.addTrailer (trailer);
		Map<String, Object> map = Util.buildResponse (savedTrailer);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Trailer
	 *
	 * @param id : id of the trailer to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid Trailer trailer) {

		trailer.setId (id);
		Trailer savedCarrier = trailerDatabaseRegistry.editTrailer (trailer);
		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Trailer record.
	 *
	 * @param id : id of the trailer to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		trailerDatabaseRegistry.delete (id);
	}


	/**
	 * Handle REST GET request and used to get Trailers of Trailer
	 *
	 * @param newStart  : start date
	 * @param newFinish : finish date
	 * @param vertexId  : id of the vertex
	 * @return ResponseEntity<Map < String, Object>> : return record list
	 */
	@GetMapping(value = "/AvailableTrailer")
	public ResponseEntity<Map<String, Object>> getAvailableTrailers (
		@RequestParam(name = "newStart" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date newStart,
		@RequestParam(name = "newFinish" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date newFinish,
		@RequestParam(name = "vertexId") int vertexId) {

		List<Trailer> trailers = trailerDatabaseRegistry.getAvailableTrailers (newStart, newFinish, vertexId);
		Map<String, Object> map = Util.buildResponse (trailers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle the request REST GET request and used to get Trailers for specific TrailerPlateNumber
	 *
	 * @param dispositionId : disposition id
	 * @param startDate     : disposition start date
	 * @param endDate       : disposition end date
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/AvailableTrailers")
	public ResponseEntity<Map<String, Object>> getAvailableTrailers (
		@RequestParam(name = "dispositionId") int dispositionId,
		@RequestParam(name = "startDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
		@RequestParam(name = "endDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {

		List<Trailer> trailers = trailerDatabaseRegistry.getAvailableTrailers (startDate, endDate, dispositionId);
		Map<String, Object> map = Util.buildResponse (trailers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.Vehicles;

import com.abenezer.itom.Service.Vehicles.TruckDatabaseRegistry;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.Vehicles.Truck;
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
 * Controller class responsible for handling Truck related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/truck")
public class TruckController {


	private final TruckDatabaseRegistry truckDatabaseRegistry;

	@Autowired
	public TruckController (TruckDatabaseRegistry truckDatabaseRegistry) {
		this.truckDatabaseRegistry = truckDatabaseRegistry;
	}


	/**
	 * Handle REST get request and used to get Truck list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getTruck () {
		List<Truck> trucks = truckDatabaseRegistry.getTrucks ();
		Map<String, Object> map = Util.buildResponse (trucks);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Truck record.
	 *
	 * @param id : id of the truck to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getTruckById (@PathVariable int id) {
		Truck truck = truckDatabaseRegistry.getTruckOfDisposition (id);
		Map<String, Object> map = Util.buildResponse (truck);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Truck to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid Truck truck) {

		Truck savedTruck = truckDatabaseRegistry.addTruck (truck);
		Map<String, Object> map = Util.buildResponse (savedTruck);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Truck
	 *
	 * @param id : id of the truck to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid Truck truck) {

		truck.setId (id);
		Truck savedCarrier = truckDatabaseRegistry.editTruck (truck);
		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Truck record.
	 *
	 * @param id : id of the truck to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		truckDatabaseRegistry.deleteID (id);
	}


	/**
	 * Handle REST GET request and used to get Trucks of Truck
	 *
	 * @param newStart  : start date
	 * @param newFinish : finish date
	 * @param vertexId  : id of the vertex
	 * @return ResponseEntity<Map < String, Object>> : return record list
	 */
	@GetMapping(value = "/AvailableTruck")
	public ResponseEntity<Map<String, Object>> getAvailableTrucks (
		@RequestParam(name = "newStart")  @DateTimeFormat(pattern="yyyy-MM-dd") Date newStart,
		@RequestParam(name = "newFinish")  @DateTimeFormat(pattern="yyyy-MM-dd") Date newFinish,
		@RequestParam(name = "vertexId") int vertexId) {

		List<Truck> trucks = truckDatabaseRegistry.getAvailableTrucks (newStart, newFinish, vertexId);
		Map<String, Object> map = Util.buildResponse (trucks);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get Trucks for specific TruckPlateNumber
	 *
	 * @param dispositionId : disposition id
	 * @param startDate     : disposition start date
	 * @param endDate       : disposition end date
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/AvailableTrucks")
	public ResponseEntity<Map<String, Object>> getAvailableTrucks (
		@RequestParam(name = "dispositionId") int dispositionId,
		@RequestParam(name = "startDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
		@RequestParam(name = "endDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {

		List<Truck> trucks = truckDatabaseRegistry.getAvailableTrucks (startDate, endDate, dispositionId);
		Map<String, Object> map = Util.buildResponse (trucks);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


}

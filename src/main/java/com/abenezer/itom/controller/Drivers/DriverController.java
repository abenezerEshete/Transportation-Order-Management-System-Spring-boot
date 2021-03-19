/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.Drivers;

import com.abenezer.itom.Service.Drivers.DriversDatabaseRegistry;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.Drivers.Driver;
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
 * Controller class responsible for handling Customer Order related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/Driver")
public class DriverController {


	private final DriversDatabaseRegistry driversDatabaseRegistry;

	@Autowired
	public DriverController (DriversDatabaseRegistry driversDatabaseRegistry) {
		this.driversDatabaseRegistry = driversDatabaseRegistry;
	}


	/**
	 * Handle REST get request and used to get Driver list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getCarrier () {
		List<Driver> drivers = driversDatabaseRegistry.getDrivers ();
		Map<String, Object> map = Util.buildResponse (drivers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Driver record.
	 *
	 * @param id : id of the driver to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getCarrierById (@PathVariable int id) {
		Driver driver = driversDatabaseRegistry.getDriverById (id);
		Map<String, Object> map = Util.buildResponse (driver);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Driver to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid Driver driver) {

		Driver savedDriver = driversDatabaseRegistry.addDriver (driver);
		Map<String, Object> map = Util.buildResponse (savedDriver);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Driver
	 *
	 * @param id : id of the driver to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid Driver driver) {

		driver.setId (id);
		Driver savedCarrier = driversDatabaseRegistry.editDriver (driver);
		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Driver record.
	 *
	 * @param id : id of the driver to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		driversDatabaseRegistry.delete (id);
	}


	/**
	 * Handle REST GET request and used to get Drivers of Trailer
	 *
	 * @param newDispositionLocation : id of the trailer
	 * @param startDate : id of the trailer
	 * @return ResponseEntity<Map < String, Object>> : return record list
	 */
	@GetMapping(value = "/nearby/")
	public ResponseEntity<Map<String, Object>> getDriversNearBy (
		@RequestParam(name = "newDispositionLocationId") int newDispositionLocation,
		@RequestParam(name = "startDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate) {

		List<Driver> drivers = driversDatabaseRegistry.getDriversNearBy (newDispositionLocation, startDate);
		Map<String, Object> map = Util.buildResponse (drivers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get Drivers for specific TrailerPlateNumber
	 *
	 * @param dispositionId : disposition id
	 * @param startDate : disposition start date
	 * @param endDate : disposition end date
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/AvailableDrivers")
	public ResponseEntity<Map<String, Object>> getAvailableDrivers (
		@RequestParam(name = "dispositionId") int dispositionId,
		@RequestParam(name = "startDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
		@RequestParam(name = "endDate" ) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {

		List<Driver> drivers = driversDatabaseRegistry.getAvailableDrivers (startDate, endDate, dispositionId);
		Map<String, Object> map = Util.buildResponse (drivers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}



}

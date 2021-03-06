/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.CarrierRecords;

import com.abenezer.itom.Service.CarrierRecords.CarrierRegistryDatabase;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.repository.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Carrier related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/carrier")
public class CarrierController {


	final
	CarrierRepository carrierRepository;

	private final CarrierRegistryDatabase carrierRegistryDatabase;

	@Autowired
	public CarrierController (CarrierRegistryDatabase carrierRegistryDatabase, CarrierRepository carrierRepository) {
		this.carrierRegistryDatabase = carrierRegistryDatabase;
		this.carrierRepository = carrierRepository;
	}


	/**
	 * Handle REST get request and used to get Carrier list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getCarrier () {
		List<Carrier> carriers =carrierRepository.findAll ();
		Map<String, Object> map = Util.buildResponse (carriers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Carrier record.
	 *
	 * @param id : id of the carrier to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getCarrierById (@PathVariable int id) {
		Carrier carriers = carrierRegistryDatabase.getCarrierById (id);
		Map<String, Object> map = Util.buildResponse (carriers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Carrier to the table
	 *
	 * @param carrier : carrier record to save in the database
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid Carrier carrier) {

		System.out.println ("hiiiii:"+carrier);
		Carrier savedCarrier = carrierRegistryDatabase.addCarrier (carrier);
		System.out.println ("Saved c: "+savedCarrier);
		Map<String, Object> map = Util.buildResponse (savedCarrier);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Carrier
	 *
	 * @param id : id of the carrier to be updated of carrier
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid Carrier carrier) {

		carrier.setId (id);
		Carrier savedCarrier = carrierRegistryDatabase.editCarrier (carrier);
		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Carrier record.
	 *
	 * @param id : id of the carrier to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		carrierRegistryDatabase.deleteById (id);
	}


}

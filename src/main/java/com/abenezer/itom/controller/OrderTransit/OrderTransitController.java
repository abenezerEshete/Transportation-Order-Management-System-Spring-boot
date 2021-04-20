/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.OrderTransit;

import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.OrderTransit.OrderTransit;
import com.abenezer.itom.repository.OrderTransitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class responsible for handling Carrier related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/orderTransit")
public class OrderTransitController {


	final
	OrderTransitRepository orderTransitRepository;


	@Autowired
	public OrderTransitController (OrderTransitRepository orderTransitRepository){
		this.orderTransitRepository = orderTransitRepository;
	}


	/**
	 * Handle REST get request and used to get order transit location list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getOrderTransit () {
		List<OrderTransit> carriers =orderTransitRepository.findAll ();
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
	public ResponseEntity<Map<String, Object>> getOrderTransitById (@PathVariable int id) {
		Optional<OrderTransit> orderTransit = orderTransitRepository.findById (id);
		Map<String, Object> map = Util.buildResponse (orderTransit);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Order Transit to the table
	 *
	 * @param orderTransit : carrier record to save in the database
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addOrderTransit (@RequestBody @Valid OrderTransit orderTransit) {

		OrderTransit savedOrderTransit = orderTransitRepository.save(orderTransit);
		System.out.println ("Saved c: "+savedOrderTransit);
		Map<String, Object> map = Util.buildResponse (savedOrderTransit);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Order Transit
	 *
	 * @param id : id of the carrier to be updated of Order Transit
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editOrderTransit (
		@PathVariable("id") int id,
		@RequestBody @Valid OrderTransit orderTransit) {

		orderTransit.setId (id);
		OrderTransit savedOrderTransit = orderTransitRepository.save (orderTransit);
		Map<String, Object> map = Util.buildResponse (savedOrderTransit);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Order Transit  record.
	 *
	 * @param id : id of the carrier to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		orderTransitRepository.deleteById (id);
	}


}

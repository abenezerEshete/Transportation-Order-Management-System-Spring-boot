/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.CustomerOrders;

import com.abenezer.itom.Service.CustomerOrders.CustomerOrderRegistryDatabase;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Customer Order related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/CustomerOrder")
public class CustomerOrderController {


	private final CustomerOrderRegistryDatabase customerOrderRegistryDatabase;

	@Autowired
	public CustomerOrderController (CustomerOrderRegistryDatabase customerOrderRegistryDatabase) {
		this.customerOrderRegistryDatabase = customerOrderRegistryDatabase;
	}


	/**
	 * Handle REST get request and used to get CustomerOrder list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getCarrier () {
		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getCustomerOrders ();
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete CustomerOrder record.
	 *
	 * @param id : id of the customerOrder to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getCarrierById (@PathVariable int id) {
		CustomerOrder customerOrder = customerOrderRegistryDatabase.getCustomerOrdersWhereId (id);
		Map<String, Object> map = Util.buildResponse (customerOrder);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add CustomerOrder to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid CustomerOrder customerOrder) {

		CustomerOrder savedCustomerOrder = customerOrderRegistryDatabase.addCustomerOrder (customerOrder);
		Map<String, Object> map = Util.buildResponse (savedCustomerOrder);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update CustomerOrder
	 *
	 * @param id : id of the customerOrder to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid CustomerOrder customerOrder) {

		customerOrder.setId (id);
		CustomerOrder savedCarrier = customerOrderRegistryDatabase.editCustomerOrder (customerOrder);

		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete CustomerOrder record.
	 *
	 * @param id : id of the customerOrder to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		customerOrderRegistryDatabase.deleteId (id);
	}


	/**
	 * Handle REST GET request and used to get CustomerOrders of Trailer
	 *
	 * @param trailerId : id of the trailer
	 * @return ResponseEntity<Map < String, Object>> : return record list
	 */
	@GetMapping(value = "/byTrailer")
	public ResponseEntity<Map<String, Object>> getCustomerOrdersOfTrailer (
		@RequestParam(name = "trailer_id") int trailerId) {
		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getCustomerOrdersOfTrailer (trailerId);

		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get CustomerOrders for specific TrailerPlateNumber
	 *
	 * @param trailerPlateNumber : Trailer Plate Number
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/byPlateNumber")
	public ResponseEntity<Map<String, Object>> getCustomerOrdersWhereTrailerPlateNumber (
		@RequestParam(name = "TrailerPlateNumber") String trailerPlateNumber) {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getCustomerOrdersWhereTrailerPlateNumber (trailerPlateNumber);

		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get CustomerOrders for specific company
	 *
	 * @param inputCompany : requiest parameter of the company name
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/byCompany")
	public ResponseEntity<Map<String, Object>> getCustomerOrdersWhereCustomerCompany (
		@RequestParam(name = "company", required = true) String inputCompany) {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getCustomerOrdersWhereCustomerCompany (inputCompany);

		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get CustomerOrders for specific despondent LastName
	 *
	 * @param disponentLastName : deponent last name
	 * @return ResponseEntity<Map < String, Object>> : return  record
	 */
	@GetMapping(value = "/byDisponent")
	public ResponseEntity<Map<String, Object>> getCustomerOrdersByDesponent (
		@RequestParam(name = "desponentLastName") String disponentLastName) {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getCustomerOrdersWhereDisponent (disponentLastName);
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get routed CustomerOrders
	 * * @return ResponseEntity<Map < String, Object>> : return  routed record List
	 */
	@GetMapping(value = "/routed")
	public ResponseEntity<Map<String, Object>> getRoutedCustomerOrders () {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getRoutedCustomerOrders ();
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get  in progress CustomerOrders
	 * * @return ResponseEntity<Map < String, Object>> : return  routed record List
	 */
	@GetMapping(value = "/inProgress")
	public ResponseEntity<Map<String, Object>> getOrdersInProgressCustomerOrders () {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getOrdersInProgressCustomerOrders ();
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get import type CustomerOrders
	 * * @return ResponseEntity<Map < String, Object>> : return  import type routed record List
	 */
	@GetMapping(value = "/import")
	public ResponseEntity<Map<String, Object>> getImportCustomerOrders () {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getImportCustomerOrders ();
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}

	/**
	 * Handle REST GET request and used to get export type CustomerOrders
	 * * @return ResponseEntity<Map < String, Object>> : return export type customer order List
	 */
	@GetMapping(value = "/export")
	public ResponseEntity<Map<String, Object>> getExportCustomerOrders () {

		List<CustomerOrder> customerOrders = customerOrderRegistryDatabase.getExportCustomerOrders ();
		Map<String, Object> map = Util.buildResponse (customerOrders);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


}

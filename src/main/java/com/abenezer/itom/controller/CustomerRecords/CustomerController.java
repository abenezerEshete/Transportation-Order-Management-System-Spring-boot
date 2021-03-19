/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.CustomerRecords;

import com.abenezer.itom.Service.CustomerRecords.CustomerRegistryDatabase;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.CustomerRecords.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling Customer related CRUD operation requests.
 *
 * @author AbenezerEsheteTilahu
 */

@RestController()
@RequestMapping("/api/v1/Customer")
public class CustomerController {


	private final CustomerRegistryDatabase customerRegistryDatabase;

	@Autowired
	public CustomerController (CustomerRegistryDatabase customerRegistryDatabase) {
		this.customerRegistryDatabase = customerRegistryDatabase;
	}


	/**
	 * Handle REST get request and used to get Customer list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getCarrier () {
		List<Customer> Customer = customerRegistryDatabase.getCustomers ();
		Map<String, Object> map = Util.buildResponse (Customer);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Customer record.
	 *
	 * @param id : id of the customerOrder to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getCarrierById (@PathVariable int id) {
		Customer customerOrder = customerRegistryDatabase.getCustomerOfOrder (id);
		Map<String, Object> map = Util.buildResponse (customerOrder);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Customer to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid Customer customer) {

		Customer savedCustomer = customerRegistryDatabase.addCustomer (customer);
		Map<String, Object> map = Util.buildResponse (savedCustomer);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Customer
	 *
	 * @param id : id of the customerOrder to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid Customer customer) {

		customer.setId (id);
		Customer savedCarrier = customerRegistryDatabase.editCustomer (customer);
		Map<String, Object> map = Util.buildResponse (savedCarrier);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Customer record.
	 *
	 * @param id : id of the customerOrder to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		customerRegistryDatabase.deleteById (id);
	}


}

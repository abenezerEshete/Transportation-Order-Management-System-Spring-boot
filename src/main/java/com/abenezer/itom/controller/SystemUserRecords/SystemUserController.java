/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.SystemUserRecords;

import com.abenezer.itom.Service.systemUserRecords.SystemUserRegistryDatabase;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.systemUserRecords.SystemUser;
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
@RequestMapping("/api/v1/user")
public class SystemUserController {


	private final SystemUserRegistryDatabase systemUsersDatabaseRegistry;

	@Autowired
	public SystemUserController (SystemUserRegistryDatabase systemUsersDatabaseRegistry) {
		this.systemUsersDatabaseRegistry = systemUsersDatabaseRegistry;
	}


	/**
	 * Handle REST get request and used to get SystemUser list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getCarrier () {
		List<SystemUser> systemUsers = systemUsersDatabaseRegistry.getUsers ();
		Map<String, Object> map = Util.buildResponse (systemUsers);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete SystemUser record.
	 *
	 * @param id : id of the systemUser to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getCarrierById (@PathVariable int id) {
		SystemUser systemUser = systemUsersDatabaseRegistry.getDisponentOfOrder (id);
		Map<String, Object> map = Util.buildResponse (systemUser);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add SystemUser to the table
	 *
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addCarrier (@RequestBody @Valid SystemUser systemUser) {

		SystemUser savedSystemUser = systemUsersDatabaseRegistry.addSystemUser (systemUser);
		Map<String, Object> map = Util.buildResponse (savedSystemUser);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update SystemUser
	 *
	 * @param id : id of the systemUser to be updated
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editCarrier (
		@PathVariable("id") int id,
		@RequestBody @Valid SystemUser systemUser) {

		systemUser.setId (id);
		SystemUser savedCarrier = systemUsersDatabaseRegistry.editUser (systemUser);
		Map<String, Object> map = Util.buildResponse (savedCarrier);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete SystemUser record.
	 *
	 * @param id : id of the systemUser to be deleted
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCarrier (@PathVariable int id) {
		systemUsersDatabaseRegistry.deleteUserById (id);
	}


}

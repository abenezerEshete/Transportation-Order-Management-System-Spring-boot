/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.controller.bid;

import com.abenezer.itom.Service.CarrierRecords.CarrierRegistryDatabase;
import com.abenezer.itom.controller.util.Util;
import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.bid.Bid;
import com.abenezer.itom.repository.BidRepository;
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
@RequestMapping("/api/v1/bid")
public class BidController {


	private final BidRepository bidRepository;

	@Autowired
	public BidController (BidRepository bidRepository) {
		this.bidRepository = bidRepository;
	}


	/**
	 * Handle REST get request and used to get bid list
	 *
	 * @return ResponseEntity<Map < String, Object>> :list of records
	 */
	@GetMapping(path = "/")
	private ResponseEntity<Map<String, Object>> getBidList () {
		List<Bid> bidList = bidRepository.findAll ();
		Map<String, Object> map = Util.buildResponse (bidList);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Carrier record.
	 *
	 * @param id : id of the carrier to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@GetMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> getBidById (@PathVariable int id) {
		Optional<Bid> optionalBid = bidRepository.findById (id);
		Bid  bid = optionalBid.isPresent ()?optionalBid.get ():null;
		Map<String, Object> map = Util.buildResponse (bid);
		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST POST request and used to add Carrier to the table
	 *
	 * @param carrier : carrier record to save in the database
	 * @return ResponseEntity<Map < String, Object>> : return saved record
	 */
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> addBid (@RequestBody @Valid Bid bid) {

			Bid savedBid = bidRepository.save (bid);
		Map<String, Object> map = Util.buildResponse (savedBid);
		return new ResponseEntity<> (map, HttpStatus.CREATED);
	}


	/**
	 * Handle REST PUT request and used to update Carrier
	 *
	 * @param id : id of the carrier to be updated of carrier
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@PutMapping(value = "{id}")
	public ResponseEntity<Map<String, Object>> editBid (
		@PathVariable("id") int id,
		@RequestBody @Valid Bid bid) {

		bid.setId (id);
		Bid savedBid = bidRepository.save (bid);
		Map<String, Object> map = Util.buildResponse (savedBid);

		return new ResponseEntity<> (map, HttpStatus.OK);
	}


	/**
	 * Handle REST DELETE request and used to Delete Bid record.
	 *
	 * @param id : id of the carrier to be deleted
	 * @return ResponseEntity<Map < String, Object>> : return updated record
	 */
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBId(@PathVariable int id) {
		bidRepository.deleteById (id);
	}


}

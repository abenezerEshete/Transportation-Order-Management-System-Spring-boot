/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.CarrierRecords;

import com.abenezer.itom.model.CarrierRecords.*;
import com.abenezer.itom.repository.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class CarrierRegistryDatabase {

	private final CarrierRepository carrierRepository;

	List<Carrier> carriers = new ArrayList<Carrier> ();

	@Autowired
	private CarrierRegistryDatabase (CarrierRepository carrierRepository) {
		this.carrierRepository = carrierRepository;
		this.getCarriers ();
	}

// TODO: create user defined exception.to handle integrity cases

	public void addCarrier (String company, Mode mode, CarrierRole role) {
		carrierRepository.save (new Carrier (company, role, mode));
	}

	public Carrier addCarrier (Carrier carrier) {
		System.out.println ("here 2"+carrier);
		return carrierRepository.save (carrier);
	}

	public Carrier getCarrierOfDisposition (int id) {
		Carrier carrier = null;
		if (carriers.isEmpty ()) {
			this.getCarriers ();
		}
		Iterator<Carrier> it = carriers.iterator ();
		while (it.hasNext ()) {
			carrier = it.next ();
			if (carrier.getId () == id) {
				break;
			}
		}

		return carrier;
	}



	public List<Carrier> getCarriers () {
			carriers = carrierRepository.findAll ();
		return carriers;
	}

	private Mode getMode (String mod) {
		Mode mode = null;
		if (mod.equals ("Rail")) mode = new Rail ();
		else if (mod.equals ("Road")) mode = new Road ();
		else mode = new Sea ();
		return mode;
	}

	private String getMode (Mode mod) {
		String mode;
		if (mod instanceof Rail) mode = "Rail";
		else if (mod instanceof Road) mode = "Road";
		else mode = "Sea";
		return mode;
	}

	// TODO: create user defined exception.to handle integrity cases
	public void editCarrier (Carrier carrier, String company, Mode mode, CarrierRole role) {
		carrier.setCompany (company);
		carrier.setMode (mode);
		carrier.setRole (role);
		carrierRepository.save (carrier);
	}

	// TODO: create user defined exception.to handle integrity cases
	public Carrier editCarrier (Carrier carrier) {
		return carrierRepository.save (carrier);
	}

	public Carrier getCarrierById (int carrierId) {
		Optional<Carrier> carrier = carrierRepository.findById (carrierId);
		if (carrier.isPresent ())
			return carrier.get ();
		return null;
	}

	public void delete (Carrier carrier) {

		carriers.remove (carrier);
		carrierRepository.delete (carrier);
	}

	public void deleteById (int id) {

		carrierRepository.deleteById (id);
		carriers.removeIf (carrier -> carrier.getId () == id);
	}

	public Carrier getInternalCarrier () {
		for (int i = 0; i < carriers.size (); i++) {
			Carrier carrier = carriers.get (i);
			if (carrier.getRole () instanceof Internal) {
				return carrier;
			}
		}
		return null;
	}

	public ArrayList<Carrier> getInternalCarriers () {
		List<Carrier> carriers = this.getCarriers ();
		ArrayList<Carrier> internalCarriers = new ArrayList<Carrier> ();
		for (int i = 0; i < carriers.size (); i++) {
			Carrier carrier = carriers.get (i);
			if (carrier.getRole () instanceof Internal) {
				internalCarriers.add (carrier);
			}
		}
		return internalCarriers;
	}

	public ArrayList<Carrier> getExternalCarriers () {
		List<Carrier> carriers = this.getCarriers ();
		ArrayList<Carrier> externalCarriers = new ArrayList<Carrier> ();
		for (int i = 0; i < carriers.size (); i++) {
			Carrier carrier = carriers.get (i);
			if (carrier.getRole () instanceof External) {
				externalCarriers.add (carrier);
			}
		}
		return externalCarriers;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.DispositionRecords;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.CarrierRecords.External;
import com.abenezer.itom.model.CarrierRecords.Internal;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
import com.abenezer.itom.model.CustomerOrders.Disposition;
import com.abenezer.itom.model.Drivers.Driver;
import com.abenezer.itom.model.Vehicles.Truck;
import com.abenezer.itom.repository.DispositionRepository;
import com.lynden.gmapsfx.service.directions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class DispositionRegistryDatabase implements DirectionsServiceCallback {

	private final DispositionRepository dispositionRepository;

	private  List<Disposition> dispositions= new ArrayList<> ();

	@Autowired
	private DispositionRegistryDatabase (DispositionRepository dispositionRepository) {
		this.dispositionRepository = dispositionRepository;
		this.loadDispositions ();
	}
// TODO: create user defined exception.to handle integrity cases

	public void addExternalDisposition (Vertex origin, Vertex destination,
					    Carrier carrier, String startDate, String finishDate, double tarrif,
					    CustomerOrder order) {
		Disposition disposition = new Disposition (carrier, origin, destination, startDate, finishDate, tarrif, order);
		dispositionRepository.save (disposition);
		dispositions.add (disposition);
	}

	public void addInternalDisposition (Vertex origin, Vertex destination,
					    Carrier carrier, String startDate, String finishDate, double tarrif,
					    CustomerOrder order, Truck truck, Driver driver) {
		Disposition disposition = new Disposition (carrier, origin, destination, startDate, finishDate, tarrif, order, driver, truck);

		dispositionRepository.save (disposition);
		dispositions.add (disposition);
	}

	public ArrayList<Disposition> getDispositionsOfOrder (int id) {
		ArrayList<Disposition> orderDispositions = new ArrayList<Disposition> ();

		if (dispositions.isEmpty ()) {
			this.getDispositions ();
		}
		Iterator<Disposition> it = dispositions.iterator ();
		while (it.hasNext ()) {
			Disposition disposition = it.next ();
			CustomerOrder order = disposition.getCustomerOrder ();
			if (order.getId () == id) {
				orderDispositions.add (disposition);
			}
		}

		return orderDispositions;
	}

	public ArrayList<Disposition> getDispositionsOfDriver (int id) throws ParseException {
		ArrayList<Disposition> driverDispositions = new ArrayList<Disposition> ();
		if (dispositions.isEmpty ()) {
			this.getDispositions ();
		}
		for (int i = 0; i < dispositions.size (); i++) {
			Disposition disposition = dispositions.get (i);
			if (disposition.getCarrier ().getRole () instanceof Internal) {
				Driver driver = disposition.getDriver ();
				if (driver.getId () == id) {
					driverDispositions.add (disposition);
				}
			}
		}
		return driverDispositions;
	}

	public ArrayList<Disposition> getDispositionsOfTruck (int id) throws ParseException {
		ArrayList<Disposition> truckDispositions = new ArrayList<Disposition> ();
		if (dispositions.isEmpty ()) {
			this.getDispositions ();
		}
		for (int i = 0; i < dispositions.size (); i++) {
			Disposition disposition = dispositions.get (i);
			if (disposition.getCarrier ().getRole () instanceof Internal) {
				Truck truck = (Truck) disposition.getTruck ();
				if (truck.getId () == id) truckDispositions.add (disposition);
			}
		}
		return truckDispositions;
	}


	public Disposition getDispositionById(int dispositionId){

		Optional<Disposition> disposition = dispositionRepository.findById (dispositionId);
		if(disposition.isPresent ())
			return  disposition.get ();
		return null;



	}


	public void loadDispositions () {


		dispositions = new ArrayList<Disposition> ();
		try {
			dispositions = dispositionRepository.findAll ();
			dispositions = dispositions.stream ().map (disposition -> {
				Carrier carrier = disposition.getCarrier ();
				if (carrier.getRole () instanceof External) {
					disposition.setDriver (null);
					disposition.setTruck (null);
				}
				return disposition;
			}).collect (Collectors.toList ());

		} catch (Exception e) {
			e.printStackTrace ();
		}


	}

	public List<Disposition> getDispositions () {

		return dispositions;
	}

	private void printList (ArrayList<Disposition> dispositions) {
		System.out.println ("LIST SIZE : " + dispositions.size ());
		for (int i = 0; i < dispositions.size (); i++) {
			System.out.println (dispositions.get (i).getId ());
		}

	}

	// TODO: create user defined exception.to handle integrity cases
	public void editExternalDisposition (Disposition disposition, Vertex origin, Vertex destination,
					     Carrier carrier, String startDate, String finishDate, double tarrif,
					     CustomerOrder order) {

		disposition.setCarrier (carrier);
		disposition.setStartDate (startDate);
		disposition.setFinishDate (finishDate);
		disposition.setTarrif (tarrif);
		disposition.setOrigin (origin);
		disposition.setDestination (destination);
		disposition.setTruck (null);
		disposition.setDriver (null);

		dispositionRepository.save (disposition);

	}

	public void editInternalDisposition (Disposition disposition, Vertex origin, Vertex destination,
					     Carrier carrier, String startDate, String finishDate, double tarrif,
					     CustomerOrder order, Truck truck, Driver driver) {

		String sql = "UPDATE Disposition SET  carrierId='" + carrier.getId () + "', startDate='" + startDate + "', "
			+ "finishDate='" + finishDate + "', tarrif='" + tarrif + "', customerOrderId='"
			+ "" + order.getId () + "', truckId='" + truck.getId () + "', "
			+ "driverId='" + driver.getId () + "', originAddressId='" + origin.getId () + "', "
			+ "destinationAddressId='" + destination.getId () + "' WHERE id='" + disposition.getId () + "';";
		disposition.setCarrier (carrier);
		disposition.setStartDate (startDate);
		disposition.setFinishDate (finishDate);
		disposition.setTarrif (tarrif);
		disposition.setCustomerOrder (order);
		disposition.setTruck (truck);
		disposition.setDriver (driver);
		disposition.setOrigin (origin);
		disposition.setDestination (destination);

		dispositionRepository.save (disposition);

	}

	public void delete (Disposition disposition) {

		try {
			dispositionRepository.delete (disposition);
		} catch (Exception ex) {
			ex.printStackTrace ();
		}
		this.loadDispositions ();
	}

	public void calculateTarriff () {
		DirectionsService directionsService = new DirectionsService ();
		DirectionsRequest request = new DirectionsRequest ("Athens", "Italy", TravelModes.DRIVING);
		DirectionsRenderer directionsRenderer = new DirectionsRenderer ();
		directionsService.getRoute (request, this, directionsRenderer);
	}

	@Override
	public void directionsReceived (DirectionsResult dr, DirectionStatus ds) {
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}

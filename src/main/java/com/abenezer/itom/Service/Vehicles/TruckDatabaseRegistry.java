/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.Vehicles;

import com.abenezer.itom.Service.DispositionRecords.DispositionRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.controller.GraphRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CustomerOrders.Disposition;
import com.abenezer.itom.model.Vehicles.Truck;
import com.abenezer.itom.repository.TruckRepository;
import com.abenezer.itom.repository.VertexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class TruckDatabaseRegistry {


	private final TruckRepository truckRepository;

	private final VertexRepository vertexRepository;

	private final GraphRegistryDatabase graphDatabase;

	private final DispositionRegistryDatabase dispositionsDatabase;

	List<Truck> trucks = new ArrayList<Truck> ();

	@Autowired
	private TruckDatabaseRegistry (TruckRepository truckRepository, VertexRepository vertexRepository, GraphRegistryDatabase graphDatabase, DispositionRegistryDatabase dispositionsDatabase) {
		this.truckRepository = truckRepository;
		this.vertexRepository = vertexRepository;
		this.graphDatabase = graphDatabase;
		this.dispositionsDatabase = dispositionsDatabase;
		this.loadTrucks ();
	}


	public Truck addTruck (Truck truck) {

		return truckRepository.save (truck);
	}

	public Truck getTruckOfDisposition (int id) {
		Truck truck = null;
		if (trucks.isEmpty ()) {
			this.getTrucks ();
		}
		Iterator<Truck> it = trucks.iterator ();
		while (it.hasNext ()) {
			truck = it.next ();
			if (truck.getId () == id) {
				break;
			}
		}

		return truck;
	}

	private void loadTrucks () {
		trucks = truckRepository.findAll ();
	}

	public List<Truck> getTrucks () {

		this.cofigureTruckLocations ();
		return trucks;
	}

	public void cofigureTruckLocations () {
		for (Truck value : trucks) {
			try {
				Truck truck = value;
				ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck (truck.getId ());
				if (truckDispositions.isEmpty ()) {
					//nothing
					System.out.println (truck.getPlateNumber () + " HAS NO DISPOSITIONS");
				} else if (isCurrentlyExecutingDisposition (truck, truckDispositions)) {
					Disposition disposition = getCurrentDisposition (truck, truckDispositions);
					Vertex destination = disposition.getDestinationAddress ();
					truck.setAddress (destination);
					System.out.println (truck.getPlateNumber () + " IS CURRENTLY EXECUTING, ADDRESS: " + destination.getCity () + " origin" + disposition.getOriginAddress ().getCity ());
				} else if (hasPastDispositions (truck, truckDispositions)) {
					System.out.println (truck.getPlateNumber () + " IS CURRENTLY IS NOT EXECUTING. HE HAS PAST DISPO ");
					Disposition disposition = getLatestPastDisposition (truck, truckDispositions);
					Vertex destination = disposition.getDestinationAddress ();
					truck.setAddress (destination);
					System.out.println (truck.getPlateNumber () + " IS CURRENTLY EXECUTING, ADDRESS: " + destination.getCity ());
				} else {
					//nothing
				}
			} catch (ParseException ex) {
				Logger.getLogger (TruckDatabaseRegistry.class.getName ()).log (Level.SEVERE, null, ex);
			}

		}

	}

	private boolean isCurrentlyExecutingDisposition (Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException {
		boolean isExecuting = false;
		Date currentDate = this.getCurrentTime ();
		for (int i = 0; i < truckDispositions.size (); i++) {
			Disposition disposition = truckDispositions.get (i);
			Date dispoStartDate = this.convertDate (disposition.getStartDate ());
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				isExecuting = true;
				break;
			}
		}
		return isExecuting;
	}

	private boolean hasPastDispositions (Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		boolean hasPastDispositions = false;
		for (int i = 0; i < truckDispositions.size (); i++) {
			Disposition disposition = truckDispositions.get (i);
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoFinishDate)) {
				hasPastDispositions = true;
				//System.out.println("HAS PAST DISPO: ADDRESS"+disposition.getDestinationAddress().getCity());
				break;
			}
		}
		return hasPastDispositions;
	}

	private Disposition getCurrentDisposition (Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		Disposition currentDisposition = null;
		for (int i = 0; i < truckDispositions.size (); i++) {
			Disposition disposition = truckDispositions.get (i);
			Date dispoStartDate = this.convertDate (disposition.getStartDate ());
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				currentDisposition = disposition;
				break;
			}
		}
		return currentDisposition;
	}

	private Disposition getLatestPastDisposition (Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		Disposition latestPastDisposition = null;
		Date latestPastDate = veryPastDate ();
		for (int i = 0; i < truckDispositions.size (); i++) {
			Disposition disposition = truckDispositions.get (i);
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoFinishDate)) {
				//System.out.println("IN THE LATEST PAST DISPO LOOP");
				if (dispoFinishDate.after (latestPastDate)) {
					latestPastDate = dispoFinishDate;
					latestPastDisposition = disposition;
					//System.out.println("IN THE LATEST PAST DISPO LOOP BUG FREE");
				}
			}
		}
		return latestPastDisposition;
	}

	private Date veryPastDate () {
		final Calendar cal = Calendar.getInstance ();
		cal.add (Calendar.DATE, -1000);
		return cal.getTime ();
	}

	public ArrayList<Truck> getAvailableTrucks (Date newStart, Date newFinish, int vertexId)  {
		ArrayList<Truck> availableTrucks = new ArrayList<Truck> ();
		try {
			Vertex newDispositionLocation = vertexRepository.getOne (vertexId);
			this.getTrucks ();
			ArrayList<Truck> trucksNearBy = this.getTrucksNearBy (dispositionsDatabase, newDispositionLocation, newStart);
			for (int i = 0; i < trucksNearBy.size (); i++) {
				Truck truck = trucksNearBy.get (i);
				ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck (truck.getId ());
				boolean conflict = false;
				for (int j = 0; j < truckDispositions.size (); j++) {
					Disposition disposition = truckDispositions.get (j);
					Date dispoStartDate = this.convertDate (disposition.getStartDate ());
					Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());

					if (((dispoStartDate.before (newStart) || dispoStartDate.equals (newStart)) && (dispoFinishDate.after (newFinish) || dispoFinishDate.equals (newFinish))) ||
						((dispoStartDate.before (newStart) || dispoStartDate.equals (newStart)) && (dispoFinishDate.after (newStart) || dispoFinishDate.equals (newStart))) ||
						(dispoStartDate.after (newStart) && dispoStartDate.before (newFinish)) ||
						((dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish) || dispoStartDate.equals (newFinish)) &&
							(dispoFinishDate.before (newFinish) || dispoFinishDate.equals (newFinish)))) {
						conflict = true;
						break;
					}
				}
				if (!conflict) {
					availableTrucks.add (truck);
				}

			}
		} catch (ParseException e) {
			e.printStackTrace ();
		}
		return availableTrucks;
	}

	public ArrayList<Truck> getAvailableTrucks (Date newStart, Date newFinish, Disposition dispositionn, Vertex newVertexLocation) throws ParseException {
		this.getTrucks ();
		ArrayList<Truck> availableTrucks = new ArrayList<Truck> ();
		ArrayList<Truck> trucksNearBy = this.getTrucksNearBy (dispositionsDatabase, newVertexLocation, newStart);
		for (int i = 0; i < trucksNearBy.size (); i++) {
			Truck truck = trucksNearBy.get (i);
			ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck (truck.getId ());
			boolean conflict = false;
			for (int j = 0; j < truckDispositions.size (); j++) {
				Disposition disposition = truckDispositions.get (j);
				if (disposition == dispositionn) {
					conflict = false;
					System.out.println ("DISPOSITION : " + dispositionn.getId ());
				} else {
					Date dispoStartDate = this.convertDate (disposition.getStartDate ());
					Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
					if (((dispoStartDate.before (newStart) || dispoStartDate.equals (newStart)) && (dispoFinishDate.after (newFinish) || dispoFinishDate.equals (newFinish))) ||
						((dispoStartDate.before (newStart) || dispoStartDate.equals (newStart)) && (dispoFinishDate.after (newStart) || dispoFinishDate.equals (newStart))) ||
						(dispoStartDate.after (newStart) && dispoStartDate.before (newFinish)) ||
						((dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish) || dispoStartDate.equals (newFinish)) &&
							(dispoFinishDate.before (newFinish) || dispoFinishDate.equals (newFinish)))) {
						conflict = true;
						break;
					}
				}
			}
			if (!conflict) {
				availableTrucks.add (truck);
			}

		}
		return availableTrucks;
	}

	private ArrayList<Truck> getTrucksNearBy (DispositionRegistryDatabase instance,
						  Vertex newDispositionLocation, Date newStart) throws ParseException {
		ArrayList<Truck> trucksNearBy = new ArrayList<Truck> ();
		for (int i = 0; i < trucks.size (); i++) {
			Truck truck = trucks.get (i);
			ArrayList<Disposition> truckDispositions =
				instance.getDispositionsOfTruck (truck.getId ());
			Vertex truckLocation = null;
			if (truckDispositions.isEmpty () || getLatestDispositionAssigned (truckDispositions, newStart) == null)
				truckLocation = truck.getAddress ();
			else {
				Disposition latestOrder = getLatestDispositionAssigned (truckDispositions, newStart);
				truckLocation = latestOrder.getDestinationAddress ();
			}
			if (newDispositionLocation == truckLocation) {
				System.out.println (newDispositionLocation.getCity () + " " + truckLocation.getCity ());
				trucksNearBy.add (truck);
			}


		}
		return trucksNearBy;
	}


	private Disposition getLatestDispositionAssigned (ArrayList<Disposition> truckDispositions, Date startDate) throws ParseException {

		ArrayList<Disposition> latestDispositions = new ArrayList<Disposition> ();
		for (int i = 0; i < truckDispositions.size (); i++) {
			Disposition disposition = truckDispositions.get (i);
			Date current = this.convertDate (disposition.getFinishDate ());
			if (current.before (startDate)) {
				latestDispositions.add (disposition);
			}
		}

		Disposition latestOrder = null;
		Date latestDate = this.veryPastDate ();
		for (int i = 0; i < latestDispositions.size (); i++) {
			Disposition disposition = latestDispositions.get (i);
			Date current = this.convertDate (disposition.getFinishDate ());
			if (current.after (latestDate)) {
				latestDate = current;
				latestOrder = disposition;
			}
		}
		return latestOrder;
	}

	private Date getCurrentTime () {
		Date todayDate = Calendar.getInstance ().getTime ();
		return todayDate;
	}

	private Date convertDate (String dat) throws ParseException {
		DateFormat format = new SimpleDateFormat ("yyyy-MM-dd", Locale.ENGLISH);
		Date date = format.parse (dat);
		return date;
	}

	public void editTruck (Truck truck, Vertex address, boolean availabilityStatus, String plateNumber,
			       boolean conditionStatus) {

		truck.setAvailability (availabilityStatus);
		truck.setPlateNumber (plateNumber);
		truck.setCondition (conditionStatus);
		truck.setAddress (address);
		truckRepository.save (truck);
	}

	public Truck editTruck (Truck truck) {

		return truckRepository.save (truck);
	}

	public void delete (Truck truck) {
		trucks.remove (truck);
		truckRepository.delete (truck);

	}

	public void deleteID (int truckId) {
		trucks.removeIf (truck->truck.getId ()==truckId);
		truckRepository.deleteById (truckId);

	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.Drivers;

import com.abenezer.itom.Service.DispositionRecords.DispositionRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CustomerOrders.Disposition;
import com.abenezer.itom.model.Drivers.Driver;
import com.abenezer.itom.repository.DriverRepository;
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
public class DriversDatabaseRegistry {


	private final DispositionRegistryDatabase dispositionsDatabase;

	private final DriverRepository driverRepository;

	private List<Driver> drivers = new ArrayList<> ();


	@Autowired
	private DriversDatabaseRegistry (DispositionRegistryDatabase dispositionsDatabase, DriverRepository driverRepository) {
		this.dispositionsDatabase = dispositionsDatabase;
		this.driverRepository = driverRepository;
		this.loadDrivers ();
	}


	public Driver addDriver (Driver driver) {

		drivers.add (driver);
		return driverRepository.save (driver);

	}


	public Driver getDriverById (int id) {
		Driver driver = null;
		if (drivers.isEmpty ()) {
			this.getDrivers ();
		}
		for (Driver value : drivers) {
			driver = value;
			if (driver.getId () == id) {
				break;
			}
		}

		return driver;
	}

	private void loadDrivers () {
		drivers = driverRepository.findAll ();
	}


	public List<Driver> getDrivers () {

		this.cofigureDriverLocations ();
		return drivers;
	}

	public void cofigureDriverLocations () {
		for (Driver value : drivers) {
			try {
				ArrayList<Disposition> driverDispositions = dispositionsDatabase.getDispositionsOfDriver (value.getId ());

				if (driverDispositions.isEmpty ()) {
					//nothing
					//System.out.println(driver.getFirstName()+" HAS NO DISPOSITIONS");
				} else if (isCurrentlyExecutingDisposition (value, driverDispositions)) {
					Disposition disposition = getCurrentDisposition (value, driverDispositions);
					Vertex destination = disposition.getDestinationAddress ();
					value.setAddress (destination);
					//System.out.println(driver.getFirstName()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity());
				} else if (hasPastDispositions (value, driverDispositions)) {
					//System.out.println(driver.getFirstName()+" IS CURRENTLY IS NOT EXECUTING. HE HAS PAST DISPO ");
					Disposition disposition = getLatestPastDisposition (value, driverDispositions);
					Vertex destination = disposition.getDestinationAddress ();
					value.setAddress (destination);
					//System.out.println(driver.getFirstName()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity());
				} else {
					//nothing
				}
			} catch (ParseException ex) {
				Logger.getLogger (DriversDatabaseRegistry.class.getName ()).log (Level.SEVERE, null, ex);
			}

		}

	}

	private boolean isCurrentlyExecutingDisposition (Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException {
		boolean isExecuting = false;
		Date currentDate = this.getCurrentTime ();
		for (int i = 0; i < driverDispositions.size (); i++) {
			Disposition disposition = driverDispositions.get (i);
			Date dispoStartDate = this.convertDate (disposition.getStartDate ());
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				isExecuting = true;
				break;
			}
		}
		return isExecuting;
	}

	private boolean hasPastDispositions (Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		boolean hasPastDispositions = false;
		for (int i = 0; i < driverDispositions.size (); i++) {
			Disposition disposition = driverDispositions.get (i);
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoFinishDate)) {
				hasPastDispositions = true;
				//System.out.println("HAS PAST DISPO: ADDRESS"+disposition.getDestinationAddress().getCity());
				break;
			}
		}
		return hasPastDispositions;
	}

	private Disposition getCurrentDisposition (Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		Disposition currentDisposition = null;
		for (int i = 0; i < driverDispositions.size (); i++) {
			Disposition disposition = driverDispositions.get (i);
			Date dispoStartDate = this.convertDate (disposition.getStartDate ());
			Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				currentDisposition = disposition;
				break;
			}
		}
		return currentDisposition;
	}

	private Disposition getLatestPastDisposition (Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		Disposition latestPastDisposition = null;
		Date latestPastDate = veryPastDate ();
		for (int i = 0; i < driverDispositions.size (); i++) {
			Disposition disposition = driverDispositions.get (i);
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

	public ArrayList<Driver> getAvailableDrivers (Date newStart, Date newFinish, int dispositionId) {
		Disposition dispositionn = dispositionsDatabase.getDispositionById (dispositionId);
		ArrayList<Driver> availableDrivers = new ArrayList<Driver> ();
		try {
			this.getDrivers ();
			ArrayList<Driver> driversNearBy = this.getDriversNearBy (Integer.parseInt (dispositionn.getOriginAddress ().getId ()), newStart);
			for (int i = 0; i < driversNearBy.size (); i++) {
				Driver driver = driversNearBy.get (i);
				ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfDriver (driver.getId ());
				boolean conflict = false;
				for (int j = 0; j < truckDispositions.size (); j++) {
					Disposition disposition = truckDispositions.get (j);
					if (disposition == dispositionn) {
						conflict = false;
						System.out.println ("DISPOSITION  :" + dispositionn.getId ());
					} else {
						Date dispoStartDate = this.convertDate (disposition.getStartDate ());
						Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
						if (((dispoStartDate.before (newStart) || (dispoStartDate.equals (newStart))) && ((dispoFinishDate.after (newFinish)) || dispoFinishDate.equals (newFinish))) ||
							((dispoStartDate.before (newStart) || (dispoStartDate.equals (newStart))) && (dispoFinishDate.after (newStart) || dispoFinishDate.equals (newStart))) ||
							((dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish)) || dispoStartDate.equals (newFinish)) ||
							(dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish) || dispoStartDate.equals (newFinish)) &&
								(dispoFinishDate.before (newFinish) || dispoFinishDate.equals (newFinish))) {
							conflict = true;
							break;
						}
					}

				}
				if (!conflict) {
					availableDrivers.add (driver);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace ();
		}
		return availableDrivers;
	}

	public ArrayList<Driver> getAvailableDrivers (Date newStart, Date newFinish, Vertex newDispositionLocation) throws ParseException {
		this.getDrivers ();
		ArrayList<Driver> availableDrivers = new ArrayList<Driver> ();
		ArrayList<Driver> driversNearBy = this.getDriversNearBy (Integer.parseInt (newDispositionLocation.getId ()), newStart);
		for (int i = 0; i < driversNearBy.size (); i++) {
			Driver driver = driversNearBy.get (i);
			ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfDriver (driver.getId ());
			boolean conflict = false;
			for (int j = 0; j < truckDispositions.size (); j++) {
				Disposition disposition = truckDispositions.get (j);
				Date dispoStartDate = this.convertDate (disposition.getStartDate ());
				Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
				if (((dispoStartDate.before (newStart) || (dispoStartDate.equals (newStart))) && ((dispoFinishDate.after (newFinish)) || dispoFinishDate.equals (newFinish))) ||
					((dispoStartDate.before (newStart) || (dispoStartDate.equals (newStart))) && (dispoFinishDate.after (newStart) || dispoFinishDate.equals (newStart))) ||
					((dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish)) || dispoStartDate.equals (newFinish)) ||
					(dispoStartDate.after (newStart) || dispoStartDate.equals (newStart)) && (dispoStartDate.before (newFinish) || dispoStartDate.equals (newFinish)) &&
						(dispoFinishDate.before (newFinish) || dispoFinishDate.equals (newFinish))) {
					conflict = true;
					break;
				}

			}
			if (!conflict) {
				availableDrivers.add (driver);
			}
		}
		return availableDrivers;
	}

	public ArrayList<Driver> getDriversNearBy (int newDispositionLocation, Date newStart) {
		ArrayList<Driver> driversNearBy = new ArrayList<> ();
		try {
			for (int i = 0; i < drivers.size (); i++) {
				Driver driver = drivers.get (i);
				ArrayList<Disposition> driverDispositions =
					dispositionsDatabase.getDispositionsOfTruck (driver.getId ());
				Vertex driverLocation = null;
				if (driverDispositions.isEmpty () || getLatestDispositionAssigned (driverDispositions, newStart) == null)
					driverLocation = driver.getAddress ();
				else {
					Disposition latestOrder = getLatestDispositionAssigned (driverDispositions, newStart);
					driverLocation = latestOrder.getDestinationAddress ();
				}
				if (driverLocation.getId ().equals (newDispositionLocation))
					driversNearBy.add (driver);

			}
		} catch (ParseException e) {
			e.printStackTrace ();
		} catch (NumberFormatException e) {
			e.printStackTrace ();
		}
		return driversNearBy;
	}

	private Disposition getLatestDispositionAssigned (ArrayList<Disposition> driverDispositions, Date startDate) throws ParseException {
		ArrayList<Disposition> latestDispositions = new ArrayList<Disposition> ();
		for (int i = 0; i < driverDispositions.size (); i++) {
			Disposition disposition = driverDispositions.get (i);
			Date current = this.convertDate (disposition.getFinishDate ());
			if (current.before (startDate)) {
				latestDispositions.add (disposition);
			}
		}
		Disposition latestDisposition = null;
		Date latestDate = this.veryPastDate ();
		for (int i = 0; i < latestDispositions.size (); i++) {
			Disposition disposition = latestDispositions.get (i);
			Date current = this.convertDate (disposition.getFinishDate ());
			if (current.after (latestDate)) {
				latestDate = current;
				latestDisposition = disposition;
			}
		}
		return latestDisposition;
	}

	private Date getCurrentTime () {
		return Calendar.getInstance ().getTime ();
	}

	private Date convertDate (String dat) throws ParseException {
		DateFormat format = new SimpleDateFormat ("yyyy-MM-dd", Locale.ENGLISH);
		Date date = format.parse (dat);
		return date;
	}


	// TODO: create user defined exception.to handle integrity cases
	public Driver editDriver (Driver driver) {
		return driverRepository.save (driver);

	}

	public void delete (Driver driver) {
		driverRepository.delete (driver);
		drivers.remove (driver);
	}

	public void delete (int driverId) {
		driverRepository.deleteById (driverId);
		drivers.removeIf (driver -> driver.getId () == driverId);
	}
}

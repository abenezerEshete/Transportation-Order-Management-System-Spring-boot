/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.CustomerOrders;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.Drivers.Driver;
import com.abenezer.itom.model.Vehicles.Truck;
import com.abenezer.itom.model.Vehicles.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author AbenezerEsheteTilahu
 */
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "disposition")
public class Disposition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@OneToOne
	Carrier carrier;

	@OneToOne
	CustomerOrder order;

	@OneToOne
	Vertex originAddress;

	@OneToOne
	Vertex destinationAddress;

	@Column(name = "startdate", nullable = false)
	String startDate;

	@Column(name = "finishdate", nullable = false)
	String finishDate;

	@Column(name = "tarrif", nullable = false)
	double  tarrif;

	@OneToOne
	Truck truck;

	@OneToOne
	Driver  driver;

	public Disposition () {
	}

	public Disposition (int id, Carrier carrier, Vertex originAddress, Vertex destinationAddress,
			    String startDate, String finishDate, double tarrif, CustomerOrder order) {
		this.id = id;
		this.carrier = carrier;
		this.originAddress = originAddress;
		this.destinationAddress = destinationAddress;
		this.order = order;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.tarrif = tarrif;

	}

	public Disposition (int id, Carrier carrier, Vertex originAddress, Vertex destinationAddress, String startDate, String finishDate,
			    double tarrif, CustomerOrder order, Driver driver, Vehicle truck) {
		this.id = id;
		this.carrier = carrier;
		this.originAddress = originAddress;
		this.destinationAddress = destinationAddress;
		this.order = order;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.tarrif = tarrif;
		this.driver = driver;
		this.truck = (Truck) truck;


	}

	public Disposition (Carrier carrier, Vertex origin, Vertex destination, String startDate, String finishDate, double tarrif, CustomerOrder order) {
		this.carrier = carrier;
		this.originAddress = origin;
		this.destinationAddress = destination;
		this.order = order;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.tarrif = tarrif;
	}

	public Disposition (Carrier carrier, Vertex origin, Vertex destination, String startDate, String finishDate, double tarrif, CustomerOrder order, Driver driver, Truck truck) {
		this.carrier = carrier;
		this.originAddress = origin;
		this.destinationAddress = destination;
		this.order = order;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.tarrif = tarrif;
		this.driver = driver;
		this.truck = truck;

	}

	public Vehicle getTruck () {
		return this.truck;
	}

	public String getTruckPlateNumber () {
		if (truck == null) {
			return "N/A";
		} else {
			return this.truck.getPlateNumber ();
		}
	}

	public void setTruck (Vehicle truck) {
		this.truck = (Truck) truck;

	}

	public Driver getDriver () {
		return this.driver;
	}

	public String getDriverId () {
		if (driver == null) {
			return "N/A";
		} else {
			return this.driver.getId () + "";
		}

	}

	public String getDriverSurname () {
		if (driver == null) {
			return "N/A";
		} else {
			return this.driver.getLastName ();
		}

	}

	public void setDriver (Driver driver) {
		this.driver = driver;
	}


	public Vertex getOriginAddress () {
		return this.originAddress;
	}


	public Vertex getDestinationAddress () {
		return this.destinationAddress;
	}


	public int getId () {
		return this.id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public Carrier getCarrier () {
		return this.carrier;
	}

	public void setCarrier (Carrier carrier) {
		this.carrier = carrier;
	}

	public String getCarrierRole () {
		return this.carrier.getCarrierRole ();
	}

	public CustomerOrder getCustomerOrder () {
		return this.order;
	}

	public void setCustomerOrder (CustomerOrder order) {
		this.order = order;
	}

	public String getCarrierCompany () {
		return this.carrier.getCompany ();
	}

	public String getCarrierMode () {
		return this.carrier.getCarrierMode ();
	}

	public String getOrigin () {
		return this.originAddress.getCity () + ", " + this.originAddress.getCountry ();
	}

	public void setOrigin (Vertex origin) {
		this.originAddress = origin;
	}

	public String getDestination () {
		return this.destinationAddress.getCity () + ", " + this.destinationAddress.getCountry ();
	}

	public void setDestination (Vertex destination) {
		this.destinationAddress = destination;
	}

	public String getStartDate () {
		return this.startDate;
	}

	public void setStartDate (String date) {
		this.startDate = date;
	}

	public String getFinishDate () {
		return this.finishDate;
	}

	public void setFinishDate (String date) {
		this.finishDate = date;
	}

	public double getTarrif () {
		return this.tarrif;
	}

	public void setTarrif (double tarrif) {
		this.tarrif = tarrif;
	}


}

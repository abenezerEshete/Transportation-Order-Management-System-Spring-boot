/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.Vehicles;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author EfthymiosChatziathanasiadis
 */
@AllArgsConstructor
@MappedSuperclass
public class Vehicle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "availabilityStatus", nullable = false)
	private boolean availabilityStatus;

	@Column(name = "plateNumber", nullable = false)
	private String plateNumber;

	@Column(name = "conditionStatus", nullable = false)
	private boolean conditionStatus;

	@OneToOne
	private Vertex address;

	public Vehicle () {
	}


	public Vehicle (int id, boolean availabilityStatus, String plateNumber, boolean conditionStatus, int addressID) {
		this.id = id;
		this.availabilityStatus = availabilityStatus;
		this.plateNumber = plateNumber;
		this.conditionStatus = conditionStatus;
		this.address.setId (addressID);
	}


	public Vehicle (boolean availabilityStatus, String plateNumber, boolean conditionStatus) {

		this.availabilityStatus = availabilityStatus;
		this.plateNumber = plateNumber;
		this.conditionStatus = conditionStatus;
	}

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public String getLocation () {
		return address != null? (this.address.getCity () + ", " + this.address.getCountry ()) : "";
	}

	public Vertex getAddress () {
		return this.address;
	}

	public void setAddress (Vertex address) {
		this.address = address;
	}

	public boolean getAvailabilityStatus () {
		return this.availabilityStatus;
	}

	public String getAvailability () {
		if (availabilityStatus) {
			return "Available";
		} else {
			return "Unavailable";
		}
	}

	public void setAvailability (boolean status) {
		this.availabilityStatus = status;
	}

	public String getPlateNumber () {
		return this.plateNumber;
	}

	public void setPlateNumber (String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public boolean getConditionStatus () {
		return this.conditionStatus;
	}

	public String getCondition () {
		if (conditionStatus) {
			return "Damaged";
		} else {
			return "Not Damaged";
		}
	}

	public void setCondition (boolean status) {
		this.conditionStatus = status;
	}

	public String toString () {
		return this.plateNumber;
	}


	public boolean isAvailabilityStatus () {
		return availabilityStatus;
	}

	public static long getSerialVersionUID () {
		return serialVersionUID;
	}

	public void setAvailabilityStatus (boolean availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public boolean isConditionStatus () {
		return conditionStatus;
	}

	public void setConditionStatus (boolean conditionStatus) {
		this.conditionStatus = conditionStatus;
	}
}

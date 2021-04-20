/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.Drivers;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author AbenezerEsheteTilahu
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "driver")
public class Driver implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "availabilityStatus", nullable = false)
	private boolean availabilityStatus;

	@Column(name = "firstName", nullable = false)
	private String firstName;

	@Column(name = "lastName", nullable = false)
	private String lastName;

	@OneToOne
	private Vertex address;

	public Driver (int id, String firstName, String lastName, boolean availabilityStatus, Vertex address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.availabilityStatus = availabilityStatus;
		this.address = address;
	}

	public Driver (String firstName, String lastName, boolean availabilityStatus, Vertex address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.availabilityStatus = availabilityStatus;
		this.address = address;
	}

	public int getId () {
		return this.id;
	}

	public Vertex getAddress () {
		return this.address;
	}


	public void setAvailabilityStatus (boolean availability) {
		this.availabilityStatus = availability;
	}


	public String getLastName () {
		return this.lastName;
	}


	@Override
	public String toString () {
		return this.lastName + ", " + this.firstName;
	}

	public void setAddress (Vertex address) {
		this.address = address;
	}
}

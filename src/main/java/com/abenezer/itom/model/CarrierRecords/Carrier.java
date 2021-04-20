/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.CarrierRecords;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author AbenezerEsheteTilahu
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "carrier")
public class Carrier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "company", nullable = false)
	private String company;

//	@Column(name = "role", nullable = false)
//	private int role = 0;

	@Column(name = "mode", nullable = false)
	private String mode;


	public Carrier (String company, CarrierRole role, Mode mode) {
		this.company = company;
		//	this.role =(role instanceof Internal?0:1);
		this.mode = mode.toString ();
	}

	public Carrier (int id, String company, Mode mode, CarrierRole role) {
		this.id = id;
		this.company = company;
		this.mode = mode.toString ();
		//this.role = (role instanceof Internal?0:1);
	}

	public CarrierRole getRole () {
		//Object o = this.role;
		return null;//(CarrierRole) o;
	}

	public String getCarrierRole () {
		//	Object o = this.role;
//		CarrierRole carrierRole = (CarrierRole) o;
//		if (carrierRole instanceof Internal) return "Internal";
		//else
		return "External";
	}

	public void setRole (CarrierRole role) {

		//this.role = (role instanceof Internal?0:1);
	}

	public int getId () {
		return this.id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public String getCompany () {
		return this.company;
	}

	public void setCompany (String company) {
		this.company = company;
	}

	public Mode getMode () {
		Mode o = null;


		return o;
	}

	public String getCarrierMode () {

		Mode m = null;
		if(mode == null) return null;
		if ("Road".equalsIgnoreCase (mode.toString ()))
			m = new Road ();
		if ("Sea".equalsIgnoreCase (mode.toString ()))
			m = new Sea ();
		if ("Rail".equalsIgnoreCase (mode.toString ()))
			m = new Rail ();


		if (m instanceof Road) {
			return "Road";
		} else if (m instanceof Sea) {
			return "Sea";
		} else {
			return "Rail";
		}

	}

	public void setMode (Mode mode) {
		this.mode = mode.toString ();
	}

	@Override
	public String toString () {
		return this.company + ", " + this.getCarrierMode ();
	}


}

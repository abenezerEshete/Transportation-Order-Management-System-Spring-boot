/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.algorithms.dijkstra.model;

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
@Entity(name = "vertex")
public class Vertex implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "lat", nullable = false)
	private String lat;

	@Column(name = "lon", nullable = false)
	private String lon;


	public Vertex (String id, String country, String city, double lat, double lon) {
		this.id = Integer.parseInt (id);
		this.country = country;
		this.city = city;
		this.lat = lat + "";
		this.lon = lon + "";
	}

	public Vertex (int id) {
		this.id = id;
	}
/*	public Vertex () {
	//	throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}*/

	public Vertex (String country, String city, double lat, double lon) {
		this.country = country;
		this.city = city;
		this.lat = lat + "";
		this.lon = lon + "";
	}

	public int getId () {
		return id;
	}

	public String getCountry () {
		return this.country;
	}

	public String getCity () {
		return this.city;
	}


	public double latitude () {
		return Double.parseDouble (this.lat);
	}

	public String getLat () {
		return lat;
	}

	public String getLon () {
		return lon;
	}

	public double longitude () {

		try {
			return Double.parseDouble (this.lon);
		} catch (NumberFormatException e) {

			e.printStackTrace ();
			return 0.0;
		}
	}



	@Override
	public int hashCode () {
		return id;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass () != obj.getClass ())
			return false;
		Vertex other = (Vertex) obj;
		if (id == 0) {
			if (other.id != 0)
				return false;
		} else if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString () {
		return id + "," + this.city + ", " + this.country;
	}

}

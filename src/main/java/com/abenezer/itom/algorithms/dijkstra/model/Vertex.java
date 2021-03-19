/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.algorithms.dijkstra.model;

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
@Entity(name = "vertex")
public class Vertex implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

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

	public Vertex (Integer id) {
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

	public String getId () {
		return id + "";
	}


	public String getCountry () {
		return this.country;
	}

	public String getCity () {
		return this.city;
	}


	public double getLat () {
		return Double.parseDouble (this.lat);
	}

	public String getLatitude () {
		return this.lat;
	}

	public double getLong () {
		return Double.parseDouble (this.lon);
	}

	public String getLongt () {
		return this.lon + "";
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode ());
		return result;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals (other.id))
			return false;
		return true;
	}

	@Override
	public String toString () {
		return this.city + ", " + this.country;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.worldCountryRecords;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.world.City;
import com.abenezer.itom.model.world.Country;
import com.abenezer.itom.repository.VertexRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AbenezerEsheteTilahu
 */
public class WorldRegistryDatabase {


	private final VertexRepository vertexRepository;

	private List<Country> countries;

	private List<Vertex> locations;

	@Autowired
	private WorldRegistryDatabase (VertexRepository vertexRepository) {
		this.vertexRepository = vertexRepository;
		locations = vertexRepository.findAll ();
	}


	public List<Country> getCountries () {
		if (countries.isEmpty ()) {
			countries = locations
				.stream ()
				.map (loc -> new Country (loc.getCountry ()))
				.collect (Collectors.toList ());
		}
		return countries;
	}

	public List<City> getCitiesWhereCountry (Country country) {

		// get cities in the country
		List<City> cities = new ArrayList<> ();
		try {
			cities = locations
				.stream ()
				.filter (loc -> loc.getCountry ().equalsIgnoreCase (country.getCountry ()))
				.map (loc -> new City (loc.getCity (), loc.getLat (), loc.getLon (), country))
				.collect (Collectors.toList ());
		} catch (Exception e) {
			e.printStackTrace ();
		}


		return cities;
	}


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.algorithms.dijkstra.controller;

import com.abenezer.itom.algorithms.dijkstra.model.Edge;
import com.abenezer.itom.algorithms.dijkstra.model.Graph;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.repository.EdgeRepository;
import com.abenezer.itom.repository.VertexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EfthymiosChatziathanasiadis
 */
@Service
public class GraphRegistryDatabase {


	final VertexRepository vertexRepository;

	final EdgeRepository edgeRepository;

	private List<Vertex> nodes = new ArrayList<> ();
	List<Edge> edges = new ArrayList<> ();

	@Autowired
	public GraphRegistryDatabase (VertexRepository vertexRepository, EdgeRepository edgeRepository) {
		this.vertexRepository = vertexRepository;
		this.edgeRepository = edgeRepository;
		this.getVertices ();
		this.getEdges ();
	}


	public Graph getGraph () {
		if (nodes.isEmpty ()) this.getVertices ();

		if (edges.isEmpty ()) this.getEdges ();

		return new Graph (nodes, edges);
	}


	public List<Vertex> getVertices () {
		nodes =  vertexRepository.findAll ();
		return nodes;
	}

	public List<Edge> getEdges () {
		return this.edges;
	}

	public Edge getEdgeById (int edgeId) {
		for (Edge edge : edges) {
			if (edge.getId ().equals (edgeId + ""))
				return edge;
		}
		return null;
	}

	public Vertex addVertex (Vertex vertex) {

		if (!exists (vertex.getCountry (), vertex.getCity ())) {
			nodes.add (vertex);
			return vertexRepository.save (vertex);
		}
		return null;

	}

	private boolean exists (String newCountry, String newCity) {
		boolean exists = false;
		for (Vertex node : nodes) {
			String city = node.getCity ();
			String country = node.getCountry ();
			if (newCountry  != null && newCity != null && newCountry.equals (country) && newCity.equals (city)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	/*public void editVertex(Vertex vertex, String country, String city, String lat, String lon){

	}*/
	public Edge addEdge (Edge edge) {
		double sourceLat = edge.getSource ().latitude ();
		double destinationLat = edge.getDestination ().latitude ();
		double sourceLon = edge.getSource ().longitude ();
		double destinationLon = edge.getDestination ().longitude ();
		double weight = this.computeWeight (sourceLat, destinationLat, sourceLon, destinationLon, 0.0, 0.0);

		if (!edgeAlreadyExists (edge.getSource (), edge.getDestination ())) {
			try {
				edges.add (edge);
				return edgeRepository.save (edge);
			} catch (Exception ex) {
				ex.printStackTrace ();
			}
		}
		return null;

	}

	private boolean edgeAlreadyExists (Vertex newSource, Vertex newDestination) {
		boolean exists = false;
		for (int i = 0; i < edges.size (); i++) {
			Edge edge = edges.get (i);
			Vertex source = edge.getSource ();
			Vertex destination = edge.getDestination ();
			if ((source == newSource) && (destination == newDestination)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public Edge editEdge (Edge edge) {
		double sourceLat = edge.getSource ().latitude ();
		double destinationLat = edge.getDestination ().latitude ();
		double sourceLon = edge.getSource ().longitude ();
		double destinationLon = edge.getDestination ().longitude ();
		double weight = this.computeWeight (sourceLat, destinationLat, sourceLon, destinationLon, 0.0, 0.0);
		return edgeRepository.save (edge);
	}

	public Vertex editVertex (Vertex vertex) {
		if (!exists (vertex.getCountry (), vertex.getCity ())) {
			nodes.add (vertex);
			return vertexRepository.save (vertex);
		}
		return null;
	}

	public void deleteEdge (int edgeId) {
		edgeRepository.deleteById (edgeId);
	}

	public void deleteVertex (int vertexId) {
		vertexRepository.deleteById (vertexId);
	}

	public Vertex getVertexById (int id) {
		Vertex vertex = null;
		for (Vertex node : nodes) {
			if (vertex.getId () == (id)) {
				vertex = node;
				break;
			}
		}
		return vertex;
	}

	/**
	 * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * <p>
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 *
	 * @returns Distance in Meters
	 */
	public static double computeWeight (double lat1, double lat2, double lon1,
					    double lon2, double el1, double el2) {

		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians (lat2 - lat1);
		double lonDistance = Math.toRadians (lon2 - lon1);
		double a = Math.sin (latDistance / 2) * Math.sin (latDistance / 2)
			+ Math.cos (Math.toRadians (lat1)) * Math.cos (Math.toRadians (lat2))
			* Math.sin (lonDistance / 2) * Math.sin (lonDistance / 2);
		double c = 2 * Math.atan2 (Math.sqrt (a), Math.sqrt (1 - a));
		double distance = R * c * 1000; // convert to meters

		double height = el1 - el2;

		distance = Math.pow (distance, 2) + Math.pow (height, 2);

		return Math.sqrt (distance);
	}


}
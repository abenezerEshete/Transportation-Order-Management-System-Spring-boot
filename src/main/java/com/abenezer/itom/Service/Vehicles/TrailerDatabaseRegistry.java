/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.Vehicles;

import com.abenezer.itom.Service.CustomerOrders.CustomerOrderRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
import com.abenezer.itom.model.Vehicles.Trailer;
import com.abenezer.itom.repository.TrailerRepository;
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
public class TrailerDatabaseRegistry {


	private final TrailerRepository trailerRepository;

	private final VertexRepository vertexRepository;

	private final CustomerOrderRegistryDatabase orderDatabase;

	private List<Trailer> trailers = new ArrayList<> ();

	private TrailerDatabaseRegistry (CustomerOrderRegistryDatabase orderDatabase, TrailerRepository trailerRepository, VertexRepository vertexRepository) {
		this.orderDatabase = orderDatabase;
		this.trailerRepository = trailerRepository;
		this.vertexRepository = vertexRepository;
		this.loadTrailers ();
	}


	public Trailer addTrailer (Trailer trailer) {
		trailers.add (trailer);
		return trailerRepository.save (trailer);
	}

	public Trailer getTrailerOfOrder (int id) {
		Trailer trailer = null;
		if (trailers.isEmpty ()) {
			this.getTrailers ();
		}
		for (Trailer value : trailers) {
			if (trailer.getId () == id) {
				trailer = value;
				break;
			}
		}

		return trailer;
	}

	private void loadTrailers () {
		if (trailers.isEmpty ()) {
			trailers = trailerRepository.findAll ();
		}
	}

	public List<Trailer> getTrailers () {
		this.cofigureTrailerLocations ();
		return trailers;
	}

	public void cofigureTrailerLocations () {
		for (Trailer value : trailers) {
			try {
				Trailer trailer = value;
				List<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer (trailer.getId ());
				if (trailerOrders.isEmpty ()) {
					//nothing
				} else if (isCurrentlyExecutingCustomerOrder (trailer, trailerOrders)) {
					CustomerOrder order = getCurrentCustomerOrder (trailer, trailerOrders);
					Vertex destination = order.getDestinationAddress ();
					trailer.setAddress (destination);
				} else if (hasPastCustomerOrder (trailer, trailerOrders)) {
					CustomerOrder disposition = getLatestPastCustomerOrder (trailer, trailerOrders);
					Vertex destination = disposition.getDestinationAddress ();
					trailer.setAddress (destination);
				} else {
					//nothing
				}
			} catch (ParseException ex) {
				Logger.getLogger (TrailerDatabaseRegistry.class.getName ()).log (Level.SEVERE, null, ex);
			}

		}

	}

	private boolean isCurrentlyExecutingCustomerOrder (Trailer driver, List<CustomerOrder> trailerCustomerOrders) throws ParseException {
		boolean isExecuting = false;
		Date currentDate = this.getCurrentTime ();
		for (CustomerOrder disposition : trailerCustomerOrders) {
			Date dispoStartDate = this.convertDate (disposition.getOrderDate ());
			Date dispoFinishDate = this.convertDate (disposition.getDeliveryDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				isExecuting = true;
				break;
			}
		}
		return isExecuting;
	}

	private boolean hasPastCustomerOrder (Trailer trailer, List<CustomerOrder> trailerCustomerOrders) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		boolean hasPastDispositions = false;
		for (int i = 0; i < trailerCustomerOrders.size (); i++) {
			CustomerOrder order = trailerCustomerOrders.get (i);
			Date dispoFinishDate = this.convertDate (order.getDeliveryDate ());
			if (currentDate.after (dispoFinishDate)) {
				hasPastDispositions = true;
				break;
			}
		}
		return hasPastDispositions;
	}

	private CustomerOrder getCurrentCustomerOrder (Trailer trailer, List<CustomerOrder> trailerCustomerOrders) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		CustomerOrder currentOrder = null;
		for (int i = 0; i < trailerCustomerOrders.size (); i++) {
			CustomerOrder order = trailerCustomerOrders.get (i);
			Date dispoStartDate = this.convertDate (order.getOrderDate ());
			Date dispoFinishDate = this.convertDate (order.getDeliveryDate ());
			if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
				currentOrder = order;
				break;
			}
		}
		return currentOrder;
	}

	private CustomerOrder getLatestPastCustomerOrder (Trailer driver, List<CustomerOrder> trailerCustomerOrders) throws ParseException {
		Date currentDate = this.getCurrentTime ();
		CustomerOrder latestPastOrder = null;
		Date latestPastDate = veryPastDate ();
		for (int i = 0; i < trailerCustomerOrders.size (); i++) {
			CustomerOrder order = trailerCustomerOrders.get (i);
			Date dispoFinishDate = this.convertDate (order.getDeliveryDate ());
			if (currentDate.after (dispoFinishDate)) {
				if (dispoFinishDate.after (latestPastDate)) {
					latestPastDate = dispoFinishDate;
					latestPastOrder = order;
				}
			}
		}
		return latestPastOrder;
	}

	private Date veryPastDate () {
		final Calendar cal = Calendar.getInstance ();
		cal.add (Calendar.DATE, -1000);
		return cal.getTime ();
	}

	private Date getCurrentTime () {
		Date todayDate = Calendar.getInstance ().getTime ();
		return todayDate;
	}

	public ArrayList<Trailer> getAvailableTrailers (Date newStart, Date newFinish,
							int vertexId) {

		ArrayList<Trailer> availableTrailers = new ArrayList<Trailer> ();
		try {
			Vertex newOrderLocation = vertexRepository.getOne (vertexId);
			this.getTrailers ();//load trailers
			ArrayList<Trailer> trailersNearBy = this.getTrailersNearBy (orderDatabase, newOrderLocation, newStart);
			for (int i = 0; i < trailersNearBy.size (); i++) {
				Trailer trailer = trailersNearBy.get (i);
				List<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer (trailer.getId ());
				boolean conflict = false;
				for (int j = 0; j < trailerOrders.size (); j++) {
					CustomerOrder order = trailerOrders.get (j);
					Date orderStartDate = this.convertDate (order.getOrderDate ());
					Date orderFinishDate = this.convertDate (order.getDeliveryDate ());
					if (((orderStartDate.before (newStart) || (orderStartDate.equals (newStart))) && ((orderFinishDate.after (newFinish)) || orderFinishDate.equals (newFinish))) ||
						((orderStartDate.before (newStart) || (orderStartDate.equals (newStart))) && (orderFinishDate.after (newStart) || orderFinishDate.equals (newStart))) ||
						((orderStartDate.after (newStart) || orderStartDate.equals (newStart)) && (orderStartDate.before (newFinish)) || orderStartDate.equals (newFinish)) ||
						(orderStartDate.after (newStart) || orderStartDate.equals (newStart)) && (orderStartDate.before (newFinish) || orderStartDate.equals (newFinish)) &&
							(orderFinishDate.before (newFinish) || orderFinishDate.equals (newFinish))) {
						conflict = true;
						break;
					}
				}
				if (!conflict) {
					availableTrailers.add (trailer);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace ();
		}
		return availableTrailers;
	}

	private Date convertDate (String dat) throws ParseException {
		DateFormat format = new SimpleDateFormat ("yyyy-MM-dd", Locale.ENGLISH);
		Date date = format.parse (dat);
		return date;
	}


	public Trailer editTrailer (Trailer trailer) {
		return trailerRepository.save (trailer);
	}

	public void delete (Trailer trailer) {
		trailerRepository.delete (trailer);
		trailers.remove (trailer);
	}

	public void delete (int trailerId) {
		trailerRepository.deleteById (trailerId);
		trailers.removeIf (trailer -> trailer.getId () == trailerId);
	}

	public ArrayList<Trailer> getAvailableTrailers (Date newStart, Date newFinish, CustomerOrder orderr, Vertex sourceVertex) throws ParseException {
		this.getTrailers ();//load trailers
		ArrayList<Trailer> availableTrailers = new ArrayList<Trailer> ();
		ArrayList<Trailer> trailersNearBy = getTrailersNearBy (orderDatabase, sourceVertex, newStart);
		for (int i = 0; i < trailersNearBy.size (); i++) {
			Trailer trailer = trailersNearBy.get (i);
			List<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer (trailer.getId ());
			boolean conflict = false;

			for (int j = 0; j < trailerOrders.size (); j++) {
				CustomerOrder order = trailerOrders.get (j);

				if (order == orderr) {
					conflict = false;
				} else {
					Date orderStartDate = this.convertDate (order.getOrderDate ());
					Date orderFinishDate = this.convertDate (order.getDeliveryDate ());
					if (((orderStartDate.before (newStart) || (orderStartDate.equals (newStart))) && ((orderFinishDate.after (newFinish)) || orderFinishDate.equals (newFinish))) ||
						((orderStartDate.before (newStart) || (orderStartDate.equals (newStart))) && (orderFinishDate.after (newStart) || orderFinishDate.equals (newStart))) ||
						((orderStartDate.after (newStart) || orderStartDate.equals (newStart)) && (orderStartDate.before (newFinish)) || orderStartDate.equals (newFinish)) ||
						(orderStartDate.after (newStart) || orderStartDate.equals (newStart)) && (orderStartDate.before (newFinish) || orderStartDate.equals (newFinish)) &&
							(orderFinishDate.before (newFinish) || orderFinishDate.equals (newFinish))) {
						conflict = true;
						break;
					}
				}
			}
			if (!conflict) {
				availableTrailers.add (trailer);
			}
		}
		return availableTrailers;
	}

	private ArrayList<Trailer> getTrailersNearBy (CustomerOrderRegistryDatabase instance,
						      Vertex newOrderLocation, Date startDate) throws ParseException {
		ArrayList<Trailer> trailersNearBy = new ArrayList<Trailer> ();
		for (int i = 0; i < trailers.size (); i++) {
			Trailer trailer = trailers.get (i);
			List<CustomerOrder> trailerOrders =
				instance.getCustomerOrdersOfTrailer (trailer.getId ());
			Vertex trailerLocation = null;
			if (trailerOrders.isEmpty () || getLatestOrderAssigned (trailerOrders, startDate) == null)
				trailerLocation = trailer.getAddress ();
			else {
				CustomerOrder latestOrder = getLatestOrderAssigned (trailerOrders, startDate);
				trailerLocation = latestOrder.getDestinationAddress ();
			}
			if (newOrderLocation == trailerLocation)
				trailersNearBy.add (trailer);

		}
		return trailersNearBy;
	}

	private CustomerOrder getLatestOrderAssigned (List<CustomerOrder> trailerOrders, Date startDate) throws ParseException {

		ArrayList<CustomerOrder> latestOrders = new ArrayList<CustomerOrder> ();
		for (int i = 0; i < trailerOrders.size (); i++) {
			CustomerOrder order = trailerOrders.get (i);
			Date current = this.convertDate (order.getDeliveryDate ());
			if (current.before (startDate)) {
				latestOrders.add (order);
			}
		}
		CustomerOrder latestOrder = null;
		Date latestDate = this.veryPastDate ();
		for (int i = 0; i < latestOrders.size (); i++) {
			CustomerOrder order = latestOrders.get (i);
			Date current = this.convertDate (order.getDeliveryDate ());
			if (current.after (latestDate)) {
				latestDate = current;
				latestOrder = order;
			}
		}
		return latestOrder;
	}
}

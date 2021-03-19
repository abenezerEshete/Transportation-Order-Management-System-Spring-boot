/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.CustomerOrders;

import com.abenezer.itom.Service.DispositionRecords.DispositionRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.CarrierRecords.Internal;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
import com.abenezer.itom.model.CustomerOrders.Disposition;
import com.abenezer.itom.model.CustomerRecords.Customer;
import com.abenezer.itom.model.Drivers.Driver;
import com.abenezer.itom.model.Vehicles.Trailer;
import com.abenezer.itom.model.Vehicles.Truck;
import com.abenezer.itom.model.systemUserRecords.SystemUser;
import com.abenezer.itom.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class CustomerOrderRegistryDatabase {

	private final CustomerOrderRepository customerorderRepository;

	private final DispositionRegistryDatabase dispositionDatabase;

	private List<CustomerOrder> customerOrders = new ArrayList<> ();


	@Autowired
	public CustomerOrderRegistryDatabase (CustomerOrderRepository customerorderRepository, DispositionRegistryDatabase dispositionDatabase) {
		this.customerorderRepository = customerorderRepository;
		this.dispositionDatabase = dispositionDatabase;
		this.loadOrders ();
	}


	public void configureAvailability () throws ParseException {
		for (int i = 0; i < customerOrders.size (); i++) {
			CustomerOrder order = customerOrders.get (i);
			Trailer trailer = order.getTrailer ();
			Date startDate = this.convertDate (order.getOrderDate ());
			Date finishDate = this.convertDate (order.getDeliveryDate ());
			Date currentDate = this.getCurrentTime ();
			if (currentDate.after (startDate) && currentDate.before (finishDate)) {
				//in between
				trailer.setAvailability (false);
			} else {
				trailer.setAvailability (true);
			}
			ArrayList<Disposition> orderDispositions = dispositionDatabase.getDispositionsOfOrder (order.getId ());
			for (int j = 0; j < orderDispositions.size (); j++) {
				Disposition disposition = orderDispositions.get (j);
				Carrier carrier = disposition.getCarrier ();
				if (carrier.getRole () instanceof Internal) {
					Driver driver = disposition.getDriver ();
					Truck truck = (Truck) disposition.getTruck ();
					Date dispoStartDate = this.convertDate (disposition.getStartDate ());
					Date dispoFinishDate = this.convertDate (disposition.getFinishDate ());
					if (currentDate.after (dispoStartDate) && currentDate.before (dispoFinishDate)) {
						driver.setAvailabilityStatus (false);
						truck.setAvailability (false);
					} else {
						driver.setAvailabilityStatus (true);
						truck.setAvailability (true);
					}
				}

			}
		}
	}

	private Date convertDate (String dat) throws ParseException {
		DateFormat format = new SimpleDateFormat ("yyyy-MM-dd", Locale.ENGLISH);
		Date date = format.parse (dat);
		return date;
	}

	private Date getCurrentTime () {
		Date todayDate = Calendar.getInstance ().getTime ();
		return todayDate;
	}

	public CustomerOrder addCustomerOrder (CustomerOrder customerOrder) {

		CustomerOrder order = customerorderRepository.save (customerOrder);
		customerOrders.add (order);
		return order;
	}


	public List<CustomerOrder> getCustomerOrdersOfTrailer (int id) {
		List<CustomerOrder> trailerCustomerOrders = new ArrayList<CustomerOrder> ();
		try {
			if (customerOrders.isEmpty ()) {
				this.getCustomerOrders ();
			}
			for (int i = 0; i < customerOrders.size (); i++) {
				CustomerOrder order = customerOrders.get (i);
				Trailer trailer = order.getTrailer ();
				if (trailer.getId () == id) {
					trailerCustomerOrders.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return trailerCustomerOrders;
	}

	public void loadOrders () {
		customerOrders = customerorderRepository.findAll ();
	}

	public List<CustomerOrder> getCustomerOrders () {
		return this.computeCustomerOrderStatus ();
	}


// TODO: create user defined exception.to handle integrity cases

	public void editCustomerOrder (CustomerOrder order,
				       Vertex origin, Vertex destination, String type,
				       String orderDate, String deliveryDate, double kg, Trailer trailer,
				       double tarrif, Customer customer, SystemUser disponent) {
		CustomerOrder order2 = new CustomerOrder (order.getId (), type, origin, destination, orderDate, deliveryDate, kg,
			trailer, tarrif, customer, disponent);
		customerorderRepository.save (order2);
		order = order2;
	}

	public CustomerOrder editCustomerOrder (CustomerOrder order) {

		return customerorderRepository.save (order);
	}


	public void delete (CustomerOrder order) {
		try {
			customerorderRepository.delete (order);
			this.loadOrders ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	public void deleteId (int customerOrderId) {
		try {
			customerorderRepository.deleteById (customerOrderId);
			this.loadOrders ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	//set status of each order i.e. routing complete or routing in progress
	public List<CustomerOrder> computeCustomerOrderStatus () {
		for (int i = 0; i < customerOrders.size (); i++) {
			CustomerOrder order = customerOrders.get (i);
			List<Disposition> dispositions = dispositionDatabase.
				getDispositionsOfOrder (order.getId ());
			if (dispositions.isEmpty ()) {
				order.setStatus ("IN PROGRESS");
			} else {
				Vertex orderOrigin = order.getOriginAddress ();
				Vertex orderDestination = order.getDestinationAddress ();
				if (dispositions.size () == 1) {
					Disposition disposition = dispositions.get (0);
					Vertex dispositionOrigin = disposition.getOriginAddress ();
					Vertex dispositionDestination = disposition.getDestinationAddress ();
					if (dispositionOrigin == orderOrigin &&
						dispositionDestination == orderDestination) {
						order.setStatus ("ROUTING COMPLETE");
					} else {
						order.setStatus ("IN PROGRESS");
					}

				} else {
					//check dispo.first = order.first
					//check for each dispo.first = dispo.prev.last
					//check dispo.last = order.last
					Disposition first = dispositions.get (0);
					Vertex dispositionOrigin = first.getOriginAddress ();

					if (dispositionOrigin == orderOrigin) {
						boolean inProgress = false;
						Vertex dispositionPrevDestination = first.getDestinationAddress ();
						for (int j = 1; j < dispositions.size (); j++) {
							Disposition current = dispositions.get (j);
							if (!(current.getOriginAddress () == dispositionPrevDestination)) {
								order.setStatus ("IN PROGRESS");
								inProgress = true;
								break;
							}
							dispositionPrevDestination = current.getDestinationAddress ();
						}
						Disposition last = dispositions.get (dispositions.size () - 1);
						if (last.getDestinationAddress () == orderDestination && !inProgress) {
							order.setStatus ("ROUTING COMPLETE");
						} else {
							order.setStatus ("IN PROGRESS");
						}

					} else {
						order.setStatus ("IN PROGRESS");
					}

				}
			}
		}
		return this.customerOrders;
	}

	public CustomerOrder getCustomerOrdersWhereId (int id) {
		CustomerOrder order = null;
		try {
			order = null;
			List<CustomerOrder> orders = this.getCustomerOrders ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				if (order.getId () == id) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return order;
	}

	public List<CustomerOrder> getCustomerOrdersWhereTrailerPlateNumber (String TrailerPlateNumber) {
		CustomerOrder order = null;
		List<CustomerOrder> ordersWithTrailer = null;
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			ordersWithTrailer = new ArrayList<CustomerOrder> ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				Trailer trailer = order.getTrailer ();
				String plateNumber = trailer.getPlateNumber ();
				if (plateNumber.equals (TrailerPlateNumber)) {
					ordersWithTrailer.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return ordersWithTrailer;
	}

	public List<CustomerOrder> getCustomerOrdersWhereCustomerCompany (String inputCompany) {
		CustomerOrder order = null;
		List<CustomerOrder> ordersWithCustomer = null;
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			ordersWithCustomer = new ArrayList<CustomerOrder> ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				Customer customer = order.getCustomer ();
				String company = customer.getCompany ();
				if (company.equals (inputCompany)) {
					ordersWithCustomer.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return ordersWithCustomer;
	}

	public List<CustomerOrder> getCustomerOrdersWhereDisponent (String inputDisponentLastName) {
		CustomerOrder order = null;
		List<CustomerOrder> ordersWithDisponent = null;
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			ordersWithDisponent = new ArrayList<CustomerOrder> ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				SystemUser disponent = order.getDisponent ();
				String lastName = disponent.getSurname ();
				if (lastName.equals (inputDisponentLastName)) {
					ordersWithDisponent.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return ordersWithDisponent;
	}

	public List<CustomerOrder> getRoutedCustomerOrders () {
		CustomerOrder order = null;
		List<CustomerOrder> routedOrders = new ArrayList<CustomerOrder> ();
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				String status = order.getStatus ();
				if (status.equals ("ROUTING COMPLETE")) {
					routedOrders.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return routedOrders;
	}

	public List<CustomerOrder> getOrdersInProgressCustomerOrders () {
		CustomerOrder order = null;
		List<CustomerOrder> ordersInProgress = new ArrayList<> ();
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				String status = order.getStatus ();
				if (status.equals ("IN PROGRESS")) {
					ordersInProgress.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return ordersInProgress;
	}

	public List<CustomerOrder> getImportCustomerOrders () {
		CustomerOrder order = null;
		List<CustomerOrder> importOrders = new ArrayList<CustomerOrder> ();
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				String type = order.getType ();
				if (type.equals ("Import")) {
					importOrders.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return importOrders;
	}

	public List<CustomerOrder> getExportCustomerOrders () {
		CustomerOrder order = null;
		List<CustomerOrder> exportOrders= new ArrayList<> ();
		try {
			List<CustomerOrder> orders = this.getCustomerOrders ();
			for (int i = 0; i < orders.size (); i++) {
				order = orders.get (i);
				String type = order.getType ();
				if (type.equals ("Export")) {
					exportOrders.add (order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return exportOrders;
	}


}

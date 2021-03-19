/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.CustomerRecords;

import com.abenezer.itom.Service.CustomerOrders.CustomerOrderRegistryDatabase;
import com.abenezer.itom.Service.DispositionRecords.DispositionRegistryDatabase;
import com.abenezer.itom.algorithms.dijkstra.controller.GraphRegistryDatabase;
import com.abenezer.itom.model.CustomerRecords.Customer;
import com.abenezer.itom.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author EfthymiosChatziathanasiadis
 */
@Service
public class CustomerRegistryDatabase {

	private final CustomerRepository customerRepository;

	private final CustomerOrderRegistryDatabase customerOrderRegistryDatabase;

	private final DispositionRegistryDatabase dispositionRegistryDatabase;

	List<Customer> customers = new ArrayList<> ();

	@Autowired
	private CustomerRegistryDatabase (CustomerRepository customerRepository, GraphRegistryDatabase graphDatabase, CustomerOrderRegistryDatabase customerOrderRegistryDatabase, DispositionRegistryDatabase dispositionRegistryDatabase) {
		this.customerRepository = customerRepository;
		this.customerOrderRegistryDatabase = customerOrderRegistryDatabase;
		this.dispositionRegistryDatabase = dispositionRegistryDatabase;
		this.loadCustomers ();
	}

	public Customer addCustomer (Customer customer) {
		customers.add (customer);
		return customerRepository.save (customer);
	}

	public Customer getCustomerOfOrder (int id) {
		Customer customer = null;
		if (customers == null) {
			this.getCustomers ();
		}
		Iterator<Customer> it = customers.iterator ();
		while (it.hasNext ()) {
			customer = it.next ();
			if (customer.getId () == id) {
				break;
			}
		}

		return customer;
	}

	private void loadCustomers () {
		customers = customerRepository.findAll ();

	}

	public List<Customer> getCustomers () {

		return customers;
	}

	public Customer editCustomer (Customer customer) {
		return customerRepository.save (customer);
	}

	public void delete (Customer customer) {
		customerRepository.delete (customer);
		this.loadCustomers ();
		customerOrderRegistryDatabase.loadOrders ();
		dispositionRegistryDatabase.loadDispositions ();
	}

	public void deleteById (int customerId) {
		customerRepository.deleteById (customerId);
		this.loadCustomers ();
		customerOrderRegistryDatabase.loadOrders ();
		dispositionRegistryDatabase.loadDispositions ();
	}

}

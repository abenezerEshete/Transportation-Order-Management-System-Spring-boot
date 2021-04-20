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

import java.util.List;
import java.util.Optional;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class CustomerRegistryDatabase {

	private final CustomerRepository customerRepository;

	private final CustomerOrderRegistryDatabase customerOrderRegistryDatabase;

	private final DispositionRegistryDatabase dispositionRegistryDatabase;


	@Autowired
	private CustomerRegistryDatabase (CustomerRepository customerRepository, GraphRegistryDatabase graphDatabase, CustomerOrderRegistryDatabase customerOrderRegistryDatabase, DispositionRegistryDatabase dispositionRegistryDatabase) {
		this.customerRepository = customerRepository;
		this.customerOrderRegistryDatabase = customerOrderRegistryDatabase;
		this.dispositionRegistryDatabase = dispositionRegistryDatabase;
	}

	public List<Customer> getCustomers () {
		return customerRepository.findAll ();
	}

	public Customer addCustomer (Customer customer) {
		return customerRepository.save (customer);
	}

	public Customer getCustomerById (int id) {
		Optional<Customer> optionalCustomer = customerRepository.findById (id);
		if (optionalCustomer.isPresent ())
			return optionalCustomer.get ();
		else
			return null;
	}

	public Customer editCustomer (Customer customer) {
		return customerRepository.save (customer);
	}

	public void delete (Customer customer) {
		customerRepository.delete (customer);
		customerOrderRegistryDatabase.loadOrders ();
		dispositionRegistryDatabase.loadDispositions ();
	}

	public void deleteById (int customerId) {
		customerRepository.deleteById (customerId);
		customerOrderRegistryDatabase.loadOrders ();
		dispositionRegistryDatabase.loadDispositions ();
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.OrderTransit;

import com.abenezer.itom.model.CarrierRecords.Carrier;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author AbenezerEsheteTilahu
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_transite")
public class OrderTransit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;


	@OneToOne
	private Carrier carrier;


	@OneToOne
	private CustomerOrder customerOrder;

	@Column(name = "city", nullable = false)
	String city;

	@Column(name = "lat", nullable = false)
	double lat;

	@Column(name = "lon", nullable = false)
	double lon;

	@Column(name = "country", nullable = false)
	String country;


	@CreationTimestamp
	java.util.Date Date;
}
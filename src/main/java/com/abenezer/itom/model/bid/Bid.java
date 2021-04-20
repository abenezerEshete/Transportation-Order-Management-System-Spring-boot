/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.bid;

import com.abenezer.itom.model.CarrierRecords.*;
import com.abenezer.itom.model.CustomerOrders.CustomerOrder;
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
@Entity(name = "bid")
public class Bid implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@OneToOne
	private CustomerOrder customerOrder;

	@OneToOne
	private Carrier carrier;

	@Column(name = "bid_price", nullable = false)
	private double bidPrice;



}
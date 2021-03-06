/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.CustomerRecords;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
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
@Entity(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "company", nullable = false)
	private String company;

	@Column(name = "phone", nullable = false)
	private String phone;

	@OneToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
	private Vertex address;




	@Override
	public String toString () {
		return phone + "," + company + "," + address;
	}
}

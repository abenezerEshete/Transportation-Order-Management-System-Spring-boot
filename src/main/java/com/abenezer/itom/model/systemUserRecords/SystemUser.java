/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.systemUserRecords;

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
@Entity(name = "user")
public class SystemUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID", nullable = false)
	Integer id;

	@Column(name = "UserFirstName", nullable = false)
	String name;

	@Column(name = "UserLastName", nullable = false)
	String surname;

	@Column(name = "UserPassword", nullable = false)
	String password;

	@Column(name = "UserIsAdmin", nullable = false)
	boolean userIsAdmin;



	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public String getIsAdmin () {
		if (this instanceof Admin) {
			return "admin";
		} else return "planner";
	}

	public boolean isUserIsAdmin () {
		return userIsAdmin;
	}

	public String getPassword () {
		return this.password;
	}

	public void setPassword (String password) {
		this.password = password;
	}

	public String getName () {
		return this.name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getSurname () {
		return this.surname;
	}

	public void setSurname (String surname) {
		this.surname = surname;
	}
}

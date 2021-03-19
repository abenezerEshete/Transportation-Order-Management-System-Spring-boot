/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.systemUserRecords;

import com.abenezer.itom.model.systemUserRecords.Admin;
import com.abenezer.itom.model.systemUserRecords.SystemUser;
import com.abenezer.itom.model.systemUserRecords.TransportPlanner;
import com.abenezer.itom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EfthymiosChatziathanasiadis
 */

@Service
public class SystemUserRegistryDatabase {

	private final UserRepository userRepository;


	private SystemUser currentUser = null;

	private List<SystemUser> users = new ArrayList<> ();

	@Autowired
	private SystemUserRegistryDatabase (UserRepository userRepository) {
		this.userRepository = userRepository;
		this.loadUsers ();
	}

	public void setCurrentUser (SystemUser user) {
		this.currentUser = user;
	}

	public SystemUser getCurrentUser () {
		return this.currentUser;
	}

	public SystemUser getDisponentOfOrder (int id) {
		SystemUser disponent = null;
		if (users.isEmpty ()) {
			this.getUsers ();
		}
		for (SystemUser user : users) {
			disponent = user;
			if (disponent.getId () == id) break;
		}
		return disponent;
	}

	public SystemUser addSystemUser (SystemUser user) {
		try {
			if (user.isUserIsAdmin ()) {
				user = new Admin (user.getPassword (), user.getName (), user.getSurname ());
			} else {
				user = new TransportPlanner (user.getPassword (), user.getName (), user.getSurname ());
			}
			users.add (user);
			return userRepository.save (user);
		} catch (Exception ex) {
			ex.printStackTrace ();

		}
		return null;
	}

	private void loadUsers () {
		if (users.isEmpty ()) {
			try {
				users = userRepository.findAll ();
				users = users.stream ().map (user -> {
					if (user.isUserIsAdmin ())
						user = new Admin (user.getId (), user.getPassword (), user.getName (), user.getSurname ());
					else
						user = new TransportPlanner (user.getId (), user.getPassword (), user.getName (), user.getSurname ());
					return user;
				}).collect (Collectors.toList ());


			} catch (Exception e) {
				e.printStackTrace ();
			}
		}

	}

	public List<SystemUser> getUsers () {
		return users;
	}

	public SystemUser editUser (SystemUser user) {
		return userRepository.save (user);
	}

	public void deleteUserById (int userId) {

		userRepository.deleteById (userId);

	}

}

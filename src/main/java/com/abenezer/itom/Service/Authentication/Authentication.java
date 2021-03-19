/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.Service.Authentication;

import com.abenezer.itom.Service.systemUserRecords.SystemUserRegistryDatabase;
import com.abenezer.itom.model.systemUserRecords.Admin;
import com.abenezer.itom.model.systemUserRecords.SystemUser;
import com.abenezer.itom.model.systemUserRecords.TransportPlanner;
import com.abenezer.itom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author AbenezerEsheteTilahu
 */
@Service
public class Authentication {

	private final SystemUserRegistryDatabase usersRegistry;

	private final UserRepository userRepository;

	@Autowired
	public Authentication (SystemUserRegistryDatabase usersRegistry, UserRepository userRepository) {
		this.usersRegistry = usersRegistry;
		this.userRepository = userRepository;
	}


	public boolean authenticate (String id, String password) {
		boolean authentication = false;
		try {

			int userid = 0;
			try {
				userid = Integer.parseInt (id);
			} catch (NumberFormatException e) {
				e.printStackTrace ();
				return false;
			}

			Optional<SystemUser> optionalUser = userRepository.findById (userid);

			if (optionalUser.isPresent ()) {
				SystemUser user = optionalUser.get ();
				if (user.isUserIsAdmin ()) {
					user = new Admin (user.getId (), user.getPassword (), user.getName (),
						user.getSurname ());
				} else {
					user = new TransportPlanner (user.getId (), user.getPassword (), user.getName (),
						user.getSurname ());
				}
				authentication = true;
				usersRegistry.setCurrentUser (user);
			} else
				authentication = false;
		} catch (Exception se) {
			se.printStackTrace ();
		}
		return authentication;
	}
}

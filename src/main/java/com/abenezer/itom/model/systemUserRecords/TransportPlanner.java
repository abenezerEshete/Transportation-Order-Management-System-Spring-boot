/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.systemUserRecords;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class TransportPlanner extends SystemUser {
    
    public TransportPlanner(int id, String password, String name, String surname){
        this.id=id;
        this.password=password;
        this.name=name;
        this.surname=surname;
    }


	public TransportPlanner (String password, String name, String surname) {
		this.password=password;
		this.name=name;
		this.surname=surname;}
}

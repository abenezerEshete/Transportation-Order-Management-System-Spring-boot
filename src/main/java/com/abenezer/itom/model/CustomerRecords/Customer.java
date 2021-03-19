/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.CustomerRecords;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AbenezerEsheteTilahu
 */
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne
    private Vertex address;
    

    
    
    public Customer (String company, String phone, Vertex address){
        this.company=company;
        this.phone=phone;
        this.address = address;
    }
    
    public Customer(int id, String company, String phone, Vertex address){
        this.id=id;
        this.company=company;
        this.phone=phone;
        this.address = address;
    }
    
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public Vertex getAddress(){
        return this.address;
    }
    public String getCountry(){
        return this.address.getCountry();
    }
    public String getCity(){
        return this.address.getCity();
    }
    
    public void setAddress(Vertex address){
        this.address = address;
    }
    
   
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    
    public String getCompany(){
        return this.company;
    }
    public void setCompany(String company){
        this.company=company;
    }
    
    
    
    public String toString(){
        return this.company+", "+this.address.getCountry();
    }
}

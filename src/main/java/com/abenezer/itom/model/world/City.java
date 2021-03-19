/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.world;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class City {
    String city;
    double lat;
    double lon;
    Country country;
    
    public City(String city, double lat, double lon, Country country){
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }
   public City(String city, String lat, String lon, Country country){
        this.city = city;
       try {
           this.lat = Double.parseDouble (lat);
       } catch (NumberFormatException e) {
           e.printStackTrace ();
       }
       try {
           this.lon = Double.parseDouble (lon);
       } catch (NumberFormatException e) {
           e.printStackTrace ();
       }
       this.country = country;
    }

    public String getCity(){
        return this.city;
    }
    
    public double getLat(){
        return this.lat;
    }
    
    public double getLon(){
        return this.lon;
    }
    
    public Country getCountry(){
        return this.country;
    }
    @Override
    public String toString(){
        return this.city;
    }
}

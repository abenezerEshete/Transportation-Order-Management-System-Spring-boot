/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.CarrierRecords;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Road implements Mode{
    public void action(Carrier carrier){
        carrier.setMode(this);
    }
}

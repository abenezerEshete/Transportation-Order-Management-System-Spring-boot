/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abenezer.itom.model.Vehicles;

import com.abenezer.itom.algorithms.dijkstra.model.Vertex;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;


/**
 *
 * @author AbenezerEsheteTilahu
 */
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "trailer")
public class Trailer extends Vehicle {




	public Trailer(int id, boolean availabilityStatus, String plateNumber, boolean conditionStatus, Vertex address){
        super(id, availabilityStatus, plateNumber, conditionStatus, address);
    }


	public Trailer (boolean availabilityStatus, String plateNumber, boolean conditionStatus) {
        super( availabilityStatus, plateNumber, conditionStatus);
	}
}

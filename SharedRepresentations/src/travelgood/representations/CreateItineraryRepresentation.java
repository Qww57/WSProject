/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.representations;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@XmlRootElement
public class CreateItineraryRepresentation extends Representation {

    public int ID;
    
    public CreateItineraryRepresentation() {
    }
}

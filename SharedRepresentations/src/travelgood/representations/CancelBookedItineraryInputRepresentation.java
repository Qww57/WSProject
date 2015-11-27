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

// TODO Should be deleted now because of ItineraryOutputRepresentation

@XmlRootElement
public class CancelBookedItineraryInputRepresentation extends Representation {
    
    private String cancelstatus;
    
    public String getStatus(){
        return cancelstatus;
    }
    
}

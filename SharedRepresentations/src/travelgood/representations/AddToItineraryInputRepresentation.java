/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.representations;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel Brand
 */
@XmlRootElement
public class AddToItineraryInputRepresentation {
    
    public List<String> hotel_booking_numbers = new ArrayList<String>();
    public List<String> flight_booking_number = new ArrayList<String>();
   
}

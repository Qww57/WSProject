/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.objects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@XmlRootElement
public class Itinerary {
    
    public int ID;
    // Booking number, status
    public final LinkedHashMap<String, String> hotels = new LinkedHashMap<>();
    public final LinkedHashMap<String, String> flights = new LinkedHashMap<>();
    
    public void addHotel(String bookingnumber) {
        hotels.put(bookingnumber, "unconfirmed");
    }
    
    public void addFlight(String bookingnumber) {
        flights.put(bookingnumber, "unconfirmed");
    }
}

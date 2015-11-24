/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.FlightInformationType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.HotelInformationType;

/**
 *
 * @author Daniel
 */
public class Itinerary {
    private final int ID;
    private final Map<HotelInformationType, String> hotels = new HashMap<>();
    private final Map<FlightInformationType, String> flights = new HashMap<>();
    
    public Itinerary(int ID) {
        this.ID = ID;
    }
    
    public int getID() {
        return ID;
    }
    
    public Map<HotelInformationType, String> getHotels() {
        return hotels;
    }
    
    public Map<FlightInformationType, String> getFlights() {
        return flights;
    }
    
    public void addHotel(HotelInformationType hotel) {
        hotels.put(hotel, "unconfirmed");
    }
    
    public void addFlight(FlightInformationType flight) {
        flights.put(flight, "unconfirmed");
    }
}

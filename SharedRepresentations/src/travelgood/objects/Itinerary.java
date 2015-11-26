/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.objects;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Daniel
 */
public class Itinerary {
    private final int ID;
    // Booking number, status
    private final Map<String, String> hotels = new HashMap<>();
    private final Map<String, String> flights = new HashMap<>();
    
    public Itinerary(int ID) {
        this.ID = ID;
    }
    
    public int getID() {
        return ID;
    }
    
    public Map<String, String> getHotels() {
        return hotels;
    }
    
    public Map<String, String> getFlights() {
        return flights;
    }
    
    public void addHotel(String bookingnumber) {
        hotels.put(bookingnumber, "unconfirmed");
    }
    
    public void addFlight(String bookingnumber) {
        flights.put(bookingnumber, "unconfirmed");
    }
}

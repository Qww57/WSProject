/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class Database {
    
    private static int count = 0;
    private static final HashMap<Integer, Itinerary> itineraries = new HashMap<>();
    
    public static int createItinerary() {
        itineraries.put(count, new Itinerary(count));
        return count++;
    }
    
    public static boolean removeItinerary(Integer ID) {
        return itineraries.remove(ID) != null;
    }
}

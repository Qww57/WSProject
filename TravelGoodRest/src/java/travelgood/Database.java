/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.HashMap;
import travelgood.objects.Itinerary;

/**
 *
 * @author Daniel
 */
public class Database {
    
    private static int count = 0;
    private static final HashMap<Integer, Itinerary> plannedItineraries = new HashMap<>();
    private static final HashMap<Integer, Itinerary> bookedItineraries = new HashMap<>();
    private static final HashMap<Integer, CreditCardInfoType> bookingCreditCard = new HashMap<>();
    
    public static int createItinerary() {
        Itinerary it = new Itinerary();
        it.ID = count;
        plannedItineraries.put(count, it);
        return count++;
    }
        
    public static boolean cancelPlannedItinerary(Integer ID) {
        return plannedItineraries.remove(ID) != null;
    }
    
    public static Itinerary getPlannedItinerary(int ID) {
        return plannedItineraries.get(ID);
    }
    
    public static boolean cancelBookedItinerary(Integer ID) {
        return bookedItineraries.remove(ID) != null;
    }
    
    public static Itinerary getBookedItinerary(int ID) {
        return bookedItineraries.get(ID);
    }
    
    public static CreditCardInfoType getBookingCreditCard(int ID){
        return bookingCreditCard.get(ID);
    }
    
    public static void storeCreditCard(int ID, CreditCardInfoType creditCard){
        bookingCreditCard.put(ID, creditCard);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.HashMap;
import javax.xml.datatype.XMLGregorianCalendar;
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
    
    //added for dates:
    private static final HashMap<String, XMLGregorianCalendar> hotelsDates = new HashMap<>();
    private static final HashMap<String, XMLGregorianCalendar> flightsDates = new HashMap<>();
    
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
    
    public static void moveItineraryToBooked(int ID) {
        Itinerary it = plannedItineraries.remove(ID);
        bookedItineraries.put(ID, it);
    }
    
    public static CreditCardInfoType getBookingCreditCard(int ID){
        return bookingCreditCard.get(ID);
    }
    
    public static void storeCreditCard(int ID, CreditCardInfoType creditCard){
        bookingCreditCard.put(ID, creditCard);
    }
    
    //added for dates
    public static void addHotelDate(String bookingnumber, XMLGregorianCalendar date) {
        hotelsDates.put(bookingnumber, date);
    }
    public static XMLGregorianCalendar getHotelDate(String bookingNumber) {
        return hotelsDates.get(bookingNumber);
    }
    
    public static void addFlightDate(String bookingnumber, XMLGregorianCalendar date) {
        flightsDates.put(bookingnumber, date);
    }
    public static XMLGregorianCalendar getFlightDate(String bookingNumber) {
        return flightsDates.get(bookingNumber);
    }
}

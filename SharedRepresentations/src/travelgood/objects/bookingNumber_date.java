/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.objects;

import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Dani Sanz
 */
public class bookingNumber_date {
    public final Map<String, XMLGregorianCalendar> hotelsDates = new HashMap<>();
    public final Map<String, XMLGregorianCalendar> flightsDates = new HashMap<>();
    
    public void addHotelDate(String bookingnumber, XMLGregorianCalendar date) {
        hotelsDates.put(bookingnumber, date);
    }
    public XMLGregorianCalendar getHotelDate(String bookingNumber) {
        return hotelsDates.get(bookingNumber);
    }
    
    public void addFlightDate(String bookingnumber, XMLGregorianCalendar date) {
        flightsDates.put(bookingnumber, date);
    }
    public XMLGregorianCalendar getFlightDate(String bookingNumber) {
        return flightsDates.get(bookingNumber);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import travelgood.representations.BookItineraryInputRepresentation;
import travelgood.representations.SearchInputRepresentation;

/**
 *
 * @author Quentin
 */
public class Constructors {
    
    public static BookItineraryInputRepresentation createBookItineraryInputRepresentation(String name, String number, int month, int year){
        BookItineraryInputRepresentation output = new BookItineraryInputRepresentation();       
        output.name = name;
        output.number = number;
        output.expirationMonth = month;
        output.expirationYear = year;
        
        return output;
    }
    
    public static SearchInputRepresentation.SearchFlightInputRepresentation createSearchFlightRep(String start, String destination, int day, int month, int year){
        SearchInputRepresentation.SearchFlightInputRepresentation output = new SearchInputRepresentation.SearchFlightInputRepresentation();
        output.setDate(CreateDate(day, month, year));
        output.setStart(start);
        output.setDestination(destination);
        return output;
   }
    
    public static SearchInputRepresentation.SearchHotelInputRepresentation createSearchHotelRep(String city, XMLGregorianCalendar arrivalDate, XMLGregorianCalendar depertureDate){
        SearchInputRepresentation.SearchHotelInputRepresentation output = new SearchInputRepresentation.SearchHotelInputRepresentation();
        output.setArrivalDate(arrivalDate);
        output.setDepartureDate(depertureDate);
        output.setCity(city);
        return output;
    }
      
    public static XMLGregorianCalendar CreateDate(int day, int month, int year) {
        XMLGregorianCalendar date = null;
        try{
            GregorianCalendar gc = new GregorianCalendar(year, month, day);
            DatatypeFactory df = DatatypeFactory.newInstance();
            date = df.newXMLGregorianCalendar(gc);
            date.setDay(day);
            date.setMonth(month);
            date.setYear(year);
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return date;
    }
}

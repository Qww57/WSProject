/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package travelgood;

import java.util.GregorianCalendar;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import travelgood.representations.*;

/**
 *
 * @author Dani Sanz
 */
public class SearchResourceTest {
    
    @Test
    public void searchEmpty() throws DatatypeConfigurationException {
        //input stuff for searching flights and hotels
        SearchInputRepresentation myEmptySearch = new SearchInputRepresentation();
        //build client and make post request
        Client clientEmpty = ClientBuilder.newClient();
        WebTarget resourceEmpty = clientEmpty.target("http://localhost:8080/ws/webresources/search");
        Response searchEmptyResult = resourceEmpty.request().post(Entity.entity(myEmptySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchEmptyResultEntity = searchEmptyResult.readEntity(SearchOutputRepresentation.class);
        //check that lists are empty
        System.out.println("hotels list length: " + searchEmptyResultEntity.hotelsList.size());
        System.out.println("flights list length: " + searchEmptyResultEntity.flightsList.size());
    }
    
    @Test
    public void searchTwoHotelsandTwoFlights()  throws DatatypeConfigurationException{
        //input stuff for searching flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        //first hotel
        SearchInputRepresentation.SearchHotelInputRepresentation myFisrtHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        myFisrtHotel.setArrivalDate(CreateDate(26, 10, 2015));
        myFisrtHotel.setDepartureDate(CreateDate(29, 10, 2015));
        myFisrtHotel.setCity("Paris");
        mySearch.hotelsList.add(0, myFisrtHotel);
        //second hotel
        SearchInputRepresentation.SearchHotelInputRepresentation mySecondHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        mySecondHotel.setArrivalDate(CreateDate(12, 11, 2015));
        mySecondHotel.setDepartureDate(CreateDate(18, 11, 2015));
        mySecondHotel.setCity("Milan");
        mySearch.hotelsList.add(1, mySecondHotel);
        //first flight
        SearchInputRepresentation.SearchFlightInputRepresentation myFirstFlight = new SearchInputRepresentation.SearchFlightInputRepresentation();
        myFirstFlight.setDate(CreateDate(26, 12, 2015));
        myFirstFlight.setStart("Barcelona");
        myFirstFlight.setDestination("New York");
        mySearch.flightsList.add(myFirstFlight);
        //second flight
        SearchInputRepresentation.SearchFlightInputRepresentation mySecondFlight = new SearchInputRepresentation.SearchFlightInputRepresentation();
        mySecondFlight.setDate(CreateDate(26, 10, 2015));
        mySecondFlight.setStart("Copenhagen");
        mySecondFlight.setDestination("London");
        mySearch.flightsList.add(mySecondFlight);
        //build client and make post request
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("http://localhost:8080/ws/webresources/search");
        Response searchResult = resource.request().post(Entity.entity(mySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        //hotel result
        System.out.println("hotels list length: " + searchResultEntity.hotelsList.size());
        System.out.println("number of hotels for 1st option: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.size());
        System.out.println("hotel 0.0 name: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 0.0 price: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getPrice());
        System.out.println("hotel 0.0 booking number: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getBookingNumber());
        System.out.println("hotel 0.1 name: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).gethotel().getName());
        System.out.println("hotel 0.1 price: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).getPrice());
        System.out.println("hotel 0.1 booking number: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).getBookingNumber());
        System.out.println("hotel 1.0 name: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 1.0 price: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getPrice());
        System.out.println("hotel 1.0 booking number: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getBookingNumber());
        //flight result
        System.out.println("flights list length: " + searchResultEntity.flightsList.size());
        System.out.println("flight 1 booking number: " + searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getBookingNumber());
        System.out.println("flight 1 booking start: " + searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getFlight().getStart());
        System.out.println("flight 2 number: " + searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getBookingNumber()); 
        System.out.println("flight 2 start: " + searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getFlight().getStart());
    }
    
    public static XMLGregorianCalendar CreateDate(int day, int month, int year) throws DatatypeConfigurationException{
        XMLGregorianCalendar date;
        
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        date = df.newXMLGregorianCalendar(gc);
        date.setDay(day);
        date.setMonth(month);
        date.setYear(year);
        
        return date;
    }
      
    
}

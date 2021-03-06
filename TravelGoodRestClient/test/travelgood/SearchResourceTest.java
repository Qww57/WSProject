/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package travelgood;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import org.junit.Test;
import static org.junit.Assert.*;
import static travelgood.Constructors.CreateDate;
import travelgood.representations.*;

/**
 *
 * @author Dani Sanz
 */
public class SearchResourceTest {
    
    private final String MIMEType = MediaType.APPLICATION_XML; // MediaType.APPLICATION_JSON also supported
    
    @Test
    public void searchEmpty() throws DatatypeConfigurationException {
        //input stuff for searching flights and hotels
        SearchInputRepresentation myEmptySearch = new SearchInputRepresentation();
        //build client and make post request
        Client clientEmpty = ClientBuilder.newClient();
        WebTarget resourceEmpty = clientEmpty.target("http://localhost:8080/ws/webresources/search");
        Response searchEmptyResult = resourceEmpty.request().accept(MIMEType).post(Entity.entity(myEmptySearch, MIMEType), Response.class);
        SearchOutputRepresentation searchEmptyResultEntity = searchEmptyResult.readEntity(SearchOutputRepresentation.class);
        //check that lists are empty
        assertEquals(0, searchEmptyResultEntity.hotelsList.size());
        assertEquals(0, searchEmptyResultEntity.flightsList.size());
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
        Response searchResult = resource.request().accept(MIMEType).post(Entity.entity(mySearch, MIMEType), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        //hotel result
        assertEquals(2, searchResultEntity.hotelsList.size());
        System.out.println("hotels list length: " + searchResultEntity.hotelsList.size());
        assertEquals(2, searchResultEntity.hotelsList.get(0).hotelsInformationList.size());
        System.out.println("number of hotels for 1st option: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.size());
        assertEquals("Hotel de France", searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 0.0 name: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 0.0 price: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getPrice());
        System.out.println("hotel 0.0 booking number: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getBookingNumber());
        assertEquals("NY Hotel", searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).gethotel().getName());
        System.out.println("hotel 0.1 name: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).gethotel().getName());
        System.out.println("hotel 0.1 price: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).getPrice());
        System.out.println("hotel 0.1 booking number: " + searchResultEntity.hotelsList.get(0).hotelsInformationList.get(1).getBookingNumber());
        assertEquals("Milan Hotel", searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 1.0 name: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).gethotel().getName());
        System.out.println("hotel 1.0 price: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getPrice());
        System.out.println("hotel 1.0 booking number: " + searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getBookingNumber());
        //flight result
        assertEquals(2, searchResultEntity.flightsList.size());
        System.out.println("flights list length: " + searchResultEntity.flightsList.size());
        assertEquals("Barcelona", searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getFlight().getStart());
        System.out.println("flight 1 booking start: " + searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getFlight().getStart());
        System.out.println("flight 1 booking number: " + searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getBookingNumber());
        assertEquals("Copenhagen", searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getFlight().getStart());
        System.out.println("flight 2 start: " + searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getFlight().getStart());
        System.out.println("flight 2 booking number: " + searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getBookingNumber()); 
    } 
}

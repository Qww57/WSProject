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
import static org.junit.Assert.*;
import org.junit.Test;
import static travelgood.Constructors.*;
import travelgood.representations.*;

/**
 *
 * @author Quentin
 */
public class ScenarioTestCases {
    
    @Test
    public void ScenarioTest_P1() throws DatatypeConfigurationException {
        // Get a list of flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        
        SearchInputRepresentation.SearchHotelInputRepresentation myFisrtHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        myFisrtHotel.setArrivalDate(CreateDate(26, 10, 2015));
        myFisrtHotel.setDepartureDate(CreateDate(29, 10, 2015));
        myFisrtHotel.setCity("Paris");
        mySearch.hotelsList.add(0, myFisrtHotel);
        
        SearchInputRepresentation.SearchHotelInputRepresentation mySecondHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        mySecondHotel.setArrivalDate(CreateDate(12, 11, 2015));
        mySecondHotel.setDepartureDate(CreateDate(18, 11, 2015));
        mySecondHotel.setCity("Milan");      
        mySearch.hotelsList.add(1, mySecondHotel);
              
        SearchInputRepresentation.SearchFlightInputRepresentation myFirstFlight = createSearchFlightRep("Barcelona", "New York", 26, 12, 2015);
        mySearch.flightsList.add(0, myFirstFlight);
        
        SearchInputRepresentation.SearchFlightInputRepresentation mySecondFlight = createSearchFlightRep("Copenhagen", "London", 26, 10, 2015);
        mySearch.flightsList.add(1, mySecondFlight);
        
        SearchInputRepresentation.SearchFlightInputRepresentation thirdFlight = createSearchFlightRep("Copenhagen", "Kuala Lumpur", 26, 2, 2016);
        mySearch.flightsList.add(2, thirdFlight);
        
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("http://localhost:8080/ws/webresources/search");
        Response searchResult = resource.request().post(Entity.entity(mySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        
        String bookingNumber_flight1 = searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_flight2 = searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_flight3 = searchResultEntity.flightsList.get(2).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel1 = searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel2 = searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getBookingNumber();;
        
        // Create an itinerary and add one flight  
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
        
        WebTarget r2 = client.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.flight_booking_number.add(bookingNumber_flight1);
        
        Response planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add an hotel
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel1);        
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add another flight
        inputRepresentation.flight_booking_number.add(bookingNumber_flight2);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);

        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");        
        
        // Add a third flight 
        inputRepresentation.flight_booking_number.add(bookingNumber_flight3);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add another hotel 
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel2);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        assertTrue(true);
        
        // Request for the itinerary
        planResult = r2.request().get(Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("Get planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Get planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- "); 
        
        
        // Check that everything is unconfirmed
    
    }
    
    @Test
    public void ScenarioTest_P2() {
    
    }
    
    @Test
    public void ScenarioTest_B() {
    
    }
    
    
    @Test
    public void ScenarioTest_C1() {
    
    }
    
    @Test
    public void ScenarioTest_C2() {
    
    }
}

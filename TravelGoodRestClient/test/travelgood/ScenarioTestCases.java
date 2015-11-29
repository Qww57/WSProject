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
    public void ScenarioTest_P1() {
        
        System.out.println("Staringt test P1");
                
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
        
        // Adding the lists of hotels and flights to the itinerary
        WebTarget r2 = client.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.flight_booking_number.add(bookingNumber_flight1);
        
        Response planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Planning the itinerary"); 
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add an hotel
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel1);        
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);  
        System.out.println("Adding one new hotel");
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add another flight
        inputRepresentation.flight_booking_number.add(bookingNumber_flight2);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Adding one new flight");
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");        
        
        // Add a third flight 
        inputRepresentation.flight_booking_number.add(bookingNumber_flight3);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Adding one new flight");
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");
        
        // Add another hotel 
        System.out.println("Adding one new hotel");
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel2);
        planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);      
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        System.out.println(" -- ");      
        
        // Request for the itinerary
        planResult = r2.request().get(Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Getting the itinerary"); 
        System.out.println("Get planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Get planned flights: " + planResultEntity.itinerary.flights);  
        
        // Check that everything is unconfirmed
        for(String status : planResultEntity.itinerary.hotels.values()){
            assertEquals("unconfirmed", status);
        }
        
        for(String status : planResultEntity.itinerary.flights.values()){
            assertEquals("unconfirmed", status);
        }
    }
    
    @Test
    public void ScenarioTest_P2() {
        
        System.out.println("Staringt test P2");
        
        // Create an itinerary and add one flight  
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
              
        // Get a list of flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        
        SearchInputRepresentation.SearchHotelInputRepresentation myFisrtHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        myFisrtHotel.setArrivalDate(CreateDate(26, 10, 2015));
        myFisrtHotel.setDepartureDate(CreateDate(29, 10, 2015));
        myFisrtHotel.setCity("Paris");
          
        SearchInputRepresentation.SearchHotelInputRepresentation mySecondHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        mySecondHotel.setArrivalDate(CreateDate(12, 11, 2015));
        mySecondHotel.setDepartureDate(CreateDate(18, 11, 2015));
        mySecondHotel.setCity("Milan");      
        
        SearchInputRepresentation.SearchFlightInputRepresentation myFirstFlight = createSearchFlightRep("Barcelona", "New York", 26, 12, 2015);      
        SearchInputRepresentation.SearchFlightInputRepresentation mySecondFlight = createSearchFlightRep("Copenhagen", "London", 26, 10, 2015);
        SearchInputRepresentation.SearchFlightInputRepresentation thirdFlight = createSearchFlightRep("Copenhagen", "Kuala Lumpur", 26, 2, 2016);
        mySearch.flightsList.add(0, myFirstFlight);
        mySearch.flightsList.add(1, mySecondFlight);   
        mySearch.flightsList.add(2, thirdFlight);
        mySearch.hotelsList.add(0, myFisrtHotel);
        mySearch.hotelsList.add(1, mySecondHotel);
       
        WebTarget resource = client.target("http://localhost:8080/ws/webresources/search");
        Response searchResult = resource.request().post(Entity.entity(mySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        
        String bookingNumber_flight1 = searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_flight2 = searchResultEntity.flightsList.get(1).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_flight3 = searchResultEntity.flightsList.get(2).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel1 = searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel2 = searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getBookingNumber();;
        
        WebTarget r2 = client.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.flight_booking_number.add(bookingNumber_flight1);
        inputRepresentation.flight_booking_number.add(bookingNumber_flight2);
        inputRepresentation.flight_booking_number.add(bookingNumber_flight3);
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel1);
        inputRepresentation.hotel_booking_numbers.add(bookingNumber_hotel2);
             
        Response planResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Planning the itinerary");  
        System.out.println("Planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntity.itinerary.flights);
        
        // Request for the itinerary
        planResult = r2.request().get(Response.class);
        planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Getting the itinerary");    
        System.out.println("Get planned hotels: " + planResultEntity.itinerary.hotels);
        System.out.println("Get planned flights: " + planResultEntity.itinerary.flights);
        
        
        // Check that everything is unconfirmed
        for(String status : planResultEntity.itinerary.hotels.values()){
            assertEquals("unconfirmed", status);
        }
        
        for(String status : planResultEntity.itinerary.flights.values()){
            assertEquals("unconfirmed", status);
        }
        
        // Cancel the planning 
        inputRepresentation.flight_booking_number.add(bookingNumber_flight1);
        r2 = client.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID) + "/cancel");
        Response cancelResult = r2.request().get(Response.class);
        System.out.println(cancelResult);
        
        // Check that everything is cancelled by trying to get the itinerary and getting an error
        boolean requestHasFailed = false;
        try {
            planResult = r2.request().get(Response.class);
            planResultEntity = planResult.readEntity(ItineraryOutputRepresentation.class);
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
            requestHasFailed = true;
        }
        assertTrue(requestHasFailed);
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

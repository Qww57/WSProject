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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import static org.junit.Assert.*;
import org.junit.Test;
import static travelgood.Constructors.*;
import travelgood.objects.LinkRelatives;
import travelgood.representations.*;

/**
 *
 * @author Quentin, Daniel Sanz
 */
public class ScenarioTestCases {
    
    @Test
    public void testP1() {
        
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
        
        // Booking the itinerary
        BookItineraryInputRepresentation bookInput = createBookItineraryInputRepresentation("Thor-Jensen Claus", "50408825", 5, 9);
        
        WebTarget r3 = client.target("http://localhost:8080/ws/webresources/booking/" + Integer.toString(resultentity.ID));
        Response bookOutput = r3.request().post(Entity.entity(bookInput, MediaType.APPLICATION_XML), Response.class);
        System.out.println(bookOutput.toString());
        ItineraryOutputRepresentation bookOutputEntity = bookOutput.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Booking the itinerary"); 
        System.out.println("Get planned hotels: " + bookOutputEntity.itinerary.hotels);
        System.out.println("Get planned flights: " + bookOutputEntity.itinerary.flights);        
        
         // Check that everything is confirmed
        for(String status : bookOutputEntity.itinerary.hotels.values()){
            assertEquals("confirmed", status);
        }
        
        for(String status : bookOutputEntity.itinerary.flights.values()){
            assertEquals("confirmed", status);
        }
    }
    
    @Test
    public void testP2() {
        
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
    public void testB() {
    
    }
        
    @Test
    public void testC1() {
        System.out.println("Staringt test C1");
        
        // Create an itinerary GET request 
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
                
        //Searching a list of flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        //1st hotel to be searched
        SearchInputRepresentation.SearchHotelInputRepresentation myFisrtHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        myFisrtHotel.setArrivalDate(CreateDate(30, 1, 2016));
        myFisrtHotel.setDepartureDate(CreateDate(4, 2, 2016));
        myFisrtHotel.setCity("London");
        mySearch.hotelsList.add(0, myFisrtHotel);
        //2nd hotel to be searched
        SearchInputRepresentation.SearchHotelInputRepresentation mySecondHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        mySecondHotel.setArrivalDate(CreateDate(4, 2, 2016));
        mySecondHotel.setDepartureDate(CreateDate(8, 2, 2016));
        mySecondHotel.setCity("Paris");      
        mySearch.hotelsList.add(1, mySecondHotel);
        //1st flight to be searched    
        SearchInputRepresentation.SearchFlightInputRepresentation myFirstFlight = createSearchFlightRep("Copenhagen", "Kuala Lumpur", 26, 2, 2016);
        mySearch.flightsList.add(0, myFirstFlight);
        //search POST request
        WebTarget resource = client.target("http://localhost:8080/ws/webresources/search");
        Response searchResult = resource.request().post(Entity.entity(mySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        
        //check that search contains 1 flight and 2 hotels
        assertEquals(2, searchResultEntity.hotelsList.size());
        assertEquals(1, searchResultEntity.flightsList.size());
        //get booking numbers
        String bookingNumber_flight1 = searchResultEntity.flightsList.get(0).flightsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel1 = searchResultEntity.hotelsList.get(0).hotelsInformationList.get(0).getBookingNumber();
        String bookingNumber_hotel2 = searchResultEntity.hotelsList.get(1).hotelsInformationList.get(0).getBookingNumber();
        
        //add flights and hotels to itinerary
        //use link to add flights and hotels to itinerary
        Link addToItineraryLink = result.getLink(LinkRelatives.ADD_TO_ITINERARY);
        WebTarget r2 = client.target(addToItineraryLink);
        // Add 1st flight to itinerary: POST request using link
        System.out.println("Adding one flight"); 
        AddToItineraryInputRepresentation itineraryInputOne = new AddToItineraryInputRepresentation();
        itineraryInputOne.flight_booking_number.add(bookingNumber_flight1);
        Response planResultOne = r2.request().post(Entity.entity(itineraryInputOne, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntityOne = planResultOne.readEntity(ItineraryOutputRepresentation.class);
        //check size
        assertEquals(1, planResultEntityOne.itinerary.flights.size());
        assertEquals(0, planResultEntityOne.itinerary.hotels.size());
        System.out.println("Planned hotels: " + planResultEntityOne.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntityOne.itinerary.flights);
        System.out.println(" -- ");
        // Add 1st hotel
        System.out.println("Adding one new hotel");
        AddToItineraryInputRepresentation itineraryInputTwo = new AddToItineraryInputRepresentation();
        itineraryInputTwo.hotel_booking_numbers.add(bookingNumber_hotel1);        
        Response planResultTwo = r2.request().post(Entity.entity(itineraryInputTwo, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntityTwo = planResultTwo.readEntity(ItineraryOutputRepresentation.class);  
        //check size
        assertEquals(1, planResultEntityTwo.itinerary.flights.size());
        assertEquals(1, planResultEntityTwo.itinerary.hotels.size());
        System.out.println("Planned hotels: " + planResultEntityTwo.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntityTwo.itinerary.flights);
        System.out.println(" -- ");
        // Add 2nd hotel 
        System.out.println("Adding 2nd hotel");
        AddToItineraryInputRepresentation itineraryInputThree = new AddToItineraryInputRepresentation();      
        itineraryInputThree.hotel_booking_numbers.add(bookingNumber_hotel2);
        Response planResultThree = r2.request().post(Entity.entity(itineraryInputThree, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation planResultEntityThree = planResultThree.readEntity(ItineraryOutputRepresentation.class);      
        //check size
        assertEquals(1, planResultEntityThree.itinerary.flights.size());
        assertEquals(2, planResultEntityThree.itinerary.hotels.size());
        System.out.println("Planned hotels: " + planResultEntityThree.itinerary.hotels);
        System.out.println("Planned flights: " + planResultEntityThree.itinerary.flights);
        System.out.println(" -- ");      
        
        // Request for the itinerary
        Response planResultItinerary = r2.request().get(Response.class);
        ItineraryOutputRepresentation planResultItineraryEntity = planResultItinerary.readEntity(ItineraryOutputRepresentation.class);
        System.out.println("Getting the itinerary"); 
        //check size
        assertEquals(1, planResultItineraryEntity.itinerary.flights.size());
        assertEquals(2, planResultItineraryEntity.itinerary.hotels.size());
        System.out.println("Get planned hotels: " + planResultItineraryEntity.itinerary.hotels);
        System.out.println("Get planned flights: " + planResultItineraryEntity.itinerary.flights);  
        
        // Check that everything is unconfirmed
        for(String status : planResultItineraryEntity.itinerary.hotels.values()){
            assertEquals("unconfirmed", status);
        }
        for(String status : planResultItineraryEntity.itinerary.flights.values()){
            assertEquals("unconfirmed", status);
        }
        
        //booking the itinerary
        //use link to add flights and hotels to itinerary
        Link bookItineraryLink = planResultItinerary.getLink(LinkRelatives.BOOK_ITINERARY);
        WebTarget r3 = client.target(bookItineraryLink);//adding credit card information
        BookItineraryInputRepresentation bookItineraryInput = createBookItineraryInputRepresentation("Thor-Jensen Claus", "50408825", 5, 9);
        Response bookingResult = r3.request().post(Entity.entity(bookItineraryInput, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation bookingResultEntity = bookingResult.readEntity(ItineraryOutputRepresentation.class);
        //check size
        assertEquals(1, bookingResultEntity.itinerary.flights.size());
        assertEquals(2, bookingResultEntity.itinerary.hotels.size());
        System.out.println("Get booked hotels: " + bookingResultEntity.itinerary.hotels);
        System.out.println("Get booked flights: " + bookingResultEntity.itinerary.flights); 
        // Check that everything is confirmed
        for(String status : bookingResultEntity.itinerary.hotels.values()){
            assertEquals("confirmed", status);
        }
        for(String status : bookingResultEntity.itinerary.flights.values()){
            assertEquals("confirmed", status);
        }
        
        //cancelling the itinerary
        //use link to add flights and hotels to itinerary
        Link cancelItineraryLink = bookingResult.getLink(LinkRelatives.CANCEL_BOOKED_ITINERARY);
        WebTarget r4 = client.target(cancelItineraryLink);
        Response resultCancelItinerary = r4.request().get(Response.class);
        ItineraryOutputRepresentation resultCancelItineraryEntity = resultCancelItinerary.readEntity(ItineraryOutputRepresentation.class);
        //check size
        assertEquals(1, resultCancelItineraryEntity.itinerary.flights.size());
        assertEquals(2, resultCancelItineraryEntity.itinerary.hotels.size());
        System.out.println("Get booked hotels: " + resultCancelItineraryEntity.itinerary.hotels);
        System.out.println("Get booked flights: " + resultCancelItineraryEntity.itinerary.flights); 
        // Check that everything is confirmed
        for(String status : resultCancelItineraryEntity.itinerary.hotels.values()){
            assertEquals("cancelled", status);
        }
        for(String status : resultCancelItineraryEntity.itinerary.flights.values()){
            assertEquals("cancelled", status);
        }
        System.out.println("Test C1 Finished!");
    }
    
    @Test
    public void testC2() {
    
    }
}

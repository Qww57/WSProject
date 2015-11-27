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
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static travelgood.Constructors.*;
import travelgood.representations.*;

/**
 *
 * @author Quentin
 */
public class BookingResourceTest {
        
    @Test
    public void bookOneHotelTest(){
        // Creating an itinerary
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
        
        // Searching hotels and flights
        
        
        // Adding a booking number to the itinerary
        Client secondClient = ClientBuilder.newClient();
        WebTarget r2 = secondClient.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        
        //TEMP: create input for adding a hotel to the itinerary
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.hotel_booking_numbers.add("booking_Hotel_4");
        
        Response secondResult = r2.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation secondResultEntity = secondResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("result: " + secondResultEntity.itinerary.hotels);
        String resultthes = secondResultEntity.itinerary.hotels.get("booking_Hotel_4");
        System.out.println("result: " + resultthes);
        
        // Adding a booking number to the itinerary
        Client thirdClient = ClientBuilder.newClient();
        WebTarget r3 = secondClient.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        BookItineraryInputRepresentation bookInput = createBookItineraryInputRepresentation("Thor-Jensen Claus", "50408825", 5, 9);
        
        Response thirdResult = r3.request().post(Entity.entity(bookInput, MediaType.APPLICATION_XML), Response.class);
        ItineraryOutputRepresentation thirdResultEntity = thirdResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("result: " + thirdResultEntity);
        String bookResult = secondResultEntity.itinerary.hotels.get("booking_Hotel_4");
        System.out.println("result: " + bookResult);
    }
}

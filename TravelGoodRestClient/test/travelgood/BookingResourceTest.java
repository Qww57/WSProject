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
 * @author Quentin, Ali, Daniel Brand
 */
public class BookingResourceTest {
        
    private final String MIMEType = MediaType.APPLICATION_XML; // MediaType.APPLICATION_JSON also supported
    
    @Test
    public void bookOneHotelTest(){
        // Creating an itinerary
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().accept(MIMEType).get(Response.class);
        System.out.println(result);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
        
        // Searching hotels and flights
               
        // Adding a booking number to the itinerary
        WebTarget r2 = client.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        
        //TEMP: create input for adding a hotel to the itinerary
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.hotel_booking_numbers.add("booking_Hotel_4");
        
        Response secondResult = r2.request().accept(MIMEType).post(Entity.entity(inputRepresentation, MIMEType), Response.class);
        ItineraryOutputRepresentation secondResultEntity = secondResult.readEntity(ItineraryOutputRepresentation.class);       
        System.out.println("result: " + secondResultEntity.itinerary.hotels);
        String resultthes = secondResultEntity.itinerary.hotels.get("booking_Hotel_4");
        System.out.println("result: " + resultthes);
        
        inputRepresentation.hotel_booking_numbers.add("booking_Hotel_5");
        
        secondResult = r2.request().accept(MIMEType).post(Entity.entity(inputRepresentation, MIMEType), Response.class);
        secondResultEntity = secondResult.readEntity(ItineraryOutputRepresentation.class);       
        System.out.println("result: " + secondResultEntity.itinerary.hotels);
        resultthes = secondResultEntity.itinerary.hotels.get("booking_Hotel_5");
        System.out.println("result: " + resultthes);
        
        // Adding a booking number to the itinerary
        BookItineraryInputRepresentation bookInput = createBookItineraryInputRepresentation("Thor-Jensen Claus", "50408825", 5, 9);
        
        WebTarget r3 = client.target("http://localhost:8080/ws/webresources/booking/" + Integer.toString(resultentity.ID));
        Response thirdResult = r3.request().accept(MIMEType).post(Entity.entity(bookInput, MIMEType), Response.class);
        System.out.println(thirdResult.toString());
        ItineraryOutputRepresentation thirdResultEntity = thirdResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("result: " + thirdResultEntity);
        String bookResult = thirdResultEntity.itinerary.hotels.get("booking_Hotel_4");
        System.out.println("result: " + bookResult);
    }
}

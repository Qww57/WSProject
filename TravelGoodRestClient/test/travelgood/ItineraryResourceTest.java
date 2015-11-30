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
import org.junit.Test;
import static org.junit.Assert.*;
import travelgood.objects.LinkRelatives;
import travelgood.representations.*;

/**
 *
 * @author Daniel
 */
public class ItineraryResourceTest {

    private final String MIMEType = MediaType.APPLICATION_XML; // MediaType.APPLICATION_JSON also supported
    
    @Test
    public void createItineraryTest() {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().accept(MIMEType).get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
        
        Client secondClient = ClientBuilder.newClient();
        WebTarget r2 = secondClient.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        //create input for adding a hotel to the itinerary
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.hotel_booking_numbers.add("thisBN");
        
        Response secondResult = r2.request().accept(MIMEType).post(Entity.entity(inputRepresentation, MIMEType), Response.class);
        ItineraryOutputRepresentation secondResultEntity = secondResult.readEntity(ItineraryOutputRepresentation.class);
        
        System.out.println("result: " + secondResultEntity.itinerary.hotels);
        String resultthes = secondResultEntity.itinerary.hotels.get(0);
        System.out.println("result: " + resultthes);          
    }
    
    @Test
    public void cancelEmptyPlannedItinerary() {
        
        // Create a new itinerary
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().accept(MIMEType).get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        
        // Get links from the response
        Link cancelItineraryLink = result.getLink(LinkRelatives.CANCEL_PLANNED_ITINERARY);
        Link findPlannedItineraryLink = result.getLink(LinkRelatives.FIND_PLANNED_ITINERARY);
        
        // Use link to call operation
        Client secondClient = ClientBuilder.newClient();
        WebTarget r2 = secondClient.target(cancelItineraryLink);
        Response result2 = r2.request().accept(MIMEType).get(Response.class);
        
        assertEquals(Response.Status.OK.getStatusCode(), result2.getStatus());
        
        // Verify itinerary has been cancelled
        Client thirdclient = ClientBuilder.newClient();
        WebTarget r3 = thirdclient.target(findPlannedItineraryLink);
        Response result3 = r3.request().accept(MIMEType).get(Response.class);
        
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result3.getStatus());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import travelgood.objects.Itinerary;
import travelgood.representations.*;

/**
 *
 * @author Daniel
 */
public class ItineraryResourceTest {

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void createItineraryTest() {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/ws/webresources/itinerary");
        Response result = r.request().get(Response.class);
        CreateItineraryRepresentation resultentity = result.readEntity(CreateItineraryRepresentation.class);
        System.out.println("returned ID: " + resultentity.ID);
        assertTrue(0 <= resultentity.ID);
        
        Client secondClient = ClientBuilder.newClient();
        WebTarget r2 = secondClient.target("http://localhost:8080/ws/webresources/itinerary/" + Integer.toString(resultentity.ID));
        //create input for adding a hotel to the itinerary
        AddToItineraryInputRepresentation inputRepresentation = new AddToItineraryInputRepresentation();
        inputRepresentation.hotel_booking_numbers.add("thisBN");
        
        Response secondResult = r.request().post(Entity.entity(inputRepresentation, MediaType.APPLICATION_XML), Response.class);
        Itinerary secondResultEntity = secondResult.readEntity(Itinerary.class);
        
        //System.out.println("length: " + secondResultEntity.hotel_booking_numbers.size());
        //String resultthes = secondResultEntity.hotel_booking_numbers.get(0);
        //System.out.println("result: " + resultthes);
        
    }
}

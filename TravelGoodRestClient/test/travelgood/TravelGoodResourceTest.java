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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class TravelGoodResourceTest {
    
    Client client = ClientBuilder.newClient();
    WebTarget r = client.target("http://localhost:8080/ws/webresources/travelgood");

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void TravelGoodResourceGetTest() {
        String result = r.request().get(String.class);
        assertEquals("TravelGood", result);
    }
    
    @Test
    public void TravelGoodResourcePutTest() {
        String expected = "TG";
        r.request().put(Entity.entity(expected, MediaType.TEXT_PLAIN));
        assertEquals(expected, r.request().get(String.class));
    }
}

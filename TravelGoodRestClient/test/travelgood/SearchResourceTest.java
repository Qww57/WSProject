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
 * @author dsanz006
 */
public class SearchResourceTest {
    
    @Test
    public void searchOneHotel() {
    //input stuff for searching flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        //create object (STILL WITHOUT DATES!!!)
        SearchInputRepresentation.SearchHotelInputRepresentation myHotel = new SearchInputRepresentation().new SearchHotelInputRepresentation("Paris", null, null);
        mySearch.hotelsList.add(myHotel);
        
        
    }
    
}
